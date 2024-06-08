package com.devcv.register.domain.dto;

import com.devcv.register.domain.enumtype.ResumeStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeDTO {

    private Long resumeId;
    private int price;
    private String title;
    private String content;
    private String resumeFilePath;
    private List<String> isStack;
    private ResumeStatus resumeStatus;
    private CategoryDTO category;
    @Builder.Default
    private List<ResumeImageDTO> imageList = new ArrayList<>();



}
