package com.devcv.order.application;

import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.TestErrorException;
import com.devcv.member.domain.Member;
import com.devcv.order.domain.Order;
import com.devcv.order.domain.dto.OrderRequest;
import com.devcv.order.domain.dto.OrderSheet;
import com.devcv.point.application.PointService;
import com.devcv.resume.domain.Resume;
import com.devcv.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        compareMemberId(member, orderRequest);
        compareResumeInfo(resume, orderRequest);
        checkPoint(member, resume);

        Order order = orderRepository.save(Order.of(member, resume));
        pointService.usePoint(member, (long) resume.getPrice(),  DESCRIPTION + order.getOrderId());
        return order;
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new TestErrorException(ErrorCode.TEST_ERROR));
    }

    private void compareMemberId(Member member, OrderRequest request) {
        if (!member.getMemberId().equals(request.memberId())) {
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
}