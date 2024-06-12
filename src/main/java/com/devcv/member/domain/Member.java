package com.devcv.member.domain;

import com.devcv.common.domain.BaseTimeEntity;
import com.devcv.member.domain.enumtype.CompanyType;
import com.devcv.member.domain.enumtype.JobType;
import com.devcv.member.domain.enumtype.RoleType;
import com.devcv.member.domain.enumtype.SocialType;
import com.devcv.member.infrastructure.ListStringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "tb_member")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberid")
    private Long memberId;

    @Column(name = "membername", nullable = false)
    private String memberName;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "social", nullable = false)
    private SocialType social;

    @Enumerated(EnumType.STRING)
    @Column(name = "memberrole", nullable = false)
    private RoleType memberRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "company", nullable = false)
    private CompanyType company;

    @Enumerated(EnumType.STRING)
    @Column(name = "job", nullable = false)
    private JobType job;

    @Convert(converter = ListStringConverter.class)
    @Column(name = "stack", nullable = false)
    private List<String> stack;

}


