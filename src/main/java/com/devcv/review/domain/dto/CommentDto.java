package com.devcv.review.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long commentId;

    private Long reviewId;
    private Long memberId;

    private String memberNickname;
    private String text;

    private LocalDateTime createdDate, updatedDate;

}