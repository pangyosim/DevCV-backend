package com.devcv.order.application;

import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.ForbiddenException;
import com.devcv.common.exception.BadRequestException;
import com.devcv.member.domain.Member;
import com.devcv.order.domain.Order;
import com.devcv.order.domain.dto.OrderRequest;
import com.devcv.order.domain.dto.OrderResponse;
import com.devcv.order.domain.dto.OrderSheet;
import com.devcv.order.domain.dto.OrderListResponse;
import com.devcv.order.exception.OrderNotFoundException;
import com.devcv.point.application.PointService;
import com.devcv.resume.domain.Resume;
import com.devcv.order.repository.OrderRepository;
import com.devcv.resume.domain.enumtype.ResumeStatus;
import com.devcv.review.exception.AlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final static String DESCRIPTION = "상품 구매 - 주문번호:";

    private final OrderRepository orderRepository;
    private final PointService pointService;

    public OrderSheet getOrderSheet(Member member, Resume resume) {
        Long myPoint = pointService.getMyPoint(member.getMemberId());
        return OrderSheet.of(member, resume, myPoint);
    }

    @Transactional
    public Order createOrder(Member member, Resume resume, OrderRequest orderRequest) {
        compareMemberId(member, orderRequest.memberId());
        compareResumeInfo(resume, orderRequest);
        checkResumeStatus(resume);
        checkOrderExists(member, resume);
        preventSelfPurchase(member, resume);
        checkPoint(member, resume);

        Order order = orderRepository.save(Order.of(member, resume));
        pointService.usePoint(member, (long) resume.getPrice(),  DESCRIPTION + order.getOrderId());
        return order;
    }

    private void compareMemberId(Member member, Long requestMemberId) {
        if (!member.getMemberId().equals(requestMemberId)) {
            throw new BadRequestException(ErrorCode.ORDER_INFO_MISMATCH_EXCEPTION);
        }
    }

    private void compareResumeInfo(Resume origin, OrderRequest request) {
        if (!origin.getResumeId().equals(request.resumeId())) {
            throw new BadRequestException(ErrorCode.ORDER_INFO_MISMATCH_EXCEPTION);
        }
        if (origin.getPrice() != request.price()) {
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

    private void checkResumeStatus(Resume resume) {
        if (!resume.getStatus().equals(ResumeStatus.판매승인)) {
            throw new BadRequestException(ErrorCode.RESUME_STATUS_EXCEPTION);
        }
    }

    private void checkOrderExists(Member member, Resume resume) {
        Optional<Order> orderOptional = orderRepository.findByMember_MemberIdAndResume_ResumeId(member.getMemberId(), resume.getResumeId());
        if (orderOptional.isPresent()) {
            throw new AlreadyExistsException(ErrorCode.ALREADY_EXISTS_ORDER);
        }
    }

    private void preventSelfPurchase(Member member, Resume resume) {
        Long memberId = member.getMemberId();
        Long resumeMemberId = resume.getMember().getMemberId();
        if (memberId.equals(resumeMemberId)) {
            throw new ForbiddenException(ErrorCode.MEMBER_MISMATCH_EXCEPTION);
        }
    }

    public Order getOrderByIdAndMember(String orderId, Member member) {
        Order order = orderRepository.findOrderByOrderIdAndMember(orderId, member)
                .orElseThrow(() -> new OrderNotFoundException(ErrorCode.ORDER_NOT_FOUND));
        compareMemberId(member, order.getMember().getMemberId());
        return order;
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