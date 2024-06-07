package com.devcv.member.domain;

import com.devcv.common.domain.BaseTimeEntity;
import com.devcv.member.domain.dto.CompanyType;
import com.devcv.member.domain.dto.JobType;
import com.devcv.member.domain.dto.RoleType;
import com.devcv.member.domain.dto.SocialType;
import com.devcv.member.infrastructure.ListStringConverter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "members")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long userId;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "userpoint", nullable = false)
    private Integer userPoint;

    @Enumerated(EnumType.STRING)
    @Column(name = "social", nullable = false)
    private SocialType social; // 일반유저, 구글, 카카오

    @Enumerated(EnumType.STRING)
    @Column(name = "userrole", nullable = false)
    private RoleType userRole; // 일반유저: user 관리자: admin

    @Enumerated(EnumType.STRING)
    @Column(name = "company", nullable = false)
    private CompanyType company;

    @Enumerated(EnumType.STRING)
    @Column(name = "job", nullable = false)
    private JobType job;

    @Convert(converter = ListStringConverter.class)
    @Column(name = "stack", nullable = false)
    private List<String> stack;

    @Builder
    public Member(Long userId, String userName, String email, String password, String nickName, String phone, String address, Integer userPoint, SocialType social, RoleType userRole, CompanyType company, JobType job, List<String> stack) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.phone = phone;
        this.address = address;
        this.userPoint = userPoint;
        this.social = social;
        this.userRole = userRole;
        this.company = company;
        this.job = job;
        this.stack = stack;
    }
}


