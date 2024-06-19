package com.devcv.review.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long reviewId;

    private Long resumeId;

    private Long memberId;

    private String orderId; // orderid

    private int grade;

    private String text;

    private LocalDateTime createdDate, updatedDate;

    private String reviewerNickname;
    private String sellerNickname;

    private List<CommentDto> comments;

}
