package com.devcv.resume.repository;

import com.devcv.resume.domain.Category;
import com.devcv.resume.domain.enumtype.CompanyType;
import com.devcv.resume.domain.enumtype.StackType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.companyType = :companyType AND c.stackType = :stackType")
    List<Category> findByCompanyTypeAndStackType(@Param("companyType") CompanyType companyType,
                                                 @Param("stackType") StackType stackType);
}
