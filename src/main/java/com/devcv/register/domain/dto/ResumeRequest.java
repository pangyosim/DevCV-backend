package com.devcv.register.domain.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ResumeRequest {
    private Long memberid;  // 추후 @AuthenticationPrincipal을 사용할 때는 필요 없음, 삭제 예정
    private int price;
    private String title;
    private String content;
    private MultipartFile resumeFile;
    private List<String> stack;
    private CategoryDTO category;
    private List<MultipartFile> imageFiles;
}
