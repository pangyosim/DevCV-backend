package com.devcv.order.domain;

import com.devcv.common.domain.BaseTimeEntity;
import com.devcv.member.Domain.Member;
import com.devcv.resume.Resume;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column
    @Enumerated(value = EnumType.STRING)
    private PayType payType;

    @Column
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    //    @Builder
    public Order(String name, PayType payType) {
        this.id = generateOrderId();
        this.name = name;
        this.payType = payType;
        this.orderStatus = OrderStatus.CREATED;
    }

    public static Order of(Member member, Resume resume) {

        // 검증

        // 객체 생성
        Order order = new Order(resume.getTitle(), PayType.Point);

        return order;
    }

    private String generateOrderId() {
        //생성로직
        return "20240601000001";
    }

    public String getId() {
        return id;
    }
}