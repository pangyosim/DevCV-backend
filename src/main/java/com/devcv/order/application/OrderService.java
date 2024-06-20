package com.devcv.order.application;

import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.TestErrorException;
import com.devcv.member.domain.Member;
import com.devcv.order.domain.Order;
import com.devcv.order.domain.dto.OrderRequest;
import com.devcv.order.domain.dto.OrderResponse;
import com.devcv.order.domain.dto.OrderSheet;
import com.devcv.order.domain.dto.OrderListResponse;
import com.devcv.point.application.PointService;
import com.devcv.resume.domain.Resume;
import com.devcv.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final static String DESCRIPTION = "상품 구매 - 주문번호:";

    private final OrderRepository orderRepository;
    private final PointService pointService;

    public OrderSheet createOrderSheet(Member member, Resume resume) {
        return OrderSheet.of(member, resume);
    }

    @Transactional
    public Order createOrder(Member member, Resume resume, OrderRequest orderRequest) {
        compareMemberId(member, orderRequest.memberId());
        compareResumeInfo(resume, orderRequest);
        checkPoint(member, resume);

        Order order = orderRepository.save(Order.of(member, resume));
        pointService.usePoint(member, (long) resume.getPrice(),  DESCRIPTION + order.getOrderId());
        return order;
    }

    public Order getOrderByIdAndMember(String orderId, Member member) {
        Order order = orderRepository.findOrderByOrderIdAndMember(orderId, member)
                .orElseThrow(() -> new TestErrorException(ErrorCode.TEST_ERROR));
        compareMemberId(member, order.getMember().getMemberId());
        return order;
    }

    private void compareMemberId(Member member, Long requestMemberId) {
        if (!member.getMemberId().equals(requestMemberId)) {
            throw new TestErrorException(ErrorCode.TEST_ERROR);
        }
    }

    private void compareResumeInfo(Resume origin, OrderRequest request) {
        if (!origin.getResumeId().equals(request.resumeId())) {
            throw new TestErrorException(ErrorCode.TEST_ERROR);
        }
        if (origin.getPrice() != request.price()) {
            throw new TestErrorException(ErrorCode.TEST_ERROR);
        }
    }

    private void checkPoint(Member member, Resume resume) {
        Long memberPoint = pointService.getMyPoint(member.getMemberId());
        Long resumePrice = (long) resume.getPrice();
        if (memberPoint < resumePrice) {
            throw new TestErrorException(ErrorCode.TEST_ERROR);
        }
    }

    public OrderListResponse getOrderListByMember(Member member) {
        List<OrderResponse> orderList = orderRepository.findOrderListByMember(member)
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
        int count = orderList.size();
        return OrderListResponse.of(member.getMemberId(), count, orderList);
    }
}