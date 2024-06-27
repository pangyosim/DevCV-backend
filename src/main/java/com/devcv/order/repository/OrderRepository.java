package com.devcv.order.repository;

import com.devcv.member.domain.Member;
import com.devcv.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<Order> findOrderByOrderIdAndMember(Long orderId, Member member);

    // 주문id 조회
    @Query("SELECT o FROM Order o " +
            "JOIN o.orderResumeList r " +
            "WHERE o.member.memberId = :memberId AND r.orderResumeId = :resumeId")
    Optional<Order> findByMemberIdAndResumeId(@Param("memberId") Long memberId, @Param("resumeId") Long resumeId);

    List<Order> findOrderListByMember(Member member);

    @Query("SELECT orderResume.resume.resumeId FROM OrderResume orderResume INNER JOIN orderResume.order o WHERE o.member.memberId = :memberId")
    List<Long> getResumeIdsByMemberId(@Param("memberId") Long memberId);
}