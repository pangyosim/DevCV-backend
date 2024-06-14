package com.devcv.resume.application;

import com.devcv.common.exception.ErrorCode;
import com.devcv.member.domain.Member;
import com.devcv.member.domain.dto.MemberResponse;
import com.devcv.member.exception.NotNullException;
import com.devcv.member.repository.MemberRepository;
import com.devcv.resume.domain.Resume;
import com.devcv.resume.domain.Category;
import com.devcv.resume.domain.dto.*;
import com.devcv.resume.domain.enumtype.CompanyType;
import com.devcv.resume.domain.enumtype.StackType;
import com.devcv.resume.exception.MemberNotFoundException;
import com.devcv.resume.exception.ResumeNotFoundException;
import com.devcv.resume.infrastructure.S3Uploader;
import com.devcv.resume.domain.ResumeImage;
import com.devcv.resume.domain.enumtype.ResumeStatus;
import com.devcv.resume.repository.CategoryRepository;
import com.devcv.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;


    @Override
    public PaginatedResumeResponse findResumes(StackType stackType, CompanyType companyType, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Resume> resumePage;

        if (stackType != null && companyType != null) {
            // 직무 & 회사별 목록 조회
            resumePage = resumeRepository.findApprovedResumesByStackTypeAndCompanyType(stackType, companyType, pageable);
        } else if (stackType != null) {
            // 직무별 목록 조회
            resumePage = resumeRepository.findApprovedResumesByStackType(stackType, pageable);
        } else if (companyType != null) {
            // 회사별 목록 조회
            resumePage = resumeRepository.findApprovedResumesByCompanyType(companyType, pageable);
        } else {
            // 전체 목록 조회
            resumePage = resumeRepository.findApprovedResumes(pageable);
        }

        List<ResumeResponse> resumeDTOs = resumePage.getContent()
                .stream()
                .map(ResumeResponse::from)
                .collect(Collectors.toList());

        int currentPage = resumePage.getNumber() + 1;
        int totalPages = resumePage.getTotalPages();
        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);

        return new PaginatedResumeResponse(
                resumeDTOs,
                resumePage.getTotalElements(),
                resumePage.getNumberOfElements(),
                currentPage,
                totalPages,
                resumePage.getSize(),
                startPage,
                endPage
        );
    }

    @Override
    public ResumeDto getResumeDetail(Long resumeId) {
        Resume resume = resumeRepository.findByIdAndStatus(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException(ErrorCode.RESUME_NOT_FOUND));

        return ResumeDto.from(resume);
    }


    @Override
    public MemberResponse getMemberResponse(Long memberid) {
        Member member = memberRepository.findMemberBymemberid(memberid);
        if (member == null) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return MemberResponse.from(member);
    }

    @Override
    public Resume register(MemberResponse memberResponse, ResumeRequest resumeRequest) {

        // 회원 아이디 조회, 추후 security 설정 시 삭제
        Member member = memberRepository.findMemberByUserId(memberResponse.getUserId());
        if (member == null) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        // Category 저장
        CategoryDto categoryDTO = resumeRequest.getCategory();
        List<Category> categories = categoryRepository.findByCompanyTypeAndStackType(
                categoryDTO.getCompanyType(),
                categoryDTO.getStackType()
        );
        Category category;
        if (categories.isEmpty()) {
            // 리스트가 비어 있으면 새로운 Category 생성 및 저장
            category = new Category(categoryDTO.getCompanyType(), categoryDTO.getStackType());
            category = categoryRepository.save(category);
        } else {
            // 리스트가 비어 있지 않으면 첫 번째 요소 사용
            category = categories.get(0);
        }

        // PDF 파일 업로드
        String resumeFilePath = null;
        if (resumeRequest.getResumeFile() != null) {
            resumeFilePath = s3Uploader.upload(resumeRequest.getResumeFile());
            log.debug("Uploaded resume file path: {}", resumeFilePath);
        }

        // 필수 데이터 검증
        if (resumeRequest.getPrice() < 0 || resumeRequest.getTitle() == null
                || resumeRequest.getContent() == null || resumeRequest.getCategory() == null) {
            throw new NotNullException(ErrorCode.NULL_ERROR);
        }

        Resume resume = Resume.builder()
                .member(member)
                .price(resumeRequest.getPrice())
                .title(resumeRequest.getTitle())
                .content(resumeRequest.getContent())
                .resumeFilePath(resumeFilePath)
                .stack(resumeRequest.getStack())
                .category(category)
                .build();

        // 이미지 파일 업로드
        List<MultipartFile> imageFiles = resumeRequest.getImageFiles();
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (int i = 0; i < imageFiles.size(); i++) {
                String imagePath = s3Uploader.upload(imageFiles.get(i));
                ResumeImage resumeImage = ResumeImage.builder()
                        .resumeImgPath(imagePath)
                        .ord(i)
                        .build();
                resume.addImage(resumeImage);
            }
        }

        // 일단 임의 판매승인, 추후 관리자 mvp 완성 시 변경
        resume.setStatus(ResumeStatus.판매승인);

//        // 상태 설정
//        resume.setStatus(ResumeStatus.승인대기);

        return resumeRepository.save(resume);
    }

    @Override
    public Resume completeRegistration(MemberResponse memberResponse, Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException(ErrorCode.RESUME_NOT_FOUND));

        Member member = memberRepository.findMemberByUserId(memberResponse.getUserId());
        if (member == null) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        resume.setStatus(ResumeStatus.등록완료);
        return resumeRepository.save(resume);
    }

    @Override
    public Resume findRegisteredResumeByMember(Long memberId) {
        Resume approvedResume = resumeRepository.findFirstApprovedByMemberIdOrderByCreatedAtAsc(memberId);
        if (approvedResume != null) {
            return approvedResume;
        } else {
            Resume pendingResume = resumeRepository.findFirstPendingByMemberIdOrderByCreatedAtAsc(memberId);
            return pendingResume;
        }
    }

}
