package com.devcv.order.repository;

import com.devcv.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    // 주문id 조회
    @Query(value = "SELECT o.id FROM orders o WHERE o.member_memberid = :memberId AND o.resume_resumeid = :resumeId",
            nativeQuery = true)
    Optional<String> findOrderIdByMemberIdAndResumeId(Long memberId, Long resumeId);


}