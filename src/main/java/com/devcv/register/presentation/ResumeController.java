package com.devcv.register.presentation;

import com.devcv.register.application.ResumeService;
import com.devcv.register.domain.dto.ResumeDTO;
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
    public ResponseEntity<ResumeDTO> registerResume(
            @RequestPart(value = "resume") ResumeDTO resumeDTO,
            @RequestPart("resumeFile") MultipartFile resumeFile,
            @RequestPart("images") List<MultipartFile> images) {
        ResumeDTO createdResume = resumeService.register(resumeDTO, resumeFile, images);
        return ResponseEntity.ok(createdResume);
    }
}
