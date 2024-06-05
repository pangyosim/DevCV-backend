package com.devcv.register.application;

import com.devcv.register.domain.ResumeImage;
import com.devcv.register.domain.dto.ResumeImageDTO;

public class ResumeImageMapper {

    public static ResumeImage dtoToEntity(ResumeImageDTO resumeImageDTO) {
        return ResumeImage.builder()
                .resumeImgPath(resumeImageDTO.getResumeImgPath())
                .ord(resumeImageDTO.getOrd())
                .build();
    }

    public static ResumeImageDTO entityToDto(ResumeImage resumeImage) {
        return ResumeImageDTO.builder()
                .resumeImgPath(resumeImage.getResumeImgPath())
                .ord(resumeImage.getOrd())
                .build();
    }
}