package com.devcv.review.application;

import com.devcv.common.exception.ErrorCode;
import com.devcv.member.domain.Member;
import com.devcv.member.repository.MemberRepository;
import com.devcv.order.domain.Order;
import com.devcv.order.exception.OrderNotFoundException;
import com.devcv.order.repository.OrderRepository;
import com.devcv.resume.domain.Resume;
import com.devcv.resume.exception.MemberNotFoundException;
import com.devcv.resume.exception.ResumeNotFoundException;
import com.devcv.resume.repository.ResumeRepository;
import com.devcv.review.domain.Review;
import com.devcv.review.domain.dto.PaginatedReviewResponse;
import com.devcv.review.domain.dto.ReviewDto;
import com.devcv.review.exception.ReviewAlreadyExistsException;
import com.devcv.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements  ReviewService{

    private final ReviewRepository reviewRepository;
    private final ResumeRepository resumeRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    // 댓글 조회
    @Override
    public PaginatedReviewResponse getListOfResume(Long resumeId, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);

        Resume resume = Resume.builder().resumeId(resumeId).build();

        Page<Review> reviewPage = reviewRepository.findByResume(resume, pageable);

        List<ReviewDto> reviewDtos = reviewPage.getContent().stream().map(this::entityToDto).toList();
        return new PaginatedReviewResponse(
                reviewDtos,
                reviewPage.getTotalElements(),
                reviewPage.getNumberOfElements(),
                reviewPage.getNumber()+1, // currentPage
                reviewPage.getTotalPages(),
                reviewPage.getSize(),
                Math.max(1, reviewPage.getNumber() - 2),  // startPage
                Math.min(reviewPage.getTotalPages(), reviewPage.getNumber() + 2) // endPage
        );


    }

    // 리뷰 등록
    @Override
    public Review register(Long resumeId, Long memberId, ReviewDto resumeReviewDto) {

        // 사용자가 해당 이력서를 주문했는지 확인
        Optional<String> orderIdOpt = orderRepository.findOrderIdByMemberIdAndResumeId(memberId, resumeId);
        if (orderIdOpt.isEmpty()) {
            throw new OrderNotFoundException(ErrorCode.ORDER_NOT_FOUND);
        }

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException(ErrorCode.RESUME_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        String orderId = orderIdOpt.get();
        resumeReviewDto.setResumeId(resumeId);
        resumeReviewDto.setMemberId(memberId);
        resumeReviewDto.setOrderId(orderId);

        // 한 회원이 특정 이력서에 대해 이미 구매후기를 작성했는지 확인
        if (reviewRepository.existsByResumeAndMember(resume, member)) {
            throw new ReviewAlreadyExistsException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }

        Review resumeReview = dtoToEntity(resumeReviewDto);

        reviewRepository.save(resumeReview);

        return resumeReview;
    }

    @Override
    public Review dtoToEntity(ReviewDto resumeReviewDto) {
        Resume resume = resumeRepository.findById(resumeReviewDto.getResumeId())
                .orElseThrow(() -> new ResumeNotFoundException(ErrorCode.RESUME_NOT_FOUND));
        Member member = memberRepository.findById(resumeReviewDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        Order order = orderRepository.findById(resumeReviewDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(ErrorCode.ORDER_NOT_FOUND));

        Review resumeReview = Review.builder()
                .resume(resume)
                .order(order)
                .member(member)
                .grade(resumeReviewDto.getGrade())
                .text(resumeReviewDto.getText())
                .reviewerNickname(member.getNickName())
                .sellerNickname(resume.getMember().getNickName())
                .build();

        return resumeReview;
    }
}
