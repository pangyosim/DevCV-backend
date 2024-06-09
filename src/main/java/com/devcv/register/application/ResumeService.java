package com.devcv.register.application;

import com.devcv.member.domain.dto.MemberResponse;
import com.devcv.register.domain.Resume;
import com.devcv.register.domain.dto.ResumeRequest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface ResumeService {
    //이력서 판매등록
    Resume register(MemberResponse memberResponse, ResumeRequest resumeRequest);
}
