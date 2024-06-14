package com.devcv.event.presentation;

import com.devcv.event.application.EventService;
import com.devcv.event.domain.Event;
import com.devcv.event.domain.dto.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventController {

    private final EventService eventService;

    @PostMapping("/")
    public ResponseEntity<Object> createEvent(@RequestBody EventRequest eventRequest) {
        Event event = eventService.createEvent(eventRequest);
        return ResponseEntity.created(URI.create(String.valueOf(event.getId()))).build();
    }
}