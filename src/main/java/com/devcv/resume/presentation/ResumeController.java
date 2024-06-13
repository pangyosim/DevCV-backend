package com.devcv.resume.presentation;

import com.devcv.member.domain.dto.MemberResponse;
import com.devcv.resume.application.ResumeService;
import com.devcv.resume.domain.Resume;
import com.devcv.resume.domain.dto.PaginatedResumeResponse;
import com.devcv.resume.domain.dto.ResumeDto;
import com.devcv.resume.domain.dto.ResumeRequest;
import com.devcv.resume.domain.enumtype.CompanyType;
import com.devcv.resume.domain.enumtype.StackType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// security 설정 이후 공통 파라미터 변경, //2 삭제
//            @AuthenticationPrincipal CustomUserDetails userDetails,

// 인증된 사용자 정보 조회 통한 memberResponse get 로직 츠기
//        Long userId = userDetails.getUserId();
//        MemberResponse memberResponse = resumeService.getMemberResponse(userId);

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    //------------이력서 목록 조회 요청 start---------------
    @GetMapping()
    public ResponseEntity<PaginatedResumeResponse> getResumesByConditions(
            @RequestParam("page") int page, @RequestParam("size") int size,
            @RequestParam(value = "stackType", required = false) StackType stackType,
            @RequestParam(value = "companyType", required = false) CompanyType companyType) {
        PaginatedResumeResponse response = resumeService.findResumes(stackType, companyType, page, size);
        return ResponseEntity.ok(response);
    }
    //------------이력서 목록 조회 요청 end---------------


    //------------이력서 상세 조회 요청 start--------------
    @GetMapping("/{resumeId}")
    public ResponseEntity<ResumeDto> getResumeDetail(@PathVariable Long resumeId) {
        ResumeDto resumeDetail = resumeService.getResumeDetail(resumeId);
        return ResponseEntity.ok(resumeDetail);
    }
    //------------이력서 상세 조회 요청 end---------------



    //------이력서 등록 페이지 호출 start --------
    @GetMapping("/add")
    public ResponseEntity<?> newResumePage(
            @RequestPart("userId") Long userId ) { //2

        // 회원정보 조회 후 에러 처리를 위한 임시 메서드->추후 security 설정 이후 변경
        MemberResponse memberResponse = resumeService.getMemberResponse(userId);
        // 새로운 이력서 페이지를 위한 기본 ResumeDto 생성
        ResumeDto newResume = new ResumeDto();
        newResume.setMemberId(memberResponse.getUserId());
        newResume.setSellerNickname(memberResponse.getNickName());

        return ResponseEntity.ok().body(newResume);

    }
    //-------이력서 등록 페이지 호출 end -----------


    // -------이력서 승인대기 요청 start-------------
    @PostMapping("/add")
    public ResponseEntity<Resume> registerResume(
            @RequestPart("member") MemberResponse memberResponse, // 2
            @RequestPart("resume") ResumeRequest resumeRequest,
            @RequestPart("resumeFile") MultipartFile resumeFile,
            @RequestPart("images") List<MultipartFile> images) {

        // 파일들 DTO 설정
        resumeRequest.setResumeFile(resumeFile);
        resumeRequest.setImageFiles(images);


        Resume createdResume = resumeService.register(memberResponse, resumeRequest);

        return ResponseEntity.ok(createdResume);
    }

    //----------이력서 승인대기 요청 end---------------


    //----------이력서 판매 상세 내역 페이지 호출 start---------
    @GetMapping("/myresume/{resumeId}")
    public ResponseEntity<ResumeDto> getResumeForEdit(@RequestParam("userId") Long userId, @PathVariable Long resumeId) {
        MemberResponse memberResponse = resumeService.getMemberResponse(userId);
        ResumeDto resumeDetail = resumeService.getRegisterResumeDetail(resumeId);
        return ResponseEntity.ok(resumeDetail);
    }
    //----------이력서 판매 상세 내역 페이지 페이지 호출 end-----------


    //----------이력서 판매 등록 요청 start----------------
    @PostMapping("/complete")
    public ResponseEntity<?> completeResumeRegistration(
            @RequestPart("member") MemberResponse memberResponse, // 2
            @RequestPart("resumeId") Long resumeId) {

        Resume completedResume = resumeService.completeRegistration(memberResponse, resumeId);
        return ResponseEntity.ok(ResumeDto.from(completedResume));
    }
    //----------이력서 판매 등록 요청 end----------------



}


