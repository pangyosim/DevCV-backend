package com.devcv.auth.config;

import com.devcv.auth.filter.JwtAccessDeniedHandler;
import com.devcv.auth.filter.JwtAuthenticationEntryPoint;
import com.devcv.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {
    private final JwtProvider jwtProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.csrf().disable()
                // CORS 설정
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                // exception handling
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // JWT 토큰으로 session disable
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 permitall
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/members/login","/members/signup","/members/findid","/members/certemail"
                        ,"/members/findpwphone","/members/findpwemail","/members/modipw","/members/duplicationemail",
                        "/auth/kakao","/auth/google", "/resumes","/resumes/{resumeId}", "/resumes/{resumeId}/reviews").permitAll()
                .anyRequest().authenticated()   // 이외 인증필요 -> Header에 "Bearer {accessToken}" 형태로 요청

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .apply(new JwtSecurityConfig(jwtProvider));

        return http.build();
    }

}
