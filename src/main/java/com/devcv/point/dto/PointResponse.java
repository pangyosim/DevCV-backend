package com.devcv.point.dto;

public record PointResponse(Long memberId, Long point) {

    public static PointResponse of(Long memberId, Long point) {
        return new PointResponse(memberId, point);
    }
}