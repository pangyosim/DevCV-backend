package com.devcv.auth.application;

import com.devcv.auth.jwt.JwtProvider;
import com.devcv.auth.jwt.JwtTokenDto;
import com.devcv.auth.jwt.RefreshToken;
import com.devcv.common.exception.ErrorCode;
import com.devcv.member.domain.Member;
import com.devcv.member.domain.dto.MemberLoginRequest;
import com.devcv.member.domain.dto.MemberLoginResponse;
import com.devcv.member.domain.dto.MemberSignUpRequest;
import com.devcv.member.domain.enumtype.RoleType;
import com.devcv.member.exception.AuthLoginException;
import com.devcv.member.exception.DuplicationException;
import com.devcv.member.exception.NotNullException;
import com.devcv.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    @Transactional
    public void signup(MemberSignUpRequest memberSignUpRequest) {
        if (memberRepository.findMemberByEmail(memberSignUpRequest.getEmail()) != null) {
            throw new DuplicationException(ErrorCode.DUPLICATE_ERROR);
        }
        Member member = memberSignUpRequest.toMember(passwordEncoder);
        // 회원가입시 관리자 형태가 아닌 일반 권한으로 가입.
        Member refreshMember = member.toBuilder().memberRole(RoleType.일반).build();
        try{
            if(refreshMember.getMemberName() == null || refreshMember.getNickName() == null || refreshMember.getEmail() == null
                || refreshMember.getPassword() == null || refreshMember.getPhone() == null || refreshMember.getAddress() == null
                || refreshMember.getSocial() == null || refreshMember.getJob() == null || refreshMember.getCompany() == null || refreshMember.getStack() == null){
                throw new NotNullException(ErrorCode.NULL_ERROR);
            } else {
                memberRepository.save(refreshMember);
            }
        } catch (NotNullException e) {
            throw new NotNullException(ErrorCode.NULL_ERROR);
        }
    }
    @Transactional
    public MemberLoginResponse login(MemberLoginRequest memberLoginRequest) {

        // 1. Login ID/PW 로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberLoginRequest.toAuthentication();
        // 2. 검증 (비밀번호 체크)
        // authenticate -> MemberDetailsService (loadUserByUsername) 실행
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            JwtTokenDto tokenDto = jwtProvider.generateTokenDto(authentication);
            // 4. RefreshToken 저장
            RefreshToken.builder()
                    .key(authentication.getName())
                    .value(tokenDto.getRefreshToken())
                    .build();
            // 5. 토큰 발급
            return MemberLoginResponse.from(tokenDto, authentication);
        } catch (BadCredentialsException e){
            e.fillInStackTrace();
            throw new AuthLoginException(ErrorCode.LOGIN_ERROR);
        }
    }
}
