package com.devcv.member.presentation;

import com.devcv.auth.application.AuthService;
import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.InternalServerException;
import com.devcv.common.exception.UnAuthorizedException;
import com.devcv.member.application.MailService;
import com.devcv.member.application.MemberService;
import com.devcv.member.domain.Member;
import com.devcv.member.domain.MemberLog;
import com.devcv.member.domain.dto.*;
import com.devcv.member.domain.dto.profile.GoogleProfile;
import com.devcv.member.domain.dto.profile.KakaoProfile;
import com.devcv.member.domain.enumtype.SocialType;
import com.devcv.member.exception.*;
import com.devcv.member.repository.MemberLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/members/*")
@PropertySource("classpath:application.yml")
@RequiredArgsConstructor
public class MemberController {
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final MailService mailService;
    private final AuthService authService;
    private final MemberLogRepository memberLogRepository;
    @Value("${keys.social_password}")
    private String socialPassword;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_REFRESH_HEADER = "RefreshToken";
    public static final String BEARER_PREFIX = "Bearer ";

    //----------- login start -----------
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> memberLogin(@RequestBody MemberLoginRequest memberLoginRequest) {
        MemberLoginResponse resultResponse = authService.login(memberLoginRequest);
        HttpHeaders header = new HttpHeaders();
        header.add(AUTHORIZATION_HEADER,BEARER_PREFIX + resultResponse.getAccessToken());
        header.add(AUTHORIZATION_REFRESH_HEADER,BEARER_PREFIX+ resultResponse.getRefreshToken());
        return ResponseEntity.ok().headers(header).body(resultResponse);
    }
    //----------- login end -----------

    //----------- signup start -----------
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody MemberSignUpRequest memberSignUpRequest) {
        authService.signup(memberSignUpRequest);
        return ResponseEntity.ok().build();
    }
    //----------- mail start -----------
    @GetMapping("/cert-email")
    public ResponseEntity<CertificationMailResponse> certEmail(@RequestParam String email) {
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

    @GetMapping("/duplication-email")
    public ResponseEntity<Object> duplicationEmail(@RequestParam String email) {
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
    @PostMapping("/find-id")
    public ResponseEntity<MemberFindIdReponse> findId(@RequestBody MemberFindIdRequest memberFindIdRequest) {
        // 아이디 찾기
        try{
            Member findIdMember = memberService.findMemberBymemberNameAndPhone(memberFindIdRequest.getMemberName(),memberFindIdRequest.getPhone());
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

    @PostMapping("/find-pw/email")
    public ResponseEntity<MemberFindPwResponse> findPwEmail(@RequestParam String email) {
        // NULL CHECK
        try{
            if(email == null){
                throw new NotNullException(ErrorCode.NULL_ERROR);
            }
        } catch (Exception e){
            throw new NotNullException(ErrorCode.NULL_ERROR);
        }
        // 이메일로 가입되어있는 아이디 찾기
        try {
            Member findpwEmailMember = memberService.findMemberByEmail(email);
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
    @PostMapping("/find-pw")
    public ResponseEntity<MemberFindPwResponse> findPwPhone(@RequestBody MemberFindPwPhoneRequest memberFindPwPhoneRequest) {
        // NULL CHECK
        try{
            if(memberFindPwPhoneRequest.getMemberName() == null || memberFindPwPhoneRequest.getPhone() == null){
                throw new NotNullException(ErrorCode.NULL_ERROR);
            }
        } catch (NotNullException e){
            throw new NotNullException(ErrorCode.NULL_ERROR);
        }

        // 아이디 찾기
        try{
            Member findIdMember = memberService.findMemberBymemberNameAndPhone(memberFindPwPhoneRequest.getMemberName(),memberFindPwPhoneRequest.getPhone());
            // 이름&핸드폰번호로 가입되어 있는 멤버가 있는지 확인.
            if(findIdMember!= null){
                return ResponseEntity.ok().body(new MemberFindPwResponse(findIdMember.getMemberId()));
            } else { // 가입되어있지 않다면 Exception 발생.
                throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
            }
        } catch (NotSignUpException e) {
            e.fillInStackTrace();
            throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
        }
    }
    //----------- find ID/PW end -----------

    //----------- modi member start -----------
    // 비밀번호 변경 modipw
    @PutMapping("/{member-id}/{password}")
    public ResponseEntity<String> modiPassword(@PathVariable("member-id") Long memberId, @PathVariable String password){
        // NULL CHECK
        try {
            if(password == null || memberId == null){
                throw new NotNullException(ErrorCode.NULL_ERROR);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new NotNullException(ErrorCode.NULL_ERROR);
        }
        // memberId로 찾은 멤버 패스워드 수정.
        try {
            Member findMemberBymemberId = memberService.findMemberBymemberId(memberId);
            if(findMemberBymemberId != null){
                if(!findMemberBymemberId.getSocial().name().equals(SocialType.normal.name())){
                    throw new SocialMemberUpdateException(ErrorCode.SOCIAL_UPDATE_ERROR);
                }
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
                int resultUpdatePassword = memberService.updatePasswordBymemberId(passwordEncoder.encode(password)
                        ,memberId);
                memberLogRepository.save(MemberLog.builder().memberId(findMemberBymemberId.getMemberId()).logIp(getIp(request))
                        .logEmail(findMemberBymemberId.getEmail()).logAgent(request.getHeader("user-agent")).logUpdateDate(LocalDateTime.now()).build());
                if( resultUpdatePassword == 1 ){ // 비밀번호 수정성공.
                  return ResponseEntity.ok().build();
                } else {
                    throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
            } else {
                throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
            }
        } catch (NotSignUpException e) {
            e.fillInStackTrace();
            throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
        } catch (SocialMemberUpdateException e){
            e.fillInStackTrace();
            throw new SocialMemberUpdateException(ErrorCode.SOCIAL_UPDATE_ERROR);
        }
    }
    // 회원정보 단건 조회/수정
    @PostMapping("{member-id}")
    public ResponseEntity<MemberMypageResponse> getMember(@PathVariable("member-id") Long memberId) {
        try {
            return ResponseEntity.ok().body(MemberMypageResponse.from(memberService.findMemberBymemberId(memberId)));
        } catch (NotSignUpException e){
            e.fillInStackTrace();
            throw new NotSignUpException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }
    @PutMapping("/{member-id}")
    public ResponseEntity<String> modiMember(@RequestBody MemberModiAllRequest memberModiAllRequest, @PathVariable("member-id") Long memberId) {
        // NULL CHECK
        try {
            if(memberModiAllRequest.getJob() == null || memberModiAllRequest.getAddress() == null || memberModiAllRequest.getStack() == null
                    || memberModiAllRequest.getEmail() == null || memberModiAllRequest.getMemberName() == null || memberModiAllRequest.getSocial() == null
                    || memberModiAllRequest.getCompany() == null || memberModiAllRequest.getPhone() == null || memberId == null
                    || memberModiAllRequest.getNickName() == null || memberModiAllRequest.getPassword() == null){
                throw new NotNullException(ErrorCode.NULL_ERROR);
            }
        } catch (NotNullException e){
            e.fillInStackTrace();
            throw new NotNullException(ErrorCode.NULL_ERROR);
        }
        try {
            // memberId로 Member 찾기
            Member findMemberBymemberId = memberService.findMemberBymemberId(memberId);
            if(findMemberBymemberId != null){
                if(!findMemberBymemberId.getSocial().name().equals(memberModiAllRequest.getSocial().name())){
                    throw new SocialDataException(ErrorCode.SOCIAL_ERROR);
                }
                // 수정할 이메일이 이미 존재할 떄 (가입한 아이디 제외)
                Member findMemberBymemberEmail = memberService.findMemberByEmail(memberModiAllRequest.getEmail());
                if(findMemberBymemberEmail != null && !findMemberBymemberId.getEmail().equals(findMemberBymemberEmail.getEmail())){
                    throw new DuplicationException(ErrorCode.DUPLICATE_ERROR);
                }
                if( memberModiAllRequest.getSocial().name().equals(SocialType.normal.name())) {
                    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
                    int resultUpdateMember = memberService.updateMemberBymemberId(memberModiAllRequest.getMemberName(), memberModiAllRequest.getEmail(),
                            passwordEncoder.encode(memberModiAllRequest.getSocial().name().equals(SocialType.normal.name()) ? memberModiAllRequest.getPassword() : socialPassword),
                            memberModiAllRequest.getNickName(), memberModiAllRequest.getPhone(), memberModiAllRequest.getAddress(),
                            memberModiAllRequest.getCompany().name(), memberModiAllRequest.getJob().name(),
                            String.join(",", memberModiAllRequest.getStack()), memberId);
                    memberLogRepository.save(MemberLog.builder().memberId(findMemberBymemberId.getMemberId()).logIp(getIp(request))
                            .logEmail(findMemberBymemberId.getEmail()).logAgent(request.getHeader("user-agent")).logUpdateDate(LocalDateTime.now()).build());
                    if (resultUpdateMember == 1) { // 일반 멤버 수정성공.
                        return ResponseEntity.ok().build();
                    } else {
                        throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
                    int resultUpdateSocialMember = memberService.updateSocialMemberBymemberId(memberModiAllRequest.getMemberName(),
                            memberModiAllRequest.getNickName(), memberModiAllRequest.getPhone(), memberModiAllRequest.getAddress(),
                            memberModiAllRequest.getCompany().name(), memberModiAllRequest.getJob().name(),
                            String.join(",", memberModiAllRequest.getStack()), memberId);
                    memberLogRepository.save(MemberLog.builder().memberId(findMemberBymemberId.getMemberId()).logIp(getIp(request))
                            .logEmail(findMemberBymemberId.getEmail()).logAgent(request.getHeader("user-agent")).logUpdateDate(LocalDateTime.now()).build());
                    if (resultUpdateSocialMember == 1) { // 소셜 멤버 수정성공.
                        return ResponseEntity.ok().build();
                    } else {
                        throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
                    }
                }
            } else {
                throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
            }
        } catch (NotSignUpException e) {
            e.fillInStackTrace();
            throw new NotSignUpException(ErrorCode.FIND_ID_ERROR);
        } catch (InternalServerException e){
            e.fillInStackTrace();
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (SocialDataException e) {
            e.fillInStackTrace();
            throw new SocialDataException(ErrorCode.SOCIAL_ERROR);
        } catch (DuplicationException e){
            e.fillInStackTrace();
            throw new DuplicationException(ErrorCode.DUPLICATE_ERROR);
        }
    }
    //----------- modi member end -----------


    //----------- Social start -----------
    //----------- KaKao Auth start -----------
    @GetMapping("/kakao-login")
    public ResponseEntity<Object> memberAuthKakao(@RequestParam String token){
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
            // 해당 소셜 이메일이 이미 일반계정으로 가입되어 있는 경우
            if(!passwordEncoder.matches(socialPassword, findMember.getPassword())){
                throw new SocialLoginException(ErrorCode.SOCIAL_LOGIN_ERROR);
            }
            // 로그인 진행
            MemberLoginRequest memberLoginRequest =MemberLoginRequest.builder().email(profile.getKakao_account().getEmail()).password(socialPassword).build();
            MemberLoginResponse memberLoginResponse = authService.login(memberLoginRequest);
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization","Bearer " + memberLoginResponse.getAccessToken());
            return ResponseEntity.ok().headers(header).body(memberLoginResponse);
        } else { // 가입되어있지 않는 이메일이면 회원가입페이지로 이메일 정보 넘김.
            Map<String,Object> userInfo = new HashMap<>(){{
                put("email", profile.getKakao_account().getEmail());
                put("social", SocialType.kakao.name());
            }};
            return ResponseEntity.ok().body(userInfo);
        }
    }
    //----------- KaKao Auth end -----------

    //----------- Google Auth start -----------
    @GetMapping("/google-login")
    public ResponseEntity<Object> memberAuthGoogle(@RequestParam String token){

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
            // 해당 소셜 이메일이 이미 일반계정으로 가입되어 있는 경우
            if(!passwordEncoder.matches(socialPassword, findMember.getPassword())){
                throw new SocialLoginException(ErrorCode.SOCIAL_LOGIN_ERROR);
            }
            // 로그인 진행.
            MemberLoginRequest memberLoginRequest = MemberLoginRequest.builder().email(googleProfile.getEmail()).password(socialPassword).build();
            MemberLoginResponse memberLoginResponse = authService.login(memberLoginRequest);
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization","Bearer " + memberLoginResponse.getAccessToken());
            return ResponseEntity.ok().headers(header).body(memberLoginResponse);
        } else { // 가입되어있지 않는 이메일이면 회원가입페이지로 이메일 정보 넘김.
            Map<String,Object> userInfo = new HashMap<>(){{
                put("email",googleProfile.getEmail());
                put("social",SocialType.google.name());
            }};
            return ResponseEntity.ok().body(userInfo);
        }
    }
    //----------- Google Auth end -----------
    //----------- Social end -----------

    //----------- GetClientIP start -----------
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    //----------- GetClientIP end -----------

}
