package com.greenfield.sms.service;

import com.greenfield.sms.model.Event;
import com.greenfield.sms.repository.EventRepository;
import com.greenfield.sms.service.EventService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository repository;

    public EventServiceImpl(EventRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Event> getAllEvents() {
        return repository.findAllByOrderByEventDateAsc();
    }

    @Override
    public Event getEventById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Override
    public Event saveEvent(Event event) {
        return repository.save(event);
    }

    @Override
    public void deleteEvent(Long id) {
        repository.deleteById(id);
    }
}
