package com.devcv.point.repository;

import com.devcv.point.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointRepository extends JpaRepository<Point, Long> {
    @Query("SELECT SUM(p.amount) FROM Point p WHERE p.member.userId = :memberId")
    Long findTotalPointsByMemberId(@Param("memberId") Long memberId);
}
