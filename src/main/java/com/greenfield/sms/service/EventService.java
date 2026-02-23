package com.greenfield.sms.service;

import com.greenfield.sms.model.Event;
import java.util.List;

public interface EventService {

    List<Event> getAllEvents();

    Event getEventById(Long id);

    Event saveEvent(Event event);

    void deleteEvent(Long id);
}
