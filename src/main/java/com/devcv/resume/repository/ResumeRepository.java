package com.devcv.resume.repository;

import com.devcv.resume.domain.Resume;
import com.devcv.resume.domain.enumtype.CompanyType;
import com.devcv.resume.domain.enumtype.ResumeStatus;
import com.devcv.resume.domain.enumtype.StackType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ResumeRepository extends JpaRepository<Resume, Long> {

    // 판매내역 이력서 상세 조회
    @Query("SELECT r FROM Resume r WHERE r.resumeId = :resumeId AND r.member.memberId = :memberId")
    Optional<Resume> findByIdAndMemberId(Long resumeId, Long memberId);

    // 기본 이력서 조회
    Page<Resume> findByStatus(ResumeStatus status, Pageable pageable);

    // 직무 & 회사별 이력서 조회
    Page<Resume> findByCategory_StackTypeAndCategory_CompanyTypeAndStatus(StackType stackType, CompanyType companyType, ResumeStatus status, Pageable pageable);

    // 직무별 이력서 조회
    Page<Resume> findByCategory_StackTypeAndStatus(StackType stackType, ResumeStatus status, Pageable pageable);

    // 회사별 이력서 조회
    Page<Resume> findByCategory_CompanyTypeAndStatus(CompanyType companyType, ResumeStatus status, Pageable pageable);

    // 상세 이력서 조회
    @Query("SELECT r FROM Resume r WHERE r.resumeId = :resumeId AND r.status = '등록완료'")
    Optional<Resume> findByIdAndStatus(Long resumeId);

    //----------------등록완료 default인 메서드 start-----------------
    default Page<Resume> findApprovedResumes(Pageable pageable) {
        return findByStatus(ResumeStatus.등록완료, pageable);
    }
    default Page<Resume> findApprovedResumesByStackTypeAndCompanyType(StackType stackType, CompanyType companyType, Pageable pageable) {
        return findByCategory_StackTypeAndCategory_CompanyTypeAndStatus(stackType, companyType, ResumeStatus.등록완료, pageable);
    }

    default Page<Resume> findApprovedResumesByStackType(StackType stackType, Pageable pageable) {
        return findByCategory_StackTypeAndStatus(stackType, ResumeStatus.등록완료, pageable);
    }

    default Page<Resume> findApprovedResumesByCompanyType(CompanyType companyType, Pageable pageable) {
        return findByCategory_CompanyTypeAndStatus(companyType, ResumeStatus.등록완료, pageable);
    }

    //----------------판매승인이 default인 메서드 end-----------------


}
