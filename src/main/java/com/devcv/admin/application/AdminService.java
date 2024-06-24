package com.devcv.admin.application;

import com.devcv.admin.dto.AdminResumeList;
import com.devcv.admin.repository.AdminResumeRepository;
import com.devcv.event.domain.Event;
import com.devcv.event.domain.dto.EventRequest;
import com.devcv.event.repository.EventRepository;
import com.devcv.resume.domain.dto.ResumeResponse;
import com.devcv.resume.domain.enumtype.ResumeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final EventRepository eventRepository;
    private final AdminResumeRepository adminResumeRepository;


    public Event createEvent(EventRequest eventRequest) {
        Event event = Event.of(eventRequest.name(), eventRequest.startDate(), eventRequest.endDate());
        return eventRepository.save(event);
    }

    public AdminResumeList getResumesByStatus(String input) {
        ResumeStatus status = ResumeStatus.valueOf(input);
        List<ResumeResponse> resumeList = adminResumeRepository.findByStatus(status)
                .stream().map(ResumeResponse::from).toList();
        int count = resumeList.size();
        return AdminResumeList.of(status, count, resumeList);
    }
}