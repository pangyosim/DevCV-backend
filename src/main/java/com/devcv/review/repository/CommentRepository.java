package com.devcv.review.repository;

import com.devcv.member.domain.Member;
import com.devcv.review.domain.Comment;
import com.devcv.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByReview(Review review);

    // 특정 판매자 특정 리뷰에 대해 이미 코멘트를 작성했는지 중복 확인
    @Query("SELECT COUNT(c) > 0 FROM Comment c WHERE c.review = :review AND c.member = :member")
    boolean existsByReviewAndMember(Review review, Member member);


}
