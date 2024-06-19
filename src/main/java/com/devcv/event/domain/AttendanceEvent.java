package com.devcv.event.domain;

import com.devcv.common.domain.BaseTimeEntity;
import com.devcv.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tb_attendance_event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendanceEvent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private AttendanceEvent(Member member, Event event) {
        this.id = null;
        this.member = member;
        this.event = event;
    }

    public static AttendanceEvent of(Member member, Event event) {
        return new AttendanceEvent(member, event);
    }
}
