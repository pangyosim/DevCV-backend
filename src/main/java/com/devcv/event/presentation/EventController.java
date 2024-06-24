package com.devcv.event.presentation;

import com.devcv.event.application.EventService;
import com.devcv.event.domain.Event;
import com.devcv.event.domain.dto.EventListResponse;
import com.devcv.event.domain.dto.EventRequest;
import com.devcv.event.domain.dto.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Object> createEvent(@RequestBody EventRequest eventRequest) {
        Event event = eventService.createEvent(eventRequest);
        return ResponseEntity.created(URI.create(String.valueOf(event.getId()))).build();
    }

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