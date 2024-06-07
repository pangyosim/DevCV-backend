package com.devcv.order.domain;

import com.devcv.common.domain.BaseTimeEntity;
import com.devcv.member.domain.Member;
import com.devcv.register.domain.Resume;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @Column(unique = true)
    private String id;

    @ManyToOne
    private Member member;

    @OneToOne
    private Resume resume;

    @Column
    private int totalAmount;

    @Column
    @Enumerated(value = EnumType.STRING)
    private PayType payType;

    @Column
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(Member member, Resume resume) {
        this.id = OrderNumberGenerator.generateOrderNumber();
        this.member = member;
        this.resume = resume;
        this.totalAmount = resume.getPrice();
        this.payType = PayType.POINT;
        this.orderStatus = OrderStatus.CREATED;
    }

    public static Order of(Member member, Resume resume) {
        // 검증?
        return new Order(member, resume);
    }
}