package com.devcv.register.presentation;

import com.devcv.member.domain.dto.MemberResponse;
import com.devcv.register.application.ResumeService;
import com.devcv.register.domain.Resume;
import com.devcv.register.domain.dto.ResumeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;


    @PostMapping("/add")
    public ResponseEntity<Resume> registerResume(
            //  로그인된 사용자 정보 가져오기 -> 현재 security 미설정으로 인해 주석처리
            // 추후 security 연결 이후 주석 해제 및 주석 2번 삭제
//            @AuthenticationPrincipal Member member,
            @RequestPart("member") MemberResponse memberResponse, // 2
            @RequestPart("resume") ResumeRequest resumeRequest,
            @RequestPart("resumeFile") MultipartFile resumeFile,
            @RequestPart("images") List<MultipartFile> images) {


        // 파일들을 DTO에 설정
        resumeRequest.setResumeFile(resumeFile);
        resumeRequest.setImageFiles(images);

        // 현재는 편의성을 위해 member가 아닌 memberResponse로 받아옴
        // 추후 security 연결 이후 member로 바꾸기
        Resume createdResume = resumeService.register(memberResponse, resumeRequest);

        return ResponseEntity.ok(createdResume);
    }
}
