
package com.greenfield.sms.controller;

import com.greenfield.sms.model.Event;
import com.greenfield.sms.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class EventApiController {

    private final EventService service;

    public EventApiController(EventService service) {
        this.service = service;
    }

    @GetMapping("/events")
    public List<Map<String, Object>> getEvents() {

        List<Map<String, Object>> list = new ArrayList<>();

        for (Event e : service.getAllEvents()) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", e.getTitle());
            map.put("start", e.getEventDate().toString());
            list.add(map);
        }

        return list;
    }
}
