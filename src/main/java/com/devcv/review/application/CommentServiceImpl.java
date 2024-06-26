package com.devcv.review.application;

import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.UnAuthorizedException;
import com.devcv.member.domain.Member;
import com.devcv.review.domain.Comment;
import com.devcv.review.domain.Review;
import com.devcv.review.domain.dto.CommentDto;
import com.devcv.review.exception.AlreadyExistsException;
import com.devcv.review.exception.ReviewNotFoundException;
import com.devcv.review.repository.CommentRepository;
import com.devcv.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements  CommentService{

    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public CommentDto addComment(Long reviewId, Member member, CommentDto commentDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getResume().getMember().getMemberId().equals(member.getMemberId())) {
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        // 한 판매자가 특정 리뷰에 대해 이미 코멘트를 작성했는지 확인
        if (commentRepository.existsByReviewAndMember(review, member)) {
            throw new AlreadyExistsException(ErrorCode.ALREADY_EXISTS);
        }

        Comment comment = Comment.builder()
                .review(review)
                .member(member)
                .text(commentDto.getText())
                .sellerNickname(member.getNickName())
                .build();

        review.addComment(comment);
        commentRepository.save(comment);
        return CommentDto.from(comment);

    }

    @Transactional
    @Override
    public void removeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ReviewNotFoundException(ErrorCode.REVIEW_NOT_FOUND));

        Review review = comment.getReview();
        review.removeComment(comment);
        commentRepository.delete(comment);
    }

}
