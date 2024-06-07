package com.devcv.order.presentation;

import com.devcv.order.application.OrderService;
import com.devcv.order.domain.dto.OrderRequest;
import com.devcv.order.domain.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders/new")
    public ResponseEntity<Void> newOrderSheet() {

        //member, resume 객체 가져오는 메서드

//        return ResponseEntity.ok().body(orderService.getOrderSheet(member, resume));
        return ResponseEntity.ok().build();
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