package com.sparta.eng68.traineetracker.controllers;

import com.sparta.eng68.traineetracker.entities.Trainee;
import com.sparta.eng68.traineetracker.entities.WeekReport;
import com.sparta.eng68.traineetracker.services.AttendanceService;
import com.sparta.eng68.traineetracker.services.TraineeService;
import com.sparta.eng68.traineetracker.services.WeekReportService;
import com.sparta.eng68.traineetracker.utilities.Pages;
import com.sparta.eng68.traineetracker.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Controller
public class AttendanceController {
    private TraineeService traineeService;
    private WeekReportService weekReportService;
    private AttendanceService attendanceService;

    @Autowired
    public AttendanceController(TraineeService traineeService, WeekReportService weekReportService,
                                AttendanceService attendanceService) {
        this.traineeService = traineeService;
        this.weekReportService = weekReportService;
        this.attendanceService = attendanceService;
    }

    @RequestMapping(value="/trainer/viewTrainee", method= RequestMethod.POST, params="btnStatus=attendance")
    public String getTraineeAttendance(Integer traineeId, Model model) {
        return "redirect:traineeAttendance/"+traineeId;
    }

    @GetMapping("/trainer/traineeAttendance/{traineeId}")
    public String getTraineeAttendanceWithPath(@PathVariable Integer traineeId, Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - K:mm a", Locale.ENGLISH);
        Trainee trainee = traineeService.getTraineeByID(traineeId).get();
        List<WeekReport> reports = weekReportService.getReportsByTraineeID(traineeId);
//        List<WeekAttendance> attendances = attendanceService.getAttendanceByTraineeId(traineeId);
//        Collections.reverse(attendances);
        Collections.reverse(reports);
        model.addAttribute("traineeId", traineeId);
        model.addAttribute("trainee", trainee);
        model.addAttribute("reports", reports);
        model.addAttribute("now", LocalDateTime.now());
        model.addAttribute("dateFormat", formatter);
        return Pages.accessPage(Role.TRAINER, Pages.TRAINER_ATTENDANCE);
    }
}
