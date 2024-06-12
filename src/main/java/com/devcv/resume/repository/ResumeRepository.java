package com.devcv.resume.repository;

import com.devcv.resume.domain.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ResumeRepository extends JpaRepository<Resume, Long> {

    // 판매승인 된 이력서 조회
    @Query(value = "SELECT * FROM tb_resume r WHERE r.member_memberid = :memberId AND r.status = '판매승인' ORDER BY r.created_date ASC LIMIT 1", nativeQuery = true)
    Resume findFirstApprovedByMemberIdOrderByCreatedAtAsc(@Param("memberId") Long memberId);

    // 승인대기 중인 이력서 조회
    @Query(value = "SELECT * FROM tb_resume r WHERE r.member_memberid = :memberId AND r.status = '승인대기' ORDER BY r.created_date ASC LIMIT 1", nativeQuery = true)
    Resume findFirstPendingByMemberIdOrderByCreatedAtAsc(@Param("memberId") Long memberId);


}
