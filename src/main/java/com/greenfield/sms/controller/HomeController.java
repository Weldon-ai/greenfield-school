
package com.greenfield.sms.controller;

import com.greenfield.sms.service.EventService;
import com.greenfield.sms.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EventService eventService;
    private final NewsService newsService;

    public HomeController(EventService eventService, NewsService newsService) {
        this.eventService = eventService;
        this.newsService = newsService;
    }

    // Public home page, accessible without login
    @GetMapping({"/", "/home"})
    public String home(Model model) {

        // Upcoming events
        model.addAttribute("events", eventService.getAllEvents());

        // Latest news
        model.addAttribute("newsList", newsService.getLatestNews());

        // About the school
        model.addAttribute("schoolInfo", """
                Green-Field School is committed to providing excellent education
                and nurturing young minds to become responsible citizens.
                Our mission is to blend academics, sports, and cultural activities
                to produce well-rounded individuals.
                """);

        // Downloadable academic calendar
        model.addAttribute("calendarLink", "/files/academic-calendar.pdf");

        // Latest announcements
        model.addAttribute("announcements", """
                - Mid-term exams start on March 15th
                - Parent-Teacher meeting on April 10th
                - Sports Day scheduled for May 5th
                """);

        // Contact information
        model.addAttribute("contactInfo", """
                Email: info@greenfieldschool.ac.ke
                Phone: +254 700 123456
                Address: Sergoit, Iten, Elgeyo Marakwet County, Kenya
                """);

        return "home"; // Renders templates/home.html
    }
}
