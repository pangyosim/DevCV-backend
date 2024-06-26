package com.devcv.resume.domain;


import com.devcv.resume.domain.enumtype.ResumeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_resumelog")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeLog{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resumeLogId;

    private Long resumeId;

    private String title;

    @Enumerated(EnumType.STRING)
    private ResumeStatus status;

    private LocalDateTime logCreatedDate;

    @PrePersist
    public void prePersist() {
        this.logCreatedDate = LocalDateTime.now();
    }
}