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
    private Long memberId;
    @Column(name = "logemail",nullable = false)
    private String logEmail;
    @Column(name = "logsignupdate")
    private LocalDateTime logSignUpDate;
    @Column(name = "logupdatedate")
    private LocalDateTime logUpdateDate;
    @Column(name = "loglogindate")
    private LocalDateTime logLoginDate;
    @Column(name = "logip",nullable = false)
    private String logIP;
    @Column(name = "logagent",nullable = false)
    private String logAgent;
}
