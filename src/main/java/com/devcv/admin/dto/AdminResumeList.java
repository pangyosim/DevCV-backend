package com.devcv.admin.dto;

import com.devcv.resume.domain.dto.ResumeResponse;
import com.devcv.resume.domain.enumtype.ResumeStatus;

import java.util.List;

public record AdminResumeList(ResumeStatus status, int count, List<ResumeResponse> resumeList) {

    public static AdminResumeList of(ResumeStatus status, int count, List<ResumeResponse> resumeList) {
        return new AdminResumeList(status, count, resumeList);
    }
}