package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.entities.*;
import com.sparta.eng72.traineetracker.services.*;
import com.sparta.eng72.traineetracker.utilities.DateCalculator;
import com.sparta.eng72.traineetracker.utilities.Pages;
import com.sparta.eng72.traineetracker.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AttendanceController {

    private TraineeService traineeService;
    private WeekReportService weekReportService;
    private AttendanceService attendanceService;
    private TrainerService trainerService;
    private CourseGroupService courseGroupService;

    @Autowired
    public AttendanceController(TraineeService traineeService, WeekReportService weekReportService, AttendanceService attendanceService, TrainerService trainerService, CourseGroupService courseGroupService) {
        this.traineeService = traineeService;
        this.weekReportService = weekReportService;
        this.attendanceService = attendanceService;
        this.trainerService = trainerService;
        this.courseGroupService = courseGroupService;
    }

    @GetMapping("/trainer/attendanceEntry")
    public ModelAndView getAllGroupTrainees(@ModelAttribute TraineeAttendance traineeAttendance, ModelMap modelMap, Principal principal){
        int groupId = trainerService.getTrainerByUsername(principal.getName()).get().getGroupId();
        List<Trainee> trainees = traineeService.getTraineesByGroupId(groupId);
        LocalDate date = LocalDate.now();
        LocalDate startDate = courseGroupService.getGroupByID(groupId).get().getStartDate().toLocalDate();

        modelMap.addAttribute("courseStartDate", startDate);
        modelMap.addAttribute("today", date);
        modelMap.addAttribute("trainees", trainees);
        modelMap.addAttribute("traineeAttendance", traineeAttendance);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_ATTENDANCE_PAGE), modelMap);
    }

    @PostMapping("/trainer/attendanceEntry")
    public ModelAndView postAllGroupTrainees(@ModelAttribute TraineeAttendance traineeAttendance, ModelMap modelMap){

        Trainee trainee = traineeService.getTraineeByID(traineeAttendance.getTraineeId()).get();
        modelMap.addAttribute("date", traineeAttendance.getAttendanceDate());
        modelMap.addAttribute("trainee", trainee);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_ATTENDANCE_SUCCESS), modelMap);
    }

    @RequestMapping(value="/trainer/viewTrainee", method= RequestMethod.POST, params="btnStatus=attendance")
    public String getTraineeAttendance(Integer traineeId, Model model) {
        return "redirect:traineeAttendance/"+traineeId;
    }

    @GetMapping("/trainer/traineeAttendance/{traineeId}")
    public String getTraineeAttendanceWithPath(@PathVariable Integer traineeId, Model model) {
        Trainee trainee = traineeService.getTraineeByID(traineeId).get();
        HashMap<Integer, List<AttendanceReport>> attendanceByWeek = getAttendanceReports(trainee);

        model.addAttribute("reports", attendanceByWeek);
        model.addAttribute("trainee", trainee);
        return Pages.accessPage(Role.TRAINER, Pages.TRAINER_ATTENDANCE);
    }

    @GetMapping("/trainee/trainee-attendance")
    public String getTraineeAttendance(Principal principal, ModelMap modelMap){
        Trainee trainee = traineeService.getTraineeByUsername(principal.getName()).get();
        HashMap<Integer, List<AttendanceReport>> attendanceByWeek = getAttendanceReports(trainee);

        modelMap.addAttribute("reports", attendanceByWeek);
        modelMap.addAttribute("trainee", trainee);
        return Pages.accessPage(Role.TRAINEE, Pages.TRAINEE_ATTENDANCE);
    }


    private HashMap<Integer, List<AttendanceReport>> getAttendanceReports(Trainee trainee) {
        Date startDate = Date.valueOf(courseGroupService.getGroupByID(trainee.getGroupId()).get().getStartDate().toLocalDate());
        List<TraineeAttendance> traineeAttendanceList = attendanceService.getTraineeAttendanceByTraineeId(trainee.getTraineeId());
        HashMap<Integer, List<AttendanceReport>> attendanceByWeek = new HashMap<>();

        for(TraineeAttendance attendance : traineeAttendanceList){
            int week = DateCalculator.getWeek(attendance.getAttendanceDate(), startDate);
            int day = DateCalculator.getDay(attendance.getAttendanceDate(), startDate);
            AttendanceReport report = new AttendanceReport(attendanceService.getAttendanceStatus(attendance.getAttendanceId()), attendance.getAttendanceDate(), day, week);
            if(!attendanceByWeek.containsKey(week)){
                List<AttendanceReport> attendanceList = new ArrayList<>();
                attendanceList.add(report);
                attendanceByWeek.put(week, attendanceList);
            } else{
                attendanceByWeek.get(week).add(report);
            }
        }
        return attendanceByWeek;
    }


}
