package com.devcv.order.presentation;

import com.devcv.member.Domain.Member;
import com.devcv.member.MemberRepository;
import com.devcv.order.application.OrderService;
import com.devcv.order.domain.Order;
import com.devcv.order.domain.dto.OrderRequest;
import com.devcv.order.domain.dto.OrderResponse;
import com.devcv.order.domain.dto.OrderSheet;
import com.devcv.resume.Resume;
import com.devcv.resume.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //임시
    private final MemberRepository memberRepository;
    private final ResumeRepository resumeRepository;

    @GetMapping("/orders/new")
    public ResponseEntity<OrderSheet> newOrderSheet() {

        Member member = mockMember();
        Resume resume = mockResume();

        return ResponseEntity.ok().body(orderService.getOrderSheet(member, resume));
    }

    @PostMapping("/orders")
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequest orderRequest) {

        Member member = mockMember();
        Resume resume = mockResume();

        //주문 생성
        Order order = orderService.createOrder(member, resume);

        return ResponseEntity.created(URI.create(order.getId())).build();
    }

    @GetMapping("/orders/{order-id}")
    public ResponseEntity<OrderResponse> getOrderResponse(@PathVariable("order-id") String orderId) {

        return ResponseEntity.ok().body(OrderResponse.from(orderService.getOrderById(orderId)));
    }

    private Member mockMember() {
        return memberRepository.save(new Member(1L, "이름1", "email@1.com", 100000));
    }

    private Resume mockResume() {
        return resumeRepository.save(new Resume(1L, "이름1", 1000));
    }
}