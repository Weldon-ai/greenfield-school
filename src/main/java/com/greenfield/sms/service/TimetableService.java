package com.greenfield.sms.service;

import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.Timetable;
import com.greenfield.sms.repository.TimetableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimetableService {

    private final TimetableRepository timetableRepository;

    public TimetableService(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    public List<Timetable> getTimetableForClass(Classes studentClass) {
        return timetableRepository.findByStudentClassOrderByDayOfWeekAscStartTimeAsc( studentClass);
    }
    
}
