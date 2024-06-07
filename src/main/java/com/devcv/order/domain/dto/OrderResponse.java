package com.devcv.order.domain.dto;

import com.devcv.order.domain.Order;
import com.devcv.order.domain.OrderStatus;
import com.devcv.order.domain.PayType;

import java.time.LocalDateTime;


public record OrderResponse(String id,
                            String resumeTitle,
//                            String thumbnail,
                            int totalAmount,
                            OrderStatus orderStatus,
                            LocalDateTime createdDate,
                            PayType payType) {

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getResume().getTitle(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getCreatedDate(),
                order.getPayType());
    }
}