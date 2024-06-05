package com.devcv.register.application;

import com.devcv.register.domain.Category;
import com.devcv.register.domain.Resume;
import com.devcv.register.domain.ResumeImage;
import com.devcv.register.domain.dto.CategoryDTO;
import com.devcv.register.domain.dto.ResumeDTO;
import com.devcv.register.domain.dto.ResumeImageDTO;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Component
public class ResumeMapper {

    public static Resume dtoToEntity(ResumeDTO resumeDTO, Category category) {
        List<ResumeImage> resumeImages = resumeDTO.getImageList() != null ? resumeDTO.getImageList().stream()
                .map(ResumeImageMapper::dtoToEntity)
                .collect(Collectors.toList()) : new ArrayList<>();

        return Resume.builder()
                .resumeId(resumeDTO.getResumeId())
                .price(resumeDTO.getPrice())
                .title(resumeDTO.getTitle())
                .content(resumeDTO.getContent())
                .resumeFilePath(resumeDTO.getResumeFilePath())
                .status(resumeDTO.getResumeStatus())
                .isStack(resumeDTO.getIsStack())
                .imageList(resumeImages)
                .category(category)
                .build();
    }


    public static ResumeDTO entityToDto(Resume resume) {
        List<ResumeImageDTO> resumeImageDTOS = resume.getImageList() != null ? resume.getImageList().stream()
                .map(ResumeImageMapper::entityToDto)
                .collect(Collectors.toList()) : new ArrayList<>();

        CategoryDTO categoryDTO = CategoryDTO.builder()
                .categoryId(resume.getCategory().getCategoryId())
                .companyType(resume.getCategory().getCompanyType())
                .stackType(resume.getCategory().getStackType())
                .build();

        return ResumeDTO.builder()
                .resumeId(resume.getResumeId())
                .price(resume.getPrice())
                .title(resume.getTitle())
                .content(resume.getContent())
                .resumeFilePath(resume.getResumeFilePath())
                .resumeStatus(resume.getStatus())
                .isStack(resume.getIsStack())
                .category(categoryDTO)
                .imageList(resumeImageDTOS)
                .build();
    }

}
