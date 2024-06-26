package com.devcv.resume.domain.dto;


import com.devcv.resume.domain.Resume;
import com.devcv.resume.domain.ResumeImage;
import com.devcv.resume.domain.enumtype.ResumeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeDto {
    private Long resumeId;
    private int price;
    private String title;
    private String content;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String resumeFilePath;
    private ResumeStatus status;
    private List<String> stack;
    private List<ResumeImage> imageList;
    private CategoryDto category;
    private Long memberId;
    private String sellerNickname;
    private String sellerEmail;

    // 평균 별점, 구매후기 개수 필드 추가
    private int averageGrade;
    private Long reviewCount;

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
                CategoryDto.from(resume.getCategory()),
                resume.getMember().getMemberId(),
                resume.getMember().getNickName(),
                resume.getMember().getEmail(),
                0,
                null
        );
    }

    public static ResumeDto entityToDto(Resume resume, Double averageGrade, Long reviewCount, boolean includeFilePath) {
        ResumeDto resumeDto = ResumeDto.builder()
                .resumeId(resume.getResumeId())
                .price(resume.getPrice())
                .title(resume.getTitle())
                .content(resume.getContent())
                .resumeFilePath(includeFilePath ? resume.getResumeFilePath() : null)
                .status(resume.getStatus())
                .stack(resume.getStack())
                .memberId(resume.getMember().getMemberId())
                .sellerNickname(resume.getMember().getNickName())
                .sellerEmail(resume.getMember().getEmail())
                .imageList(resume.getImageList())
                .category(CategoryDto.from(resume.getCategory()))
                .averageGrade(averageGrade != null ? averageGrade.intValue() : 0)
                .build();

        resumeDto.setReviewCount(reviewCount);

        return resumeDto;

    }

}
