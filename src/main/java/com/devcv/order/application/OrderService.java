package com.devcv.order.application;

import com.devcv.member.Domain.Member;
import com.devcv.order.domain.dto.OrderSheet;
import com.devcv.resume.Resume;
import com.devcv.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

//    private final OrderRepository orderRepository;

    public OrderSheet getOrderSheet(Member member,
                                       Resume resume) {
        return OrderSheet.of(member, resume);
    }
}