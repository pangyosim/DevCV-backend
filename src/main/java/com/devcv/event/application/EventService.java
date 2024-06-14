package com.devcv.event.application;

import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.TestErrorException;
import com.devcv.event.domain.Event;
import com.devcv.event.domain.dto.EventRequest;
import com.devcv.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new TestErrorException(ErrorCode.TEST_ERROR));
    }
}
