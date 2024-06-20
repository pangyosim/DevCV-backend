package com.devcv.order.repository;

import com.devcv.member.domain.Member;
import com.devcv.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<Order> findOrderByOrderIdAndMember(String orderId, Member member);

    // 주문id 조회
    @Query(value = "SELECT o.id FROM orders o WHERE o.member_memberid = :memberId AND o.resume_resumeid = :resumeId",
            nativeQuery = true)
    Optional<String> findOrderIdByMemberIdAndResumeId(Long memberId, Long resumeId);

    List<Order> findOrderListByMember(Member member);
}