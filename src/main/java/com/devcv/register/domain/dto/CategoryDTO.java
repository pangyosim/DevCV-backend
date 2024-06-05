package com.devcv.register.domain.dto;

import com.devcv.register.domain.enumtype.CompanyType;
import com.devcv.register.domain.enumtype.StackType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Long categoryId;
    private CompanyType companyType;
    private StackType stackType;
}
