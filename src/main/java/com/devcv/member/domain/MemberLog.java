package com.devcv.member.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_memberlog")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;
    @Column(name = "memberId",nullable = false)
    private Long memberId;
    @Column(name = "logEmail",nullable = false)
    private String logEmail;
    @Column(name = "logSignUpDate")
    private LocalDateTime logSignUpDate;
    @Column(name = "logUpdateDate")
    private LocalDateTime logUpdateDate;
    @Column(name = "logLoginDate")
    private LocalDateTime logLoginDate;
    @Column(name = "logIp",nullable = false)
    private String logIp;
    @Column(name = "logAgent",nullable = false)
    private String logAgent;
}
