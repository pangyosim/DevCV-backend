package com.devcv.order.repository;

import com.devcv.order.domain.OrderResume;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderResumeRepository extends JpaRepository<OrderResume, Long> {

    @EntityGraph(attributePaths = {"resume"}, type=EntityGraph.EntityGraphType.FETCH)
    List<OrderResume> findAllByOrder_OrderId(Long orderId);
}