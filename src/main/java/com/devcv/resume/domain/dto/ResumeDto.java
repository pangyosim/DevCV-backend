package com.devcv.resume.domain.dto;


import com.devcv.resume.domain.Resume;
import com.devcv.resume.domain.ResumeImage;
import com.devcv.resume.domain.enumtype.ResumeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResumeDto {
    private Long resumeId;
    private int price;
    private String title;
    private String content;
    private String resumeFilePath;
    private ResumeStatus status;
    private List<String> stack;
    private List<ResumeImage> imageList;
    private Long categoryId;
    private Long memberId;

    public static ResumeDto from(Resume resume) {
        return new ResumeDto(
                resume.getResumeId(),
                resume.getPrice(),
                resume.getTitle(),
                resume.getContent(),
                resume.getResumeFilePath(),
                resume.getStatus(),
                resume.getStack(),
                resume.getImageList(),
                resume.getCategory().getCategoryId(),
                resume.getMember().getUserId()
        );
    }
}