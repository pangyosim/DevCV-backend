package com.devcv.point.application;

import com.devcv.member.domain.Member;
import com.devcv.point.domain.Point;
import com.devcv.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public Long getMyPoint(Long memberId) {
        return pointRepository.findTotalPointsBymemberId(memberId);
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
