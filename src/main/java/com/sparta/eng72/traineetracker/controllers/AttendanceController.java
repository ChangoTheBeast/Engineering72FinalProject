package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.entities.TraineeAttendance;
import com.sparta.eng72.traineetracker.services.CourseGroupService;
import com.sparta.eng72.traineetracker.services.AttendanceService;
import com.sparta.eng72.traineetracker.services.TraineeService;
import com.sparta.eng72.traineetracker.services.TrainerService;
import com.sparta.eng72.traineetracker.utilities.Pages;
import com.sparta.eng72.traineetracker.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class AttendanceController {

    public final TrainerService trainerService;
    public final TraineeService traineeService;
    public final CourseGroupService courseGroupService;
    public final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(TrainerService trainerService, TraineeService traineeService, CourseGroupService courseGroupService, AttendanceService attendanceService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.courseGroupService = courseGroupService;
        this.attendanceService = attendanceService;
    }

    @GetMapping("/trainer/attendanceEntry")
    public ModelAndView getAllGroupTrainees(@ModelAttribute TraineeAttendance traineeAttendance, ModelMap modelMap, Principal principal){
        int groupId = trainerService.getTrainerByUsername(principal.getName()).get().getGroupId();
        List<Trainee> trainees = traineeService.getTraineesByGroupId(groupId);

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

}
