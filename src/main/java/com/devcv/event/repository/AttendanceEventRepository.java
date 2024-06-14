package com.devcv.event.repository;

import com.devcv.event.domain.AttendanceEvent;
import com.devcv.event.domain.Event;
import com.devcv.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface AttendanceEventRepository extends JpaRepository<AttendanceEvent, Long> {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END " +
            "FROM AttendanceEvent a " +
            "WHERE a.member = :member " +
            "AND a.event = :event " +
            "AND DATE(a.createdDate) = :createdDate")
    boolean existsByMemberAndEventAndDate(
            @Param("member") Member member,
            @Param("event") Event event,
            @Param("createdDate") LocalDate createdDate
    );
}