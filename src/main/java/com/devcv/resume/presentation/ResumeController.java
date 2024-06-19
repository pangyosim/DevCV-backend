package com.devcv.resume.presentation;

import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.InternalServerException;
import com.devcv.common.exception.UnAuthorizedException;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    //------------이력서 목록 조회 요청 start---------------
    @GetMapping()
    public ResponseEntity<PaginatedResumeResponse> getResumesByConditions(
            @RequestParam("page") int page, @RequestParam("size") int size,
            @RequestParam(value = "stackType", required = false) StackType stackType,
            @RequestParam(value = "companyType", required = false) CompanyType companyType) {

        try {
            PaginatedResumeResponse response = resumeService.findResumes(stackType, companyType, page, size);
            return ResponseEntity.ok(response);
        } catch (InternalServerException e) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    //------------이력서 목록 조회 요청 end---------------


    //------------이력서 상세 조회 요청 start--------------
    @GetMapping("/{resume-id}")
    public ResponseEntity<ResumeDto> getResumeDetail(@PathVariable("resume-id") Long resumeId) {
        try {
            ResumeDto resumeDetail = resumeService.getResumeDetail(resumeId);
            return ResponseEntity.ok(resumeDetail);
        }catch(InternalServerException e) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    //------------이력서 상세 조회 요청 end---------------



    // -------이력서 승인대기 요청 start-------------
    @PostMapping()
    public ResponseEntity<ResumeDto> registerResume(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("resume") ResumeRequest resumeRequest,
            @RequestPart("resumeFile") MultipartFile resumeFile,
            @RequestPart("images") List<MultipartFile> images) {

        if(userDetails == null) {
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        Long memberId = Long.valueOf(userDetails.getUsername());
        Resume createdResume = resumeService.register(resumeRequest, resumeFile, images, memberId);
        ResumeDto resumeDto = ResumeDto.from(createdResume);
        return ResponseEntity.ok(resumeDto);
    }

    //----------이력서 승인대기 요청 end---------------


    //----------이력서 판매 상세 내역 페이지 호출 start---------
    @GetMapping("/myresume/{resume-id}")
    public ResponseEntity<?> getResumeForEdit(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable("resume-id") Long resumeId) {
        if(userDetails == null) {
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_ERROR);
        }
        Long memberId = Long.valueOf(userDetails.getUsername());
        ResumeDto resumeDetail = resumeService.getRegisterResumeDetail(memberId, resumeId);

        return ResponseEntity.ok().body(resumeDetail);
    }
    //----------이력서 판매 상세 내역 페이지 페이지 호출 end-----------


    //----------이력서 판매 등록 요청 start----------------
    @PutMapping("/myresume/{resume-id}/status")
    public ResponseEntity<?> completeResumeRegistration(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("resume-id") Long resumeId) {
        if(userDetails == null) {
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_ERROR);
        }
        Long memberId = Long.valueOf(userDetails.getUsername());
        Resume completedResume = resumeService.completeRegistration(memberId, resumeId);

        return ResponseEntity.ok(ResumeDto.from(completedResume));
    }
    //----------이력서 판매 등록 요청 end----------------



}


