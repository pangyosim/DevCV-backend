package com.devcv.event.domain;

import com.devcv.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "tb_event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    private Event(String name, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = null;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Event of(String name, LocalDateTime startDate, LocalDateTime endDate) {
        return new Event(name, startDate, endDate);
    }

    public Boolean isOngoing() {
        LocalDateTime currentDate = LocalDateTime.now();
        return (currentDate.isEqual(startDate) || currentDate.isAfter(startDate)) &&
                (currentDate.isEqual(endDate) || currentDate.isBefore(endDate));
    }
}