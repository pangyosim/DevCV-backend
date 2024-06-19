package com.devcv.point.application;

import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.TestErrorException;
import com.devcv.member.domain.Member;
import com.devcv.point.domain.Point;
import com.devcv.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public Long getMyPoint(Long memberId) {
        Long point = pointRepository.findTotalPointsByMemberId(memberId);
        return Objects.requireNonNullElse(point, 0L);
    }

    public Point savePoint(Member member, Long amount, String description) {

        Point point = Point.save(member, amount, description);

        return pointRepository.save(point);
    }

    public Point usePoint(Member member, Long amount, String description) {

        Point point = Point.use(member, amount, description);

        return pointRepository.save(point);
    }
}
