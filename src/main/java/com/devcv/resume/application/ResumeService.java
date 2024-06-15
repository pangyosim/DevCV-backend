package com.devcv.resume.application;

import com.devcv.member.domain.dto.MemberResponse;
import com.devcv.resume.domain.Resume;
import com.devcv.resume.domain.dto.PaginatedResumeResponse;
import com.devcv.resume.domain.dto.ResumeDto;
import com.devcv.resume.domain.dto.ResumeRequest;
import com.devcv.resume.domain.enumtype.CompanyType;
import com.devcv.resume.domain.enumtype.StackType;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface ResumeService {
    // 이력서 목록 조회
    PaginatedResumeResponse findResumes(StackType stackType, CompanyType companyType, int page, int size);
    // 이력서 상세 조회
    ResumeDto getResumeDetail(Long resumeId);
    MemberResponse getMemberResponse(Long memberId);
    //이력서 판매승인 요청
    Resume register(MemberResponse memberResponse, ResumeRequest resumeRequest);
    // 이력서 판매내역 상세 조회
    ResumeDto getRegisterResumeDetail(Long resumeId);
    // 이력서 등록완료 요청
    Resume completeRegistration(MemberResponse memberResponse, Long resumeId);
}
