package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.entities.*;
import com.sparta.eng72.traineetracker.services.*;
import com.sparta.eng72.traineetracker.utilities.DateCalculator;
import com.sparta.eng72.traineetracker.utilities.Pages;
import com.sparta.eng72.traineetracker.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Controller
public class AttendanceController {

    private final TraineeService traineeService;
    private final AttendanceService attendanceService;
    private final TrainerService trainerService;
    private final CourseGroupService courseGroupService;

    @Autowired
    public AttendanceController(TraineeService traineeService, AttendanceService attendanceService, TrainerService trainerService, CourseGroupService courseGroupService) {
        this.traineeService = traineeService;
        this.attendanceService = attendanceService;
        this.trainerService = trainerService;
        this.courseGroupService = courseGroupService;
    }

    @GetMapping("/trainer/attendanceEntry")
    public ModelAndView getAllGroupTrainees(@ModelAttribute TraineeAttendance traineeAttendance, ModelMap modelMap, Principal principal) {
        int groupId = getTrainer(principal).getGroupId();
        List<Trainee> trainees = traineeService.getTraineesByGroupId(groupId);
        Date startDate = Date.valueOf(getGroup(groupId).getStartDate().toLocalDate());

        setDefaultAttendanceAndDate(traineeAttendance);

        populateAttendanceEntryModelMap(modelMap, trainees, startDate);
        modelMap.addAttribute("traineeAttendance", traineeAttendance);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_ATTENDANCE_PAGE), modelMap);
    }

    @PostMapping("/trainer/attendanceEntry")
    public ModelAndView postAllGroupTrainees(@ModelAttribute TraineeAttendance traineeAttendance, ModelMap modelMap) {
        Trainee trainee = getTrainee(traineeAttendance);
        int groupId = trainee.getGroupId();

        List<Trainee> trainees = traineeService.getTraineesByGroupId(groupId);
        Date startDate = Date.valueOf(getGroup(groupId).getStartDate().toLocalDate());
        populateAttendanceEntryModelMap(modelMap, trainees, startDate);

        int dayOfWeek = getDayOfWeek(traineeAttendance, startDate);
        if (dayOfWeek == 6 || dayOfWeek == 7) {
            modelMap.addAttribute("error", "Trainees are out of office on weekends!");
            return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_ATTENDANCE_PAGE), modelMap);
        }

        setDayAndWeek(traineeAttendance, startDate);
        attendanceService.saveAttendance(traineeAttendance);

        String success = "Attendance successfully entered for " + trainee.getFirstName() + " " + trainee.getLastName()
                + " for date " + traineeAttendance.getAttendanceDate();
        modelMap.addAttribute("success", success);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_ATTENDANCE_PAGE), modelMap);

    }

    @GetMapping("/trainer/traineeAttendance/{traineeId}")
    public ModelAndView getTraineeAttendanceWithPath(@PathVariable Integer traineeId, ModelMap modelMap) {
        Trainee trainee = getTrainee(traineeId);
        Map<Integer, List<TraineeAttendance>> attendanceByWeek = getAttendanceReports(trainee);

        populateTraineeAttendanceModelMap(modelMap, trainee, attendanceByWeek);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_ATTENDANCE), modelMap);
    }

    @GetMapping("/trainee/trainee-attendance")
    public ModelAndView getTraineeAttendance(ModelMap modelMap, Principal principal){
        Trainee trainee = getTrainee(principal);
        Map<Integer, List<TraineeAttendance>> attendanceByWeek = getAttendanceReports(trainee);

        populateTraineeAttendanceModelMap(modelMap, trainee, attendanceByWeek);
        return new ModelAndView(Pages.accessPage(Role.TRAINEE, Pages.TRAINEE_ATTENDANCE), modelMap);
    }

    @GetMapping("/trainer/weekly-attendance")
    public ModelAndView getWeeklyAttendance(Principal principal, ModelMap modelMap){
        Trainer trainer = getTrainer(principal);
        int currentWeek = courseGroupService.getWeekByGroupId(trainer.getGroupId());
        Map<Integer, Map<Trainee, List<TraineeAttendance>>> attendanceByWeek = getWeeklyAttendanceReports(trainer, currentWeek);
        List<String> days = getDaysOfWeek();

        populateWeeklyAttendanceModelMap(modelMap, attendanceByWeek, days);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_WEEKLY_ATTENDANCE), modelMap);
    }

    private void setDefaultAttendanceAndDate(TraineeAttendance traineeAttendance) {
        traineeAttendance.setAttendanceId(1);
        traineeAttendance.setAttendanceDate(Date.valueOf(LocalDate.now()));
    }

    private Map<Integer, Map<Trainee, List<TraineeAttendance>>> getWeeklyAttendanceReports(Trainer trainer, int currentWeek) {

        List<Trainee> trainees = traineeService.getTraineesByGroupId(trainer.getGroupId());
        Map<Integer, Map<Trainee, List<TraineeAttendance>>> attendanceByWeek = new TreeMap<>(Collections.reverseOrder());
        for(int week = 1; week <= currentWeek; week++){
            Map<Trainee, List<TraineeAttendance>> traineeAttendanceMap = new HashMap<>();
            for(Trainee trainee : trainees){
                traineeAttendanceMap.put(trainee, (attendanceService.getTraineeAttendanceByTraineeIdAndWeek(trainee.getTraineeId(), week)));
            }
            attendanceByWeek.put(week, traineeAttendanceMap);
        }
        return attendanceByWeek;
    }

    private Map<Integer, List<TraineeAttendance>> getAttendanceReports(Trainee trainee) {
        List<TraineeAttendance> traineeAttendanceList = attendanceService.getTraineeAttendanceByTraineeId(trainee.getTraineeId());
        Map<Integer, List<TraineeAttendance>> attendanceByWeek = new TreeMap<>(Collections.reverseOrder());

        for(TraineeAttendance attendance : traineeAttendanceList){
            int week = attendance.getWeek();
            if(!attendanceByWeek.containsKey(week)){
                List<TraineeAttendance> attendanceList = new ArrayList<>();
                attendanceList.add(attendance);
                attendanceByWeek.put(week, attendanceList);
            } else{
                attendanceByWeek.get(week).add(attendance);
            }
        }
        return attendanceByWeek;
    }

    private List<String> getDaysOfWeek() {
        List<String> days = new ArrayList<>();
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        return days;
    }

    private Trainer getTrainer(Principal principal) {
        Trainer trainer = null;
        if (trainerService.getTrainerByUsername(principal.getName()).isPresent()) {
            trainer = trainerService.getTrainerByUsername(principal.getName()).get();
        }
        return trainer;
    }

    private Trainee getTrainee(Principal principal) {
        Trainee trainee = null;
        if (traineeService.getTraineeByUsername(principal.getName()).isPresent()) {
            trainee = traineeService.getTraineeByUsername(principal.getName()).get();
        }
        return trainee;
    }

    private Trainee getTrainee(Integer traineeId) {
        Trainee trainee = null;
        if (traineeService.getTraineeByID(traineeId).isPresent()) {
            trainee = traineeService.getTraineeByID(traineeId).get();
        }
        return trainee;
    }

    private Trainee getTrainee(TraineeAttendance traineeAttendance) {
        Trainee trainee = null;
        if (traineeService.getTraineeByID(traineeAttendance.getTraineeId()).isPresent()) {
            trainee = traineeService.getTraineeByID(traineeAttendance.getTraineeId()).get();
        }
        return trainee;
    }

    private CourseGroup getGroup(Integer groupId) {
        CourseGroup group = null;
        if (courseGroupService.getGroupByID(groupId).isPresent()) {
            group = courseGroupService.getGroupByID(groupId).get();
        }
        return group;
    }

    private int getDayOfWeek(TraineeAttendance traineeAttendance, Date startDate) {
        return DateCalculator.getDay(traineeAttendance.getAttendanceDate(),startDate);
    }

    private void setDayAndWeek(TraineeAttendance traineeAttendance, Date startDate) {
        traineeAttendance.setWeek(DateCalculator.getWeek(traineeAttendance.getAttendanceDate(), startDate));
        traineeAttendance.setDay(DateCalculator.getDay(traineeAttendance.getAttendanceDate(), startDate));
    }

    private void populateTraineeAttendanceModelMap(ModelMap modelMap, Trainee trainee, Map<Integer, List<TraineeAttendance>> attendanceByWeek) {
        modelMap.addAttribute("attendanceReports", attendanceByWeek);
        modelMap.addAttribute("trainee", trainee);
    }

    private void populateWeeklyAttendanceModelMap(ModelMap modelMap, Map<Integer, Map<Trainee, List<TraineeAttendance>>> attendanceByWeek, List<String> days) {
        modelMap.addAttribute("days", days);
        modelMap.addAttribute("reports", attendanceByWeek);
    }

    private void populateAttendanceEntryModelMap(ModelMap modelMap, List<Trainee> trainees, Date startDate) {
        modelMap.addAttribute("courseStartDate", startDate);
        modelMap.addAttribute("today", LocalDate.now());
        modelMap.addAttribute("trainees", trainees);
    }
}
