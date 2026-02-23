package com.greenfield.sms.repository;

import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
List <Timetable> findByStudentClassOrderByDayOfWeekAscStartTimeAsc(Classes studentClass);
    
}
