package com.devcv.review.presentation;

import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.InternalServerException;
import com.devcv.common.exception.UnAuthorizedException;
import com.devcv.order.application.OrderService;
import com.devcv.review.application.ReviewService;
import com.devcv.review.domain.Review;
import com.devcv.review.domain.dto.PaginatedReviewResponse;
import com.devcv.review.domain.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resumes")
@Slf4j
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final OrderService orderService;

    // 구매후기 전체 조회
    @GetMapping("/{resume-id}/reviews")
    public ResponseEntity<PaginatedReviewResponse> getList(
            @RequestParam("page") int page, @RequestParam("size") int size,
            @PathVariable("resume-id") Long resumeId) {

        try {
            PaginatedReviewResponse response = reviewService.getListOfResume(resumeId, page, size);
            return ResponseEntity.ok(response);
        } catch (InternalServerException e) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }


    // 구매후기 등록
    @PostMapping("/{resume-id}/reviews")
    public ResponseEntity<ReviewDto> addReview(@PathVariable("resume-id") Long resumeId,
                                            @AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody ReviewDto resumeReviewDto) {
        if(userDetails == null) {
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_ERROR);
        }
        Long memberId = Long.valueOf(userDetails.getUsername());

        Review resumeReview = reviewService.register(resumeId, memberId, resumeReviewDto);

        ReviewDto responseDto = reviewService.entityToDto(resumeReview);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

}
