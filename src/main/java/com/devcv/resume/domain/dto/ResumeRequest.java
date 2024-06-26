package com.devcv.resume.domain.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ResumeRequest {
    private Integer price;
    private String title;
    private String content;
    private MultipartFile resumeFile;
    private List<String> stack;
    private CategoryDto category;
    private List<MultipartFile> imageFiles;
}
