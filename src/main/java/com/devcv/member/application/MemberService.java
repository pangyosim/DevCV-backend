package com.devcv.member.application;

import com.devcv.member.domain.Member;
import com.devcv.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberrepo) {
        this.memberRepository = memberrepo;
    }

    public void signup(Member member){
        memberRepository.save(member);
    };
    public Member findMemberByEmail(String email){ return memberRepository.findMemberByEmail(email);}
    public Member findMemberByUserId(Long userid){ return memberRepository.findMemberByUserId(userid);}
    public Member findMemberByUserNameAndPhone(String username, String phone) { return memberRepository.findMemberByUserNameAndPhone(username,phone);}

    public int updatePasswordByUserId(String password,Long userid) {return memberRepository.updatePasswordByUserId(password,userid);}
}
