package com.devcv.member.application;

import com.devcv.member.domain.Member;
import com.devcv.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
*   ********************************* MemberService 하위 메서드 기능 정리 *********************************
*
*       ************************************ SELECT ************************************
*       findMemberByEmail : email로 해당 회원정보 SELECT.
*       findMemberByUserId : userId로 해당 회원정보 SELECT.
*       findMemberByUserNameAndPhone: 이름&전화번호 정보로 해당 회원정보 SELECT.
*       ************************************ SELECT ************************************
*
*       ************************************ INSERT ************************************
*       signup : 회원가입 INSERT.
*       ************************************ INSERT ************************************
*
*       ************************************ UPDATE ************************************
*       updatePasswordByUserId: userid로 해당 회원 비밀번호 UPDATE.
*       updateMemberByUserId: userId로 해당 회원정보 UPDATE.
*       ************************************ UPDATE ************************************
*
*   ********************************* MemberService 하위 메서드 기능 정리 *********************************
 */
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    public void signup(Member member){
        memberRepository.save(member);
    };
    public Member findMemberByEmail(String email){ return memberRepository.findMemberByEmail(email);}
    public Member findMemberByUserId(Long userid){ return memberRepository.findMemberByUserId(userid);}
    public Member findMemberByUserNameAndPhone(String username, String phone) { return memberRepository.findMemberByUserNameAndPhone(username,phone);}
    public int updatePasswordByUserId(String password,Long userid) { return memberRepository.updatePasswordByUserId(password,userid);}
    public int updateMemberByUserId(String username, String email, String password, String nickname, String phone, String address, String issocial, String iscompany, String isjob, String isstack, Long userid) { return memberRepository.updateMemberByUserId(username,email,password,nickname,phone,address,issocial,iscompany,isjob,isstack,userid);}

}
