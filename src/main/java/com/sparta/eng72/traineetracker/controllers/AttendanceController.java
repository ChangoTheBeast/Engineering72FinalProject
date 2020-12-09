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
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

@Controller
public class AttendanceController {

    private TraineeService traineeService;
    private AttendanceService attendanceService;
    private TrainerService trainerService;
    private CourseGroupService courseGroupService;

    @Autowired
    public AttendanceController(TraineeService traineeService, AttendanceService attendanceService, TrainerService trainerService, CourseGroupService courseGroupService) {
        this.traineeService = traineeService;
        this.attendanceService = attendanceService;
        this.trainerService = trainerService;
        this.courseGroupService = courseGroupService;
    }

    @GetMapping("/trainer/attendanceEntry")
    public ModelAndView getAllGroupTrainees(@ModelAttribute TraineeAttendance traineeAttendance, ModelMap modelMap, Principal principal){
        int groupId = trainerService.getTrainerByUsername(principal.getName()).get().getGroupId();
        List<Trainee> trainees = traineeService.getTraineesByGroupId(groupId);
        LocalDate today = LocalDate.now();
        LocalDate startDate = courseGroupService.getGroupByID(groupId).get().getStartDate().toLocalDate();
        traineeAttendance.setAttendanceId(1);
        traineeAttendance.setAttendanceDate(Date.valueOf(today));

        modelMap.addAttribute("courseStartDate", startDate);
        modelMap.addAttribute("today", today);
        modelMap.addAttribute("todayString", today.toString());
        modelMap.addAttribute("trainees", trainees);
        modelMap.addAttribute("traineeAttendance", traineeAttendance);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_ATTENDANCE_PAGE), modelMap);
    }

    @PostMapping("/trainer/attendanceEntry")
    public ModelAndView postAllGroupTrainees(@ModelAttribute TraineeAttendance traineeAttendance, ModelMap modelMap){

        int groupId = traineeService.getTraineeByID(traineeAttendance.getTraineeId()).get().getGroupId();
        Date startDate = Date.valueOf(courseGroupService.getGroupByID(groupId).get().getStartDate().toLocalDate());
        traineeAttendance.setWeek(DateCalculator.getWeek(traineeAttendance.getAttendanceDate(), startDate));
        traineeAttendance.setDay(DateCalculator.getDay(traineeAttendance.getAttendanceDate(), startDate));
        attendanceService.saveAttendance(traineeAttendance);

        Trainee trainee = traineeService.getTraineeByID(traineeAttendance.getTraineeId()).get();
        modelMap.addAttribute("date", traineeAttendance.getAttendanceDate());
        modelMap.addAttribute("trainee", trainee);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_ATTENDANCE_SUCCESS), modelMap);

    }

    @RequestMapping(value="/trainer/viewTrainee", method = RequestMethod.POST, params="btnStatus=attendance")
    public String getTraineeAttendance(Integer traineeId) {
        return "redirect:traineeAttendance/"+traineeId;
    }

    @GetMapping("/trainer/traineeAttendance/{traineeId}")
    public ModelAndView getTraineeAttendanceWithPath(@PathVariable Integer traineeId, ModelMap modelMap) {
        Trainee trainee = traineeService.getTraineeByID(traineeId).get();
        Map<Integer, List<TraineeAttendance>> attendanceByWeek = getAttendanceReports(trainee);

        modelMap.addAttribute("attendanceReports", attendanceByWeek);
        modelMap.addAttribute("trainee", trainee);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_ATTENDANCE), modelMap);
    }

    @GetMapping("/trainee/trainee-attendance")
    public ModelAndView getTraineeAttendance(ModelMap modelMap,Principal principal){
        Trainee trainee = null;
        if (traineeService.getTraineeByUsername(principal.getName()).isPresent()) {
            trainee = traineeService.getTraineeByUsername(principal.getName()).get();
        }
        Map<Integer, List<TraineeAttendance>> attendanceByWeek = getAttendanceReports(trainee);
        modelMap.addAttribute("currentWeek", courseGroupService.getWeekByGroupId(trainee.getGroupId()));
        modelMap.addAttribute("attendanceReports", attendanceByWeek);
        modelMap.addAttribute("trainee", trainee);
        return new ModelAndView(Pages.accessPage(Role.TRAINEE, Pages.TRAINEE_ATTENDANCE), modelMap);
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

    @GetMapping("/trainee/profile-percentage")
    public ModelAndView getTraineeAttendancePercentage(Principal principal, ModelMap modelMap) {
        Trainee trainee = traineeService.getTraineeByUsername(principal.getName()).get();
        List<TraineeAttendance> traineeAttendanceList = attendanceService.getTraineeAttendanceByTraineeId(trainee.getTraineeId());

        double onTime = 0, late = 0, excused = 0, unexcused = 0;
        for(TraineeAttendance attendance : traineeAttendanceList){
            switch (attendance.getAttendanceId()) {
                case 1:
                    onTime++;
                    continue;
                case 2:
                    late++;
                    continue;
                case 3:
                    excused++;
                    continue;
                case 4:
                    unexcused++;
                    continue;
                default:
                    continue;
            }
        }

        double count = onTime + late + excused + unexcused;
        DecimalFormat decimal = new DecimalFormat("###.##");

        String onTimePercentage = "" + decimal.format(onTime/count * 100) + "%";
        String latePercentage = "" + decimal.format(late/count * 100) + "%";
        String excusedPercentage = "" + decimal.format(excused/count * 100) + "%";
        String unexcusedPercentage = "" + decimal.format(unexcused/count * 100) + "%";

        modelMap.addAttribute("onTimePercentage", onTimePercentage);
        modelMap.addAttribute("latePercentage", latePercentage);
        modelMap.addAttribute("excusedPercentage", excusedPercentage);
        modelMap.addAttribute("unexcusedPercentage", unexcusedPercentage);

        return new ModelAndView("fragments/attendancePercentages", modelMap);
    }

    @GetMapping("/trainee/profile-percentage/{traineeId}")
    public ModelAndView getTraineeAttendancePercentage(@PathVariable Integer traineeId, ModelMap modelMap) {
        List<TraineeAttendance> traineeAttendanceList = attendanceService.getTraineeAttendanceByTraineeId(traineeId);

        double onTime = 0, late = 0, excused = 0, unexcused = 0;
        for(TraineeAttendance attendance : traineeAttendanceList){
            switch (attendance.getAttendanceId()) {
                case 1:
                    onTime++;
                    continue;
                case 2:
                    late++;
                    continue;
                case 3:
                    excused++;
                    continue;
                case 4:
                    unexcused++;
                    continue;
                default:
                    continue;
            }
        }

        double count = onTime + late + excused + unexcused;

        String onTimePercentage = "" + Math.round((onTime/count * 100)) + "%";
        String latePercentage = "" + Math.round(late/count * 100) + "%";
        String excusedPercentage = "" + Math.round(excused/count * 100) + "%";
        String unexcusedPercentage = "" + Math.round(unexcused/count * 100) + "%";

        modelMap.addAttribute("onTimePercentage", onTimePercentage);
        modelMap.addAttribute("latePercentage", latePercentage);
        modelMap.addAttribute("excusedPercentage", excusedPercentage);
        modelMap.addAttribute("unexcusedPercentage", unexcusedPercentage);

        return new ModelAndView("fragments/attendancePercentages", modelMap);
    }

    @GetMapping("/trainer/weekly-attendance")
    public ModelAndView getWeeklyAttendance(Principal principal, ModelMap modelMap){
        Trainer trainer = trainerService.getTrainerByUsername(principal.getName()).get();
        int currentWeek = courseGroupService.getWeekByGroupId(trainer.getGroupId());
        Map<Integer, Map<Trainee, List<TraineeAttendance>>> attendanceByWeek = getWeeklyAttendanceReports(trainer, currentWeek);
        List<String> days = new ArrayList<>();
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");

        modelMap.addAttribute("days", days);
        modelMap.addAttribute("currentWeek", currentWeek);
        modelMap.addAttribute("reports", attendanceByWeek);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_WEEKLY_ATTENDANCE), modelMap);
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

}
