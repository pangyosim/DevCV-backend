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
@Table(name = "member")
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
    @Column(name = "issocial", nullable = false)
    private SocialType isSocial; // 일반유저, 구글, 카카오

    @Enumerated(EnumType.STRING)
    @Column(name = "userole", nullable = false)
    private RoleType userRole; // 일반유저: user 관리자: admin

    @Enumerated(EnumType.STRING)
    @Column(name = "iscompany", nullable = false)
    private CompanyType isCompany;

    @Enumerated(EnumType.STRING)
    @Column(name = "isjob", nullable = false)
    private JobType isJob;

    @Convert(converter = ListStringConverter.class)
    @Column(name = "isstack", nullable = false)
    private List<String> isStack;

    @Builder
    public Member(Long userId, String userName, String email, String password, String nickName, String phone, String address, Integer userPoint, SocialType isSocial, RoleType userRole, CompanyType isCompany, JobType isJob, List<String> isStack) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.phone = phone;
        this.address = address;
        this.userPoint = userPoint;
        this.isSocial = isSocial;
        this.userRole = userRole;
        this.isCompany = isCompany;
        this.isJob = isJob;
        this.isStack = isStack;
    }
}


