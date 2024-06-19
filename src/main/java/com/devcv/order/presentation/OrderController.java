package com.devcv.order.presentation;

import com.devcv.member.application.MemberService;
import com.devcv.member.domain.Member;
import com.devcv.order.application.OrderService;
import com.devcv.order.domain.Order;
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

    @GetMapping("/resumes/{resume-id}/checkout")
    public ResponseEntity<OrderSheet> checkoutResume(@AuthenticationPrincipal UserDetails userDetails,
                                                     @PathVariable("resume-id") Long resumeId) {
        Member member = extractMember(userDetails);
        Resume resume = resumeService.findByResumeId(resumeId);
        return ResponseEntity.ok().body(orderService.createOrderSheet(member, resume));
    }

    @PostMapping("/orders")
    public ResponseEntity<Void> createOrder(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody OrderRequest orderRequest) {
        Member member = extractMember(userDetails);
        Resume resume = resumeService.findByResumeId(orderRequest.resumeId());

        Order order = orderService.createOrder(member, resume, orderRequest);
        return ResponseEntity.created(URI.create(order.getOrderId())).build();
    }

    @GetMapping("/orders/{order-id}")
    public ResponseEntity<OrderResponse> getOrderResponse(@AuthenticationPrincipal UserDetails userDetails,
                                                          @PathVariable("order-id") String orderId) {
        Member member = extractMember(userDetails);
        return ResponseEntity.ok().body(OrderResponse.from(orderService.getOrderByIdAndMember(orderId, member)));
    }

    private Member extractMember(UserDetails userDetails) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        return memberService.findMemberBymemberId(memberId);
    }
}