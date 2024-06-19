package com.devcv.resume.application;

import com.devcv.common.exception.ErrorCode;
import com.devcv.member.domain.Member;
import com.devcv.member.repository.MemberRepository;
import com.devcv.resume.domain.Category;
import com.devcv.resume.domain.Resume;
import com.devcv.resume.domain.ResumeImage;
import com.devcv.resume.domain.dto.CategoryDto;
import com.devcv.resume.domain.dto.PaginatedResumeResponse;
import com.devcv.resume.domain.dto.ResumeDto;
import com.devcv.resume.domain.dto.ResumeRequest;
import com.devcv.resume.domain.enumtype.CompanyType;
import com.devcv.resume.domain.enumtype.ResumeStatus;
import com.devcv.resume.domain.enumtype.StackType;
import com.devcv.resume.exception.HttpMessageNotReadableException;
import com.devcv.resume.exception.MemberNotFoundException;
import com.devcv.resume.exception.ResumeNotFoundException;
import com.devcv.resume.infrastructure.S3Uploader;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static com.devcv.resume.domain.dto.ResumeDto.entityToDto;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;


    // 이력서 목록 조회
    @Override
    public PaginatedResumeResponse findResumes(StackType stackType, CompanyType companyType, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Object[]> resumePage;

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

        List<ResumeDto> resumeDTOs = resumePage.getContent()
                .stream()
                .map(objects -> {
                    Resume resume = (Resume) objects[0];
                    Double averageGrade = (Double) objects[1];
                    Long reviewCount = (Long) objects[2];
                    return ResumeDto.entityToDto(resume, averageGrade, reviewCount);
                })
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

    // 이력서 상세 조회
    @Override
    public ResumeDto getResumeDetail(Long resumeId) {
        List<Object[]>result = resumeRepository.findByIdAndStatus(resumeId);

        Resume resume = (Resume) result.get(0)[0];

        Double averageGrade = (Double) result.get(0)[1];
        Long reviewCount = (Long) result.get(0)[2];

        return entityToDto(resume, averageGrade, reviewCount);
    }

    // 이력서 등록(승인대기)
    @Override
    public Resume register(ResumeRequest resumeRequest, MultipartFile resumeFile, List<MultipartFile> images, Long memberId) {

        Member member = memberRepository.findMemberBymemberId(memberId);

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
        if (resumeFile != null) {
            resumeFilePath = s3Uploader.upload(resumeFile);
            log.debug("Uploaded resume file path: {}", resumeFilePath);
        }

        if (resumeRequest.getPrice() == null || resumeRequest.getTitle() == null || resumeRequest.getContent() == null ||
                resumeRequest.getStack() == null || resumeRequest.getCategory() == null) {
            throw new HttpMessageNotReadableException(ErrorCode.EMPTY_VALUE_ERROR);
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
        if (images != null && !images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                String imagePath = s3Uploader.upload(images.get(i));
                ResumeImage resumeImage = ResumeImage.builder()
                        .resumeImgPath(imagePath)
                        .ord(i)
                        .build();

                resume.addImage(resumeImage);
            }
        }

         //일단 임의 판매승인, 추후 관리자 mvp 완성 시 변경
         resume.setStatus(ResumeStatus.판매승인);

        // 상태 설정
        // resume.setStatus(ResumeStatus.승인대기);


        return resumeRepository.save(resume);

    }

    // 이력서 판매내역 상세조회
    @Override
    public ResumeDto getRegisterResumeDetail(Long memberId, Long resumeId) {
        Optional<Resume> resumeOpt = resumeRepository.findByIdAndMemberId(resumeId, memberId);
        if (resumeOpt.isPresent()) {
            Resume resume = resumeOpt.get();
            return ResumeDto.from(resume);
        }else {
            throw new ResumeNotFoundException(ErrorCode.RESUME_NOT_FOUND);
        }
    }

    // 이력서 최종 판매등록
    @Override
    public Resume completeRegistration( Long memberId, Long resumeId) {
        Member member = memberRepository.findMemberBymemberId(memberId);
        if (member == null) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
        Optional<Resume> resumeOpt = resumeRepository.findByIdAndMemberId(resumeId, memberId);
        if (resumeOpt.isPresent()) {
            Resume resume = resumeOpt.get();
            resume.setStatus(ResumeStatus.등록완료);
            return resumeRepository.save(resume);
        } else {
            throw new ResumeNotFoundException(ErrorCode.RESUME_NOT_FOUND);
        }
    }

    @Override
    public Resume findByResumeId(Long resumeId) {
        return resumeRepository.findByResumeId(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException(ErrorCode.RESUME_NOT_FOUND));
    }
}
