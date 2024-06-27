package com.devcv.order.application;

import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.BadRequestException;
import com.devcv.common.exception.NotFoundException;
import com.devcv.member.domain.Member;
import com.devcv.order.domain.Order;
import com.devcv.order.domain.OrderResume;
import com.devcv.order.domain.dto.*;
import com.devcv.order.exception.OrderNotFoundException;
import com.devcv.order.repository.OrderResumeRepository;
import com.devcv.point.application.PointService;
import com.devcv.resume.domain.Resume;
import com.devcv.order.repository.OrderRepository;
import com.devcv.resume.domain.enumtype.ResumeStatus;
import com.devcv.resume.repository.ResumeRepository;
import com.devcv.review.exception.AlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final static String DESCRIPTION = "상품 구매 - 주문번호:";

    private final OrderRepository orderRepository;
    private final ResumeRepository resumeRepository;
    private final OrderResumeRepository orderResumeRepository;
    private final PointService pointService;

    public OrderSheet getOrderSheet(Member member, Resume resume) {
        Long myPoint = pointService.getMyPoint(member.getMemberId());
        return OrderSheet.of(member, resume, myPoint);
    }

    @Transactional
    public Order createOrder(Member member, CartOrderRequest cartOrderRequest) {
//        checkPoint(member, resume);
        List<Long> existingResumeIdList = orderRepository.getResumeIdsByMemberId(member.getMemberId());
        Order order = orderRepository.save(Order.init(member));

        List<OrderResume> orderResumeList = new ArrayList<>();
        for (CartDto dto : cartOrderRequest.cartList()) {
            Resume resume = resumeRepository.findByResumeId(dto.resumeId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.RESUME_NOT_FOUND));
            validateResume(resume, dto, existingResumeIdList);
            OrderResume orderResume = OrderResume.of(order, resume);
            orderResumeList.add(orderResume);
        }

        orderResumeRepository.saveAll(orderResumeList);
        order.updateOrderResumeList(orderResumeList);
//        pointService.usePoint(member, (long) resume.getPrice(),  DESCRIPTION + order.getOrderId());
        return order;
    }

    private void validateResume(Resume resume, CartDto dto, List<Long> existingResumeIdList) {
        checkResumeStatus(resume);
        validateCartDtoWithOrigin(resume, dto);
        checkDuplicate(resume, existingResumeIdList);
    }

    private void checkResumeStatus(Resume resume) {
        if (!resume.getStatus().equals(ResumeStatus.approved)) {
            throw new BadRequestException(ErrorCode.RESUME_STATUS_EXCEPTION);
        }
    }

    private void validateCartDtoWithOrigin(Resume origin, CartDto dto) {
        if (!origin.getResumeId().equals(dto.resumeId())) {
            throw new BadRequestException(ErrorCode.ORDER_INFO_MISMATCH_EXCEPTION);
        }
        if (origin.getPrice() != dto.price()) {
            throw new BadRequestException(ErrorCode.ORDER_INFO_MISMATCH_EXCEPTION);
        }
    }

    private void checkPoint(Member member, Resume resume) {
        Long memberPoint = pointService.getMyPoint(member.getMemberId());
        Long resumePrice = (long) resume.getPrice();
        if (memberPoint < resumePrice) {
            throw new BadRequestException(ErrorCode.INSUFFICIENT_POINT);
        }
    }

    private void checkDuplicate(Resume resume, List<Long> existingIds) {
        if (existingIds.stream().anyMatch(existingId -> existingId.equals(resume.getResumeId()))){
            throw new AlreadyExistsException(ErrorCode.ALREADY_EXISTS_ORDER);
        }
    }

    public OrderResponse getOrderResponse(Long orderId, Member member) {
        Order order = orderRepository.findOrderByOrderIdAndMember(orderId, member)
                .orElseThrow(() -> new OrderNotFoundException(ErrorCode.ORDER_NOT_FOUND));
        List<OrderResume> orderResumeList = orderResumeRepository.findAllByOrder_OrderId(orderId);
        return OrderResponse.of(order, orderResumeListToDto(orderResumeList));
    }

    private List<OrderResumeDto> orderResumeListToDto(List<OrderResume> orderResumeList) {
        return orderResumeList.stream().map(OrderResume::getResume).map(OrderResumeDto::from).collect(Collectors.toList());
    }

    public OrderListResponse getOrderListByMember(Member member) {
        List<OrderResponse> orderList = orderRepository.findOrderListByMember(member).stream()
                .map(order -> getOrderResponse(order.getOrderId(), member))
                .collect(Collectors.toList());
        int count = orderList.size();
        return OrderListResponse.of(member.getMemberId(), count, orderList);
    }
}