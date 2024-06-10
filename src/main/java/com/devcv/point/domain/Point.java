package com.devcv.point.domain;

import com.devcv.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_userid")
    private Member member;

    @Column
    private Long amount;

    @Column
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;

    @Column
    private String description;

    @Column
    private LocalDateTime transactionDate;

    private Point(Member member, Long amount, TransactionType transactionType, String description) {
        this.id = null;
        this.member = member;
        this.amount = amount;
        this.transactionType = transactionType;
        this.description = description;
        this.transactionDate = LocalDateTime.now();
    }

    public static Point save(Member member, Long amount, String description) {
        return new Point(member, amount, TransactionType.SAVE, description);
    }

    public static Point use(Member member, Long amount, String description) {
        Long negativeAmount = -amount;
        return new Point(member, negativeAmount, TransactionType.USE, description);
    }
}