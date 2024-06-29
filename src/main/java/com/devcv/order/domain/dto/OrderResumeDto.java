package com.devcv.order.domain.dto;

import com.devcv.resume.domain.Resume;

public record OrderResumeDto(Long resumeId, String title, Long price, String resumeFilePath, String thumbnailPath) {

    public static OrderResumeDto from(Resume resume) {
        return new OrderResumeDto(
                resume.getResumeId(),
                resume.getTitle(),
                (long) resume.getPrice(),
                resume.getResumeFilePath(),
                resume.getImageList().get(0).getResumeImgPath());
    }
}
