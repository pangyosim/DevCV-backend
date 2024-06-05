package com.devcv.register.repository;

import com.devcv.register.domain.Category;
import com.devcv.register.domain.enumtype.CompanyType;
import com.devcv.register.domain.enumtype.StackType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.companyType = :companyType AND c.stackType= :stackType")
    Optional<Category> findByCompanyTypeAndStackType(CompanyType companyType, StackType stackType);

}
