package com.devcv.event.domain.dto;

import java.util.List;

public record EventListResponse(int count, List<EventResponse> eventListResponse) {

    public static EventListResponse of(int count, List<EventResponse> eventListResponse) {
        return new EventListResponse(count, eventListResponse);
    }
}