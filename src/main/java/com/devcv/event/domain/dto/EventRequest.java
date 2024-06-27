package com.devcv.event.domain.dto;

import java.time.LocalDateTime;

public record EventRequest(String name, String eventCategory, LocalDateTime startDate, LocalDateTime endDate) {

}