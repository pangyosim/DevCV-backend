package com.devcv.resume.domain.dto;

import com.devcv.resume.domain.enumtype.CompanyType;
import com.devcv.resume.domain.enumtype.StackType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long categoryId;
    private CompanyType companyType;
    private StackType stackType;
}
