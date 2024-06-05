package com.devcv.member.presentation;

import com.devcv.common.exception.*;
import com.devcv.member.application.MailService;
import com.devcv.member.application.MemberService;
import com.devcv.member.domain.Member;
import com.devcv.member.domain.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/members")
@PropertySource("classpath:application.yml")
public class MemberController {

    //----------- JWT Secret Key -----------
    @Value("${keys.jwtkey}")
    private String SECRET_KEY;
    //----------- JWT Secret Key -----------

    //----------- Social Key -----------
    @Value("${keys.kakao_clientid}")
    private String KAKAO_CLIENT_ID;
    @Value("${keys.kakao_clientpw}")
    private String KAKAO_CLIENT_PW;
    @Value("${keys.google_client_id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${keys.google_client_secret}")
    private String GOOGLE_CLIENT_SECRET;
    //----------- Social Key -----------

    private final MemberService memberService;
    private final MailService mailService;
    @Autowired
    public MemberController(MemberService memberService, MailService mailService) {
        this.memberService = memberService;
        this.mailService = mailService;
    }

    //----------- login start -----------
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> memberLogin(@RequestBody Member member) {

            // 가입힌 아이디인지 체크
            try{
                if(memberService.findMemberByEmail(member.getEmail()) == null){
                    throw new NotSignUpException(ErrorCode.LOGIN_ID_ERROR);
                }
            } catch (Exception e) {
                e.fillInStackTrace();
                throw new NotSignUpException(ErrorCode.LOGIN_ID_ERROR);
            }
            // 아이디 & 비밀번호 체크
            try {
                Member loginMember = memberService.findMemberByEmail(member.getEmail());
                // 일반회원 login 부분
                if(loginMember != null && member.getEmail().equals(loginMember.getEmail()) && member.getPassword().equals(parseJwtToken(loginMember.getPassword()).get("pw").toString())){
                    // Header에 로그인 성공부분 jwt 토큰화한 값 FE에 전달 Bearer + key값
                    HttpHeaders header = new HttpHeaders();
                    header.set("Authorization", "Bearer " + makeJwtToken(generateLoginToken()));
                    Map<String,Object> responseLogin = new HashMap<>(){{
                        put("isSocial", loginMember.getIsSocial());
                        put("userId", loginMember.getUserId());
                    }};
                    return ResponseEntity.ok().headers(header).body(responseLogin); // 아이디, 소셜로그인여부 값 반환.
                } else {
                    throw new AuthLoginException(ErrorCode.LOGIN_ERROR);
                }
            } catch (Exception e){
                e.fillInStackTrace();
                throw new AuthLoginException(ErrorCode.LOGIN_ERROR);
            }
    }
    //----------- token start -----------
    public String generateLoginToken(){
        byte[] array = new byte[20];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
    //----------- token end -----------
    //----------- login end -----------

    //----------- signup start -----------
    @PostMapping("/signup")
    public ResponseEntity<String> memberSignup(@RequestBody Member member) {
        // NULL CHECK
        nullCheckMemberAllProperties(member);
        // 가입된 아이디 확인 if문 ** 회원가입 진행시 email로 확인.
        try{
            if( memberService.findMemberByEmail(member.getEmail()) != null ){
                throw new DuplicationException(ErrorCode.DUPLICATE_ERROR);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new DuplicationException(ErrorCode.DUPLICATE_ERROR);
        }

        // 회원가입 로직
        try {
            // SOCIAL LOGIN CHECK
            // 일반로그인
            if (member.getIsSocial().toString().equals(SocialType.일반.name())) {
                // 만료시간 없는 jwt토큰 발행 후 DB PW에 저장.
                Member normalMember = Member.builder()
                        .userName(member.getUserName())
                        .userPoint(member.getUserPoint())
                        .userRole(member.getUserRole())
                        .email(member.getEmail())
                        .isJob(member.getIsJob())
                        .phone(member.getPhone())
                        .address(member.getAddress())
                        .nickName(member.getNickName())
                        .isCompany(member.getIsCompany())
                        .isStack(member.getIsStack())
                        .isSocial(member.getIsSocial())
                        .password(Jwts.builder()
                                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                                .setIssuer("devcv")
                                .claim("pw", member.getPassword())
                                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                                .compact())
                        .build();
                memberService.signup(normalMember);
            } else { // 소셜로그인
                Member socialMember = Member.builder()
                        .userName(member.getUserName())
                        .userRole(member.getUserRole())
                        .userPoint(member.getUserPoint())
                        .email(member.getEmail())
                        .isJob(member.getIsJob())
                        .phone(member.getPhone())
                        .address(member.getAddress())
                        .nickName(member.getNickName())
                        .isCompany(member.getIsCompany())
                        .isStack(member.getIsStack())
                        .isSocial(member.getIsSocial())
                        .password(Jwts.builder()
                                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                                .setIssuer("devcv")
                                .claim("pw", KAKAO_CLIENT_PW)
                                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                                .compact())
                        .build();
                memberService.signup(socialMember);
            }
            return ResponseEntity.ok().build();
        } catch (NotNullException e) {
            e.fillInStackTrace();
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    //----------- mail start -----------
    @GetMapping("/certemail")
    public int certMail(@RequestParam String email) throws UnsupportedEncodingException {return mailService.sendMail(email);}
    //----------- mail end -----------
    //----------- signup end -----------

    //----------- find ID/PW start -----------
    @PostMapping("/findid")
    public ResponseEntity<Map<String,String>> findId(@RequestBody Member member) {
        // NULL CHECK
        try{
            if(member.getUserName() == null || member.getPhone() == null){
                throw new NotNullException(ErrorCode.NULL_ERROR);
            }
        } catch (Exception e){
            throw new NotNullException(ErrorCode.NULL_ERROR);
        }

        // 아이디 찾기
        try{
            Member findIdMember = memberService.findMemberByUserNameAndPhone(member.getUserName(),member.getPhone());
            // 이름&핸드폰번호로 가입되어 있는 멤버가 있는지 확인.
            if(findIdMember!= null){
                Map<String,String> responseMap = new HashMap<>(){{
                   put("email",findIdMember.getEmail());
                }};
                return ResponseEntity.ok(responseMap);
            } else { // 가입되어있지 않다면 Exception 발생.
                throw new NotSignUpException(ErrorCode.LOGIN_ID_ERROR);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new NotSignUpException(ErrorCode.LOGIN_ID_ERROR);
        }
    }

    @PostMapping("/findpwemail")
    public ResponseEntity<String> findPwEmail(@RequestBody Member member) {
        // NULL CHECK
        try{
            if(member.getEmail() == null){
                throw new NotNullException(ErrorCode.NULL_ERROR);
            }
        } catch (Exception e){
            throw new NotNullException(ErrorCode.NULL_ERROR);
        }
        // 이메일로 가입되어있는 아이디 찾기
        try {
            Member findpwEmailMember = memberService.findMemberByEmail(member.getEmail());
            if(findpwEmailMember != null){
                return ResponseEntity.ok().build();
            }
        } catch (Exception e){
            throw new NotSignUpException(ErrorCode.LOGIN_ID_ERROR);
        }
        return null;
    }
    @PostMapping("/findpwphone")
    public ResponseEntity<String> findPwPhone(@RequestBody Member member) {
        // NULL CHECK
        try{
            if(member.getUserName() == null || member.getPhone() == null){
                throw new NotNullException(ErrorCode.NULL_ERROR);
            }
        } catch (Exception e){
            throw new NotNullException(ErrorCode.NULL_ERROR);
        }

        // 아이디 찾기
        try{
            Member findIdMember = memberService.findMemberByUserNameAndPhone(member.getUserName(),member.getPhone());
            // 이름&핸드폰번호로 가입되어 있는 멤버가 있는지 확인.
            if(findIdMember!= null){
                return ResponseEntity.ok().build();
            } else { // 가입되어있지 않다면 Exception 발생.
                throw new NotSignUpException(ErrorCode.LOGIN_ID_ERROR);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new NotSignUpException(ErrorCode.LOGIN_ID_ERROR);
        }
    }
    //----------- find ID/PW end -----------

    //----------- modi member start -----------
    @PostMapping("/modipw")
    public ResponseEntity<String> modiPassword(@RequestBody Member member){
        // NULL CHECK
        try {
            if(member.getPassword() == null || member.getUserId() == null){
                throw new NotNullException(ErrorCode.NULL_ERROR);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new NotNullException(ErrorCode.NULL_ERROR);
        }
        // userId로 찾은 멤버 패스워드 수정.
        try {
            Member findMemberByuserId = memberService.findMemberByUserId(member.getUserId());
            if(findMemberByuserId != null){
                int resultUpdatePassword = memberService.updatePasswordByUserId(Jwts.builder()
                        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                        .setIssuer("devcv")
                        .claim("pw", member.getPassword())
                        .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8))) // JWT암호화
                        .compact(), member.getUserId());
                if( resultUpdatePassword == 1 ){ // 비밀번호 수정성공.
                  return ResponseEntity.ok().build();
                } else {
                    throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
            } else {
                throw new NotSignUpException(ErrorCode.LOGIN_ID_ERROR);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new NotSignUpException(ErrorCode.LOGIN_ID_ERROR);
        }
    }

    //*************** 작업중 ***************
    @PostMapping("/modi")
    public ResponseEntity<?> modiMember(@RequestBody Member member) {
        // NULL CHECK
        nullCheckMemberAllProperties(member);

        return null;
    }
    //*************** 작업중 ***************

    // NULL CHECK Exception Handling
    private void nullCheckMemberAllProperties(@RequestBody Member member) {
        try {
            if(member.getIsJob() == null || member.getAddress() == null || member.getIsStack() == null
                    || member.getEmail() == null || member.getUserRole() == null || member.getUserName() == null
                    || member.getIsSocial() == null || member.getIsCompany() == null || member.getPhone() == null
                    || member.getUserPoint() == null || member.getNickName() == null || member.getPassword() == null){
                throw new NotNullException(ErrorCode.NULL_ERROR);
            }
        } catch (Exception e){
            e.fillInStackTrace();
            throw new NotNullException(ErrorCode.NULL_ERROR);
        }
    }

    //----------- modi member end -----------
    
    // ----------- JWT start -----------
    // 암호화 부분 SHA-256
    // 만료시간 30분 토큰
    private String makeJwtToken(String str) {
        Date now = new Date();
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("devcv")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis()))
                .claim("str", str)
                .signWith(key)
                .compact();
    }
    // 복호화 부분
    private Claims parseJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    //----------- JWT end -----------


    //----------- Social start -----------
    //----------- KaKao Auth start -----------
    @GetMapping("/auth/kakao")
    public ResponseEntity<Map<String,Object>> memberAuthKakao(@RequestParam String code){
        // HttpsURLConnection ResTemplate
        RestTemplate restTemplateToken = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = getMultiValueMapHttpEntity(code);

        // Http POST && Response
        ResponseEntity<String> response = restTemplateToken.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoOAuthToken oauthToken;
        try {
            oauthToken = objectMapper.readValue(response.getBody(), KakaoOAuthToken.class);
        } catch (JsonProcessingException e) { // Token 발급 실패 부분 = 카카오 로그인 실패
            e.fillInStackTrace();
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        RestTemplate restTemplateInfo = new RestTemplate();

        HttpHeaders headersInfo = new HttpHeaders();
        headersInfo.add("Authorization", "Bearer " + oauthToken.getAccess_token());
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
        if(memberService.findMemberByEmail(profile.getKakao_account().getEmail()) != null){
            HttpHeaders header = new HttpHeaders();
            header.set("Authorization", "Bearer " + makeJwtToken(generateLoginToken()));
            return ResponseEntity.ok().headers(header).build();
        } else { // 가입되어있지 않는 이메일이면 회원가입페이지로 이메일 정보 넘김.
            Map<String,Object> userInfo = new HashMap<>(){{
                put("email", profile.getKakao_account().getEmail());
                put("isSocial", 2);
            }};
            return ResponseEntity.ok().body(userInfo);
        }
    }

    private HttpEntity<MultiValueMap<String, String>> getMultiValueMapHttpEntity(String code) {
        HttpHeaders headersToken = new HttpHeaders();
        headersToken.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("redirect_uri", "http://localhost:8080/members/auth/kakao");
        params.add("code", code);

        return new HttpEntity<>(params, headersToken);
    }

    //----------- KaKao Auth end -----------

    //----------- Google Auth start -----------
    @GetMapping("/auth/google")
    public ResponseEntity<Map<String,Object>> memberAuthGoogle(@RequestParam String code){
        // HttpsURLConnection ResTemplate
        RestTemplate restTemplateToken = new RestTemplate();
        Map<String,String> params = new HashMap<>();

        params.put("code",code);
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("client_secret", GOOGLE_CLIENT_SECRET);
        params.put("redirect_uri", "http://localhost:8080/members/auth/google");
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseToken = restTemplateToken.postForEntity("https://oauth2.googleapis.com/token",params,String.class);

        ObjectMapper objectMapperToken = new ObjectMapper();
        GoogleOAuthToken oauthToken;
        try {
            oauthToken = objectMapperToken.readValue(responseToken.getBody(), GoogleOAuthToken.class);
        } catch (JsonProcessingException e) { // Token 발급 실패 부분 = 구글 로그인 실패
            e.fillInStackTrace();
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_ERROR);
        }
        RestTemplate restTemplateInfo = new RestTemplate();
        HttpHeaders headersInfo = new HttpHeaders();
        headersInfo.add("Authorization", "Bearer " + oauthToken.getAccess_token());
        headersInfo.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<MultiValueMap<String, String>> googleProfileRequest = new HttpEntity<>(headersInfo);

        // Http POST && Response
        ResponseEntity<String> responseInfo = restTemplateInfo.exchange("https://www.googleapis.com/userinfo/v2/me", HttpMethod.GET,
                googleProfileRequest, String.class
        );
        System.out.println(responseInfo);
        ObjectMapper objectMapper = new ObjectMapper();
        GoogleProfile googleProfile;
        try {
            googleProfile = objectMapper.readValue(responseInfo.getBody(), GoogleProfile.class);
            System.out.println(googleProfile);
        } catch (JsonProcessingException e) {
            e.fillInStackTrace();
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        // 이미 가입되어 있는 이메일이면 해당 이메일로 로그인
        if(memberService.findMemberByEmail(googleProfile.getEmail()) != null){
            HttpHeaders header = new HttpHeaders();
            header.set("Authorization", "Bearer " + makeJwtToken(generateLoginToken()));
            return ResponseEntity.ok().headers(header).build();
        } else { // 가입되어있지 않는 이메일이면 회원가입페이지로 이메일 정보 넘김.
            Map<String,Object> userInfo = new HashMap<>(){{
                put("email",googleProfile.getEmail());
                put("isSocial",1);
            }};
            return ResponseEntity.ok().body(userInfo);
        }
    }
    //----------- Google Auth end -----------
    //----------- Social end -----------




}
