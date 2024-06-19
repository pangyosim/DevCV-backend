package com.devcv.review.application;


import com.devcv.member.domain.Member;
import com.devcv.resume.domain.Resume;
import com.devcv.order.domain.Order;
import com.devcv.review.domain.Review;
import com.devcv.review.domain.dto.PaginatedReviewResponse;
import com.devcv.review.domain.dto.ReviewDto;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface ReviewService {

    // 이력서의 모든 리뷰 조회
    PaginatedReviewResponse getListOfResume(Long resumeId, int page, int  size);

    // 이력서 리뷰 등록
    Review register(Long resumeId, Long memberId, ReviewDto resumeReviewDto);

//    // 이력서 리뷰 수정
//    void modify(ReviewDto resumeReviewDto);

    default Review dtoToEntity(ReviewDto resumeReviewDto) {

        Review resumeReview = Review.builder()
                .reviewId(resumeReviewDto.getReviewId())
                .resume(Resume.builder().resumeId(resumeReviewDto.getResumeId()).build())
                .member(Member.builder().memberId(resumeReviewDto.getMemberId()).build())
                .order(Order.builder().id(resumeReviewDto.getOrderId()).build())
                .grade(resumeReviewDto.getGrade())
                .text(resumeReviewDto.getText())
                .build();

        return resumeReview;
    }

    default ReviewDto entityToDto(Review resumeReview) {
        String orderId = (resumeReview.getOrder() != null) ? resumeReview.getOrder().getId() : null;

        ReviewDto resumeReviewDto = ReviewDto.builder()
                .reviewId(resumeReview.getReviewId())
                .resumeId(resumeReview.getResume().getResumeId())
                .memberId(resumeReview.getMember().getMemberId())
                .orderId(orderId) // order id
                .reviewerNickname(resumeReview.getMember().getNickName())
                .sellerNickname(resumeReview.getResume().getMember().getNickName())
                .grade(resumeReview.getGrade())
                .text(resumeReview.getText())
                .createdDate(resumeReview.getCreatedDate())
                .updatedDate(resumeReview.getUpdatedDate())
                .build();

        return resumeReviewDto;
    }







}
