package com.devcv.member.presentation;

import com.devcv.auth.application.AuthService;
import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.InternalServerException;
import com.devcv.common.exception.UnAuthorizedException;
import com.devcv.member.application.MailService;
import com.devcv.member.application.MemberService;
import com.devcv.member.domain.Member;
import com.devcv.member.domain.dto.*;
import com.devcv.member.domain.dto.profile.GoogleProfile;
import com.devcv.member.domain.dto.profile.KakaoProfile;
import com.devcv.member.exception.DuplicationException;
import com.devcv.member.exception.NotNullException;
import com.devcv.member.exception.NotSignUpException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/members")
@PropertySource("classpath:application.yml")
@RequiredArgsConstructor
public class MemberController {
    private final PasswordEncoder passwordEncoder;

    private final MemberService memberService;
    private final MailService mailService;
    private final AuthService authService;

    //----------- login start -----------
    @GetMapping("/login")
    public ResponseEntity<MemberLoginResponse> memberLogin(@RequestBody MemberLoginRequest memberLoginRequest) {
        return ResponseEntity.ok(authService.login(memberLoginRequest));
    }

    //----------- login end -----------

    //----------- signup start -----------
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody MemberSignUpRequest memberSignUpRequest) {
        authService.signup(memberSignUpRequest);
        return ResponseEntity.ok().build();
    }
    //----------- mail start -----------
    @GetMapping("/certemail")
    public ResponseEntity<CertificationMailResponse> certMail(@RequestParam String email) {
        try{
            Long certNumber = mailService.sendMail(email);
            if(certNumber == 0){ // 메일 인증 실패
                throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
            return ResponseEntity.ok().body(new CertificationMailResponse(certNumber));
        } catch (UnsupportedEncodingException ue){
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/duplicationemail")
    public ResponseEntity<?> duplicationMail(@RequestParam String email) {
        try {
            Member findMember = memberService.findMemberByEmail(email);
            if(findMember!= null){
                throw new DuplicationException(ErrorCode.DUPLICATE_ERROR);
            } else {
                return ResponseEntity.ok().build();
            }
        } catch (DuplicationException de){
            de.fillInStackTrace();
            throw new DuplicationException(ErrorCode.DUPLICATE_ERROR);
        }
    }

    //----------- mail end -----------
    //----------- signup end -----------

    //----------- find ID/PW start -----------
    @PostMapping("/findid")
    public ResponseEntity<MemberFindIdReponse> findId(@RequestBody MemberFindIdRequest memberFindIdRequest) {
        // 아이디 찾기
        try{
            Member findIdMember = memberService.findMemberByMemberNameAndPhone(memberFindIdRequest.getMemberName(),memberFindIdRequest.getPhone());
            // 이름&핸드폰번호로 가입되어 있는 멤버가 있는지 확인.
            if(findIdMember!= null){
                return ResponseEntity.ok().body(MemberFindIdReponse.from(findIdMember));
            } else { // 가입되어있지 않다면 Exception 발생.
                throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
            }
        } catch (NotSignUpException e) {
            e.fillInStackTrace();
            throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
        }
    }

    @PostMapping("/findpwemail")
    public ResponseEntity<MemberFindPwResponse> findPwEmail(@RequestBody MemberFindPwEmailRequest memberFindPwEmailRequest) {
        // NULL CHECK
        try{
            if(memberFindPwEmailRequest.getEmail() == null){
                throw new NotNullException(ErrorCode.NULL_ERROR);
            }
        } catch (Exception e){
            throw new NotNullException(ErrorCode.NULL_ERROR);
        }
        // 이메일로 가입되어있는 아이디 찾기
        try {
            Member findpwEmailMember = memberService.findMemberByEmail(memberFindPwEmailRequest.getEmail());
            if(findpwEmailMember != null){
                return ResponseEntity.ok().body(new MemberFindPwResponse(findpwEmailMember.getMemberId()));
            } else {
                throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
            }
        } catch (Exception e){
            e.fillInStackTrace();
            throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
        }
    }
    @PostMapping("/findpwphone")
    public ResponseEntity<MemberFindPwResponse> findPwPhone(@RequestBody MemberFindPwPhoneRequest memberFindPwPhoneRequest) {
        // NULL CHECK
        try{
            if(memberFindPwPhoneRequest.getMemberName() == null || memberFindPwPhoneRequest.getPhone() == null){
                throw new NotNullException(ErrorCode.NULL_ERROR);
            }
        } catch (Exception e){
            throw new NotNullException(ErrorCode.NULL_ERROR);
        }

        // 아이디 찾기
        try{
            Member findIdMember = memberService.findMemberByMemberNameAndPhone(memberFindPwPhoneRequest.getMemberName(),memberFindPwPhoneRequest.getPhone());
            // 이름&핸드폰번호로 가입되어 있는 멤버가 있는지 확인.
            if(findIdMember!= null){
                return ResponseEntity.ok().body(new MemberFindPwResponse(findIdMember.getMemberId()));
            } else { // 가입되어있지 않다면 Exception 발생.
                throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
        }
    }
    //----------- find ID/PW end -----------

    //----------- modi member start -----------
    // 비밀번호 변경 modipw
    @PutMapping("/modipw")
    public ResponseEntity<String> modiPassword(@RequestBody MemberModifyPwRequest memberModifyPwRequest){
        // NULL CHECK
        try {
            if(memberModifyPwRequest.getPassword() == null || memberModifyPwRequest.getMemberId() == null){
                throw new NotNullException(ErrorCode.NULL_ERROR);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new NotNullException(ErrorCode.NULL_ERROR);
        }
        // memberid로 찾은 멤버 패스워드 수정.
        try {
            Member findMemberBymemberid = memberService.findMemberByMemberId(memberModifyPwRequest.getMemberId());
            if(findMemberBymemberid != null){
                int resultUpdatePassword = memberService.updatePasswordByMemberId(passwordEncoder.encode(memberModifyPwRequest.getPassword())
                        ,memberModifyPwRequest.getMemberId());
                if( resultUpdatePassword == 1 ){ // 비밀번호 수정성공.
                  return ResponseEntity.ok().build();
                } else {
                    throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
            } else {
                throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
        }
    }
    // 회원정보수정 modiall
    @PutMapping("/modiall")
    public ResponseEntity<String> modiMember(@RequestBody MemberModiAllRequest memberModiAllRequest) {
        // NULL CHECK
        try {
            if(memberModiAllRequest.getJob() == null || memberModiAllRequest.getAddress() == null || memberModiAllRequest.getStack() == null
                    || memberModiAllRequest.getEmail() == null || memberModiAllRequest.getMemberName() == null || memberModiAllRequest.getSocial() == null
                    || memberModiAllRequest.getCompany() == null || memberModiAllRequest.getPhone() == null || memberModiAllRequest.getMemberId() == null
                    || memberModiAllRequest.getNickName() == null || memberModiAllRequest.getPassword() == null){
                throw new NotNullException(ErrorCode.NULL_ERROR);
            }
        } catch (Exception e){
            e.fillInStackTrace();
            throw new NotNullException(ErrorCode.NULL_ERROR);
        }

        try {
            // memberid로 Member 찾기
            Member findMemberBymemberid = memberService.findMemberByMemberId(memberModiAllRequest.getMemberId());
            if(findMemberBymemberid != null){
                int resultUpdateMember = memberService.updateMemberByMemberId(memberModiAllRequest.getMemberName(), memberModiAllRequest.getEmail(),
                        passwordEncoder.encode(memberModiAllRequest.getPassword()),
                        memberModiAllRequest.getNickName(), memberModiAllRequest.getPhone(), memberModiAllRequest.getAddress(), memberModiAllRequest.getSocial().name(),
                        memberModiAllRequest.getCompany().name(), memberModiAllRequest.getJob().name(), String.join(",", memberModiAllRequest.getStack()), memberModiAllRequest.getMemberId());
                if( resultUpdateMember == 1 ){ // 멤버 수정성공.
                    return ResponseEntity.ok().build();
                } else {
                    throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
            } else {
                throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
        }
    }

    //----------- modi member end -----------


    //----------- Social start -----------
    //----------- KaKao Auth start -----------
    @GetMapping("/auth/kakao")
    public ResponseEntity<Map<String,Object>> memberAuthKakao(@RequestParam String token){

        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplateInfo = new RestTemplate();
        HttpHeaders headersInfo = new HttpHeaders();
        headersInfo.add("Authorization", "Bearer " + token);
        headersInfo.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headersInfo);

        // Http POST && Response
        ResponseEntity<String> responseInfo = restTemplateInfo.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,
                kakaoProfileRequest, String.class
        );

        // KaKaoProfile 객체
        KakaoProfile profile;
        try {
            profile = objectMapper.readValue(responseInfo.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.fillInStackTrace();
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_ERROR);
        }
        // 이미 가입되어 있는 이메일이면 해당 이메일로 로그인
        Member findMember = memberService.findMemberByEmail(profile.getKakao_account().getEmail());
        if(findMember != null){
            HttpHeaders header = new HttpHeaders();
            //header.set("Authorization", "Bearer " + createJwtToken(generateLoginToken(),findMember.getMemberId()));
            return ResponseEntity.ok().headers(header).build();
        } else { // 가입되어있지 않는 이메일이면 회원가입페이지로 이메일 정보 넘김.
            Map<String,Object> userInfo = new HashMap<>(){{
                put("email", profile.getKakao_account().getEmail());
                put("social", 2);
            }};
            return ResponseEntity.ok().body(userInfo);
        }
    }
    //----------- KaKao Auth end -----------

    //----------- Google Auth start -----------
    @GetMapping("/auth/google")
    public ResponseEntity<Map<String,Object>> memberAuthGoogle(@RequestParam String token){

        RestTemplate restTemplateInfo = new RestTemplate();
        HttpHeaders headersInfo = new HttpHeaders();
        headersInfo.add("Authorization", "Bearer " + token);
        headersInfo.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<MultiValueMap<String, String>> googleProfileRequest = new HttpEntity<>(headersInfo);

        // Http POST && Response
        ResponseEntity<String> responseInfo = restTemplateInfo.exchange("https://www.googleapis.com/userinfo/v2/me", HttpMethod.GET,
                googleProfileRequest, String.class
        );
        ObjectMapper objectMapper = new ObjectMapper();
        GoogleProfile googleProfile;
        try {
            googleProfile = objectMapper.readValue(responseInfo.getBody(), GoogleProfile.class);
        } catch (JsonProcessingException e) {
            e.fillInStackTrace();
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        // 이미 가입되어 있는 이메일이면 해당 이메일로 로그인
        Member findMember = memberService.findMemberByEmail(googleProfile.getEmail());
        if(findMember != null){
            HttpHeaders header = new HttpHeaders();
            //header.set("Authorization", "Bearer " + createJwtToken(generateLoginToken(),findMember.getMemberId()));
            return ResponseEntity.ok().headers(header).build();
        } else { // 가입되어있지 않는 이메일이면 회원가입페이지로 이메일 정보 넘김.
            Map<String,Object> userInfo = new HashMap<>(){{
                put("email",googleProfile.getEmail());
                put("social",1);
            }};
            return ResponseEntity.ok().body(userInfo);
        }
    }
    //----------- Google Auth end -----------
    //----------- Social end -----------




}
