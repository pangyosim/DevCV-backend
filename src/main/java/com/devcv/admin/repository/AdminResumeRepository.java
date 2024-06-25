package com.devcv.admin.repository;

import com.devcv.resume.domain.Resume;
import com.devcv.resume.domain.enumtype.ResumeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminResumeRepository extends JpaRepository<Resume, Long> {

    @EntityGraph(attributePaths = {"member", "category", "imageList"}, type=EntityGraph.EntityGraphType.FETCH)
    Page<Resume> findAllByStatus(@Param("status")ResumeStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"member", "category", "imageList"}, type=EntityGraph.EntityGraphType.FETCH)
    Optional<Resume> findByResumeId(Long resumeId);
}
