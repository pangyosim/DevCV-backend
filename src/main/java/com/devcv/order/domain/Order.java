package com.devcv.order.domain;

import com.devcv.common.domain.BaseTimeEntity;
import com.devcv.member.domain.Member;
import com.devcv.resume.domain.Resume;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "tb_order")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @Column(unique = true)
    private String orderId;

    @ManyToOne
    private Member member;

    @OneToOne
    private Resume resume;

    @Column
    private int totalAmount;

    @Column
    private String sellerName;

    @Column
    @Enumerated(value = EnumType.STRING)
    private PayType payType;

    @Column
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(Member member, Resume resume) {
        this.orderId = OrderNumberGenerator.generateOrderNumber();
        this.member = member;
        this.resume = resume;
        this.totalAmount = resume.getPrice();
        this.sellerName = resume.getMember().getMemberName();
        this.payType = PayType.POINT;
        this.orderStatus = OrderStatus.CREATED;
    }

    public static Order of(Member member, Resume resume) {
        // 검증?
        return new Order(member, resume);
    }
}