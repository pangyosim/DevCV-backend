package com.devcv.order.domain.dto;

import com.devcv.member.domain.Member;
import com.devcv.member.domain.dto.MemberResponse;
import com.devcv.resume.domain.Resume;
import com.devcv.resume.domain.dto.ResumeResponse;

public record OrderSheet(MemberResponse memberResponse,
                         ResumeResponse resumeResponse) {

    public static OrderSheet of(Member member,
                                Resume resume) {
        return new OrderSheet(
                MemberResponse.from(member),
                ResumeResponse.from(resume));
    }
}