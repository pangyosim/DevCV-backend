package com.devcv.point.dto;

public record PointRequestDto(Long memberId,
                              Long amount,
                              String description) {
}