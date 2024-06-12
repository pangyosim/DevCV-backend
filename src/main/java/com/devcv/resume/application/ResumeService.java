package com.devcv.resume.application;

import com.devcv.member.domain.dto.MemberResponse;
import com.devcv.resume.domain.Resume;
import com.devcv.resume.domain.dto.ResumeRequest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface ResumeService {
    MemberResponse getMemberResponse(Long memberid);
    //이력서 판매승인 요청
    Resume register(MemberResponse memberResponse, ResumeRequest resumeRequest);
    // 이력서 판매등록 요청
    Resume completeRegistration(MemberResponse memberResponse, Long resumeId);
    // 이력서 조회(상태구분을 위함)
    Resume findRegisteredResumeByMember(Long memberId);
}
