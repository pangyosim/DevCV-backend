package com.devcv.event.presentation;

import com.devcv.event.application.EventService;
import com.devcv.event.domain.dto.EventListResponse;
import com.devcv.event.domain.dto.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<EventListResponse> getEventList() {
        return ResponseEntity.ok(eventService.getEventListResponse());
    }

    @GetMapping("/{event-id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable("event-id") Long eventId) {
        EventResponse eventResponse = eventService.getEventResponse(eventId);
        return ResponseEntity.ok(eventResponse);
    }
}