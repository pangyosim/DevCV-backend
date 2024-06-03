package com.devcv.order.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {

    CREATED("주문생성완료"),
    PENDING_PAYMENT("결제대기"),
    COMPLETED("구매완료");

    private final String status;

    OrderStatus(String status){
        this.status = status;
    }
}