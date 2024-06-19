package com.devcv.order.presentation;

import com.devcv.member.application.MemberService;
import com.devcv.member.domain.Member;
import com.devcv.order.application.OrderService;
import com.devcv.order.domain.dto.OrderRequest;
import com.devcv.order.domain.dto.OrderResponse;
import com.devcv.order.domain.dto.OrderSheet;
import com.devcv.resume.application.ResumeService;
import com.devcv.resume.domain.Resume;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ResumeService resumeService;
    private final MemberService memberService;

    @GetMapping("/resume/{resume-id}/checkout")
    public ResponseEntity<OrderSheet> newOrderSheet(@AuthenticationPrincipal UserDetails userDetails,
                                                    @PathVariable("resume-id") Long resumeId) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        Member member = memberService.findMemberByMemberId(memberId);
        Resume resume = resumeService.findByResumeId(resumeId);
        return ResponseEntity.ok().body(orderService.getOrderSheet(member, resume));
    }

    @PostMapping("/orders")
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequest orderRequest) {

        //member, resume 객체 가져오는 메서드

        //주문 생성
//        Order order = orderService.createOrder(member, resume);

//        return ResponseEntity.created(URI.create(order.getId())).build();
        return ResponseEntity.created(URI.create("test")).build();
    }

    @GetMapping("/orders/{order-id}")
    public ResponseEntity<OrderResponse> getOrderResponse(@PathVariable("order-id") String orderId) {

        return ResponseEntity.ok().body(OrderResponse.from(orderService.getOrderById(orderId)));
    }
}