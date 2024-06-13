package com.devcv.resume.domain;

import com.devcv.resume.domain.enumtype.CompanyType;
import com.devcv.resume.domain.enumtype.StackType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    // 회사 카테고리
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    // 직무 카테고리
    @Enumerated(EnumType.STRING)
    private StackType stackType;

    public Category(CompanyType companyType, StackType stackType) {
        this.companyType = companyType;
        this.stackType = stackType;
    }
}
