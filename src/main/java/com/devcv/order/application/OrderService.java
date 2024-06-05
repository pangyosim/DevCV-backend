package com.devcv.order.application;

import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.TestErrorException;
import com.devcv.member.Domain.Member;
import com.devcv.order.domain.Order;
import com.devcv.order.domain.dto.OrderSheet;
import com.devcv.resume.Resume;
import com.devcv.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderSheet getOrderSheet(Member member,
                                    Resume resume) {
        return OrderSheet.of(member, resume);
    }

    public Order createOrder(Member member,
                             Resume resume) {
        return orderRepository.save(Order.of(member, resume));
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new TestErrorException(ErrorCode.TEST_ERROR));
    }
}