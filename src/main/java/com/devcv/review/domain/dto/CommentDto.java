package com.devcv.review.domain.dto;


import com.devcv.review.domain.Comment;
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

    private String sellerNickname;
    private String text;

    private LocalDateTime createdDate, updatedDate;

    public static CommentDto from(Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getCommentId())
                .reviewId(comment.getReview().getReviewId())
                .memberId(comment.getMember().getMemberId())
                .sellerNickname(comment.getSellerNickname())
                .text(comment.getText())
                .createdDate(comment.getCreatedDate())
                .updatedDate(comment.getUpdatedDate())
                .build();
    }

}