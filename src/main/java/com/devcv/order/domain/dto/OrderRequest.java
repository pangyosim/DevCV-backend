package com.devcv.order.domain.dto;

public record OrderRequest(Long memberId, Long resumeId, int price) {

}