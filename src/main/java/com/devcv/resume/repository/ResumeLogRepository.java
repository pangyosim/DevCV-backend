package com.devcv.resume.repository;

import com.devcv.resume.domain.ResumeLog;
import com.devcv.resume.domain.enumtype.ResumeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeLogRepository extends JpaRepository<ResumeLog, Long> {
    List<ResumeLog> findByResumeId(Long resumeId);
    List<ResumeLog> findByResumeIdAndStatus(Long resumeId, ResumeStatus status);

}
