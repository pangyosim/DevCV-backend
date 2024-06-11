package com.devcv.register.domain.dto;

import com.devcv.register.domain.Resume;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResumeResponse {

    private Long resumeId;
    private String title;
    private int price;
    private String resumeFilePath;
    private String thumbnail;


    public static ResumeResponse from(Resume resume) {
        return new ResumeResponse(
                resume.getResumeId(),
                resume.getTitle(),
                resume.getPrice(),
                resume.getResumeFilePath(),
                getThumbnailFromResume(resume) // 썸네일 이미지를 직접 가져오는 메서드
        );
    }
    private static String getThumbnailFromResume(Resume resume) {
        if (resume.getImageList() != null && !resume.getImageList().isEmpty()) {
            return resume.getImageList().get(0).getResumeImgPath(); // 0번째 이미지 가져오기
        }
        return null;
    }


}
