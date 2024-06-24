package com.devcv.order.domain.dto;

import java.util.List;

public record OrderListResponse(Long memberId, int count, List<OrderResponse> orderList) {

    public static OrderListResponse of(Long memberId, int count, List<OrderResponse> orderList) {
        return new OrderListResponse(memberId, count, orderList);
    }
}