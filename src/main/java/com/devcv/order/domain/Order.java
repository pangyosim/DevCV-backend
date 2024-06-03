package com.devcv.order.domain;

import com.devcv.common.domain.BaseTimeEntity;
import com.devcv.member.Domain.Member;
import com.devcv.resume.Resume;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Getter
    @Id
    @Column(unique = true)
    private String id;

    @ManyToOne
    private Member member;

    @OneToOne
    private Resume orderItem;

    @Column
    private int totalAmount;

    @Column
    @Enumerated(value = EnumType.STRING)
    private PayType payType;

    @Column
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(Member member, Resume orderItem) {
        this.id = OrderNumberGenerator.generateOrderNumber();
        this.member = member;
        this.orderItem = orderItem;
        this.totalAmount = orderItem.getPrice();
        this.payType = PayType.Point;
        this.orderStatus = OrderStatus.CREATED;
    }

    public static Order of(Member member, Resume resume) {
        // 검증?
        return new Order(member, resume);
    }
}