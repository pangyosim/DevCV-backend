package com.devcv.event.application;

import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.NotFoundException;
import com.devcv.event.domain.Event;
import com.devcv.event.domain.dto.EventListResponse;
import com.devcv.event.domain.dto.EventRequest;
import com.devcv.event.domain.dto.EventResponse;
import com.devcv.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event createEvent(EventRequest eventRequest) {
        Event event = Event.of(eventRequest.name(), eventRequest.startDate(), eventRequest.endDate());
        return eventRepository.save(event);
    }

    public Event findByEventId(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.EVENT_NOT_FOUND));
    }

    public EventResponse getEventResponse(Long eventId) {
        return EventResponse.from(findByEventId(eventId));
    }

    public EventListResponse getEventListResponse() {
        List<EventResponse> eventResponseList = eventRepository.findAll()
                .stream()
                .map(EventResponse::from)
                .toList();
        int count = eventResponseList.size();
        return EventListResponse.of(count, eventResponseList);
    }
}