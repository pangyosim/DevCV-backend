package com.devcv.order.repository;

import com.devcv.order.domain.OrderResume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderResumeRepository extends JpaRepository<OrderResume, Long> {

    @Query("SELECT o FROM OrderResume o JOIN FETCH o.resume i JOIN FETCH i.imageList WHERE o.order.orderId = :orderId")
    List<OrderResume> findAllByOrder_OrderId(@Param("orderId") Long orderId);
}