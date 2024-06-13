package com.devcv.resume.repository;

import com.devcv.resume.domain.Resume;
import com.devcv.resume.domain.enumtype.CompanyType;
import com.devcv.resume.domain.enumtype.ResumeStatus;
import com.devcv.resume.domain.enumtype.StackType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ResumeRepository extends JpaRepository<Resume, Long> {

    // 판매승인 된 이력서 조회
    @Query(value = "SELECT * FROM tb_resume r WHERE r.member_userid = :memberId AND r.status = '판매승인' ORDER BY r.created_date ASC LIMIT 1", nativeQuery = true)
    Resume findFirstApprovedByMemberIdOrderByCreatedAtAsc(@Param("memberId") Long memberId);

    // 승인대기 중인 이력서 조회
    @Query(value = "SELECT * FROM tb_resume r WHERE r.member_userid = :memberId AND r.status = '승인대기' ORDER BY r.created_date ASC LIMIT 1", nativeQuery = true)
    Resume findFirstPendingByMemberIdOrderByCreatedAtAsc(@Param("memberId") Long memberId);

    // 상세 이력서 조회
    @Query("SELECT r FROM Resume r WHERE r.resumeId = :resumeId AND r.status = '등록완료'")
    Optional<Resume> findByIdAndStatus(Long resumeId);

    // 기본 이력서 조회
    Page<Resume> findByStatus(ResumeStatus status, Pageable pageable);

    // 직무 & 회사별 이력서 조회
    Page<Resume> findByCategory_StackTypeAndCategory_CompanyTypeAndStatus(StackType stackType, CompanyType companyType, ResumeStatus status, Pageable pageable);

    // 직무별 이력서 조회
    Page<Resume> findByCategory_StackTypeAndStatus(StackType stackType, ResumeStatus status, Pageable pageable);

    // 회사별 이력서 조회
    Page<Resume> findByCategory_CompanyTypeAndStatus(CompanyType companyType, ResumeStatus status, Pageable pageable);

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
