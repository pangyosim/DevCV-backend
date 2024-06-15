package com.devcv.member.application;

import com.devcv.member.domain.Member;
import com.devcv.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
*   ********************************* MemberService 하위 메서드 기능 정리 *********************************
*
*       ************************************ SELECT ************************************
*       findMemberByEmail : email로 해당 회원정보 SELECT.
*       findMemberByMemberid : memberid로 해당 회원정보 SELECT.
*       findMemberByMemberNameAndPhone: 이름&전화번호 정보로 해당 회원정보 SELECT.
*       ************************************ SELECT ************************************
*
*       ************************************ INSERT ************************************
*       signup : 회원가입 INSERT.
*       ************************************ INSERT ************************************
*
*       ************************************ UPDATE ************************************
*       updatePasswordByMemberId: memberid로 해당 회원 비밀번호 UPDATE.
*       updateMemberByMemberId: memberid로 해당 회원정보 UPDATE.
*       ************************************ UPDATE ************************************
*
*   ********************************* MemberService 하위 메서드 기능 정리 *********************************
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    public void signup(Member member){memberRepository.save(member);}
    public Member findMemberByEmail(String email){ return memberRepository.findMemberByEmail(email);}
    public Member findMemberByMemberId(Long memberid){ return memberRepository.findMemberBymemberid(memberid);}
    public Member findMemberByMemberNameAndPhone(String memberName, String phone) { return memberRepository.findMemberByMemberNameAndPhone(memberName,phone);}
    public int updatePasswordByMemberId(String password,Long memberid) { return memberRepository.updatePasswordByMemberId(password,memberid);}
    public int updateMemberByMemberId(String memberName, String email, String password, String nickname, String phone, String address, String issocial, String iscompany, String isjob, String isstack, Long memberid) { return memberRepository.updateMemberByMemberId(memberName,email,password,nickname,phone,address,issocial,iscompany,isjob,isstack,memberid);}
}
