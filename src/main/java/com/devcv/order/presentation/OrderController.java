package com.devcv.order.presentation;

import com.devcv.member.Domain.Member;
import com.devcv.order.application.OrderService;
import com.devcv.order.domain.dto.OrderSheet;
import com.devcv.resume.Resume;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders/new")
    public ResponseEntity<OrderSheet> newOrderSheet() {

        //구매자정보조회(임시)
        Member member = new Member(1L,"이름1", "이메일1");

        //상품정보조회(임시)
        Resume resume = new Resume(1L, "제목1", "내용1");

        return ResponseEntity.ok().body(orderService.getOrderSheet(member, resume));
    }
}