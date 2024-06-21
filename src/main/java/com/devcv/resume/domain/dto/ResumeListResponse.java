package com.devcv.resume.domain.dto;

import java.util.List;

public record ResumeListResponse (Long memberId, int count, List<ResumeResponse> resumeList){
    public static ResumeListResponse of(Long memberId, int count, List<ResumeResponse> resumeList){
        return new ResumeListResponse(memberId,count,resumeList);
    }
}
