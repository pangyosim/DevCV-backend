package com.devcv.order.domain.dto;

import com.devcv.member.Domain.Member;
import com.devcv.member.Domain.dto.MemberResponse;
import com.devcv.resume.Resume;
import com.devcv.resume.ResumeResponse;

public record OrderSheet(MemberResponse memberResponse,
                         ResumeResponse resumeResponse) {

    public static OrderSheet of(Member member,
                                Resume resume) {
        return new OrderSheet(
                MemberResponse.from(member),
                ResumeResponse.from(resume));
    }
}