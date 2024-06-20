package com.devcv.resume.domain.dto;


import com.devcv.resume.domain.Resume;
import com.devcv.resume.domain.ResumeImage;
import com.devcv.resume.domain.enumtype.ResumeStatus;
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
    private String resumeFilePath;
    private ResumeStatus status;
    private List<String> stack;
    private List<ResumeImage> imageList;
    private CategoryDto category;
    private Long memberId;
    private String sellerNickname;

    // 평균 별점, 구매후기 개수 필드 추가
    private Double averageGrade;
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
                null,
                null
        );
    }

    public static ResumeDto entityToDto(Resume resume, Double averageGrade, Long reviewCount) {
        ResumeDto resumeDto = ResumeDto.builder()
                .resumeId(resume.getResumeId())
                .price(resume.getPrice())
                .title(resume.getTitle())
                .content(resume.getContent())
                .resumeFilePath(resume.getResumeFilePath())
                .status(resume.getStatus())
                .stack(resume.getStack())
                .memberId(resume.getMember().getMemberId())
                .sellerNickname(resume.getMember().getNickName())
                .imageList(resume.getImageList())
                .category(CategoryDto.from(resume.getCategory()))
                .build();

        resumeDto.setAverageGrade(averageGrade);
        resumeDto.setReviewCount(reviewCount);

        return resumeDto;

    }

}
