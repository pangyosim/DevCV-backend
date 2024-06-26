package com.devcv.order.domain.dto;

import java.util.List;

public record CartOrderRequest(int resumeCount, Long totalPrice, List<CartDto> cartList) {

}