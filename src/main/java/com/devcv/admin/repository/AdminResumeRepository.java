package com.devcv.admin.repository;

import com.devcv.resume.domain.Resume;
import com.devcv.resume.domain.enumtype.ResumeStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminResumeRepository extends JpaRepository<Resume, Long> {

    @EntityGraph(attributePaths = {"member", "category"}, type=EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT r FROM Resume r JOIN FETCH r.imageList WHERE r.status = ?1 ORDER BY r.createdDate ASC")
    List<Resume> findByStatus(ResumeStatus status);
}
