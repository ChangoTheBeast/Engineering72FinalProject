package com.sparta.eng68.traineetracker.controllers;

import com.sparta.eng68.traineetracker.entities.*;
import com.sparta.eng68.traineetracker.utilities.NewUserForm;
import com.sparta.eng68.traineetracker.services.*;
import com.sparta.eng68.traineetracker.utilities.Pages;
import com.sparta.eng68.traineetracker.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TrainerHomeController {

    public final TraineeService traineeService;
    public final TrainerService trainerService;
    public final WeekReportService weekReportService;
    public final CourseGroupService courseGroupService;
    public final CourseService courseService;

    @Autowired
    public TrainerHomeController(TraineeService traineeService, TrainerService trainerService, WeekReportService weekReportService, CourseGroupService courseGroupService, CourseService courseService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.weekReportService = weekReportService;
        this.courseGroupService = courseGroupService;
        this.courseService = courseService;
    }

    @GetMapping("/trainer/home")
    public String getTrainerHome(ModelMap modelMap, Principal principal) {
        List<Trainee> traineeList = traineeService.getTraineesByGroupId(trainerService.getTrainerByUsername(principal.getName()).get().getGroupId());
        List<Trainee> missedDeadlineList = new ArrayList<>();
        List<Trainee> needToCompleteList = new ArrayList<>();
        List<Integer> courseDurationList = new ArrayList<>();
        List<Integer> traineeCounter = new ArrayList<>();
        int counter = 0;
        List<List<WeekReport>> traineeCompletedList = new ArrayList<>();


        Integer groupId = trainerService.getTrainerByUsername(principal.getName()).get().getGroupId();
        Integer courseId = courseGroupService.getGroupByID(groupId).get().getCourseId();
        Integer courseDuration = courseService.getGroupByID(courseId).get().getDuration();

        for(int i = 1; i <= courseDuration; i++){
            courseDurationList.add(i);
        }
        for (Trainee trainee: traineeList) {
            traineeCounter.add(counter);
            counter++;
            List<WeekReport> weekReports = weekReportService.getReportsByTraineeID(trainee.getTraineeId());
            traineeCompletedList.add(weekReports);
            Optional<WeekReport> weekReport = weekReportService.getCurrentWeekReportByTraineeID(trainee.getTraineeId());
            if(weekReport.isPresent()){
                if(weekReport.get().getDeadline().isBefore(LocalDateTime.now()) && weekReport.get().getTraineeSubmittedFlag() == 0){
                    missedDeadlineList.add(trainee);
                }

                if(weekReport.get().getTrainerCompletedFlag() == 0){
                    needToCompleteList.add(trainee);
                }
            }
        }

        Trainer trainer = trainerService.getTrainerByUsername(principal.getName()).get();
        CourseGroup courseGroup = courseGroupService.getGroupByID(trainer.getGroupId()).get();
        Course course = courseService.getCourseByID(courseGroup.getCourseId()).get();

        modelMap.addAttribute("trainer", trainer);
        modelMap.addAttribute("courseGroup",courseGroup);
        modelMap.addAttribute("course", course);
        modelMap.addAttribute("traineeList", traineeList);
        modelMap.addAttribute("missedDeadlineList", missedDeadlineList);
        modelMap.addAttribute("toCompleteList", needToCompleteList);
        modelMap.addAttribute("courseDuration", courseDuration);
        modelMap.addAttribute("courseDurationList", courseDurationList);
        modelMap.addAttribute("traineeCompletedList", traineeCompletedList);
        modelMap.addAttribute("traineeCounter", traineeCounter);
        return Pages.accessPage(Role.TRAINER, Pages.TRAINER_HOME_PAGE);
    }

    @GetMapping("/trainer/addTrainee")
    public String newUserForm(Model model) {
        model.addAttribute("newUserForm", new NewUserForm());
        model.addAttribute("allGroups", courseGroupService.getAllCourseGroups());
        model.addAttribute("allTrainees", traineeService.getAllTrainees());
        return Pages.accessPage(Role.TRAINER, Pages.TRAINER_NEW_USER_PAGE);
    }


}
