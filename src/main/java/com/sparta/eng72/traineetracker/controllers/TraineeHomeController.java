package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.entities.Course;
import com.sparta.eng72.traineetracker.entities.CourseGroup;
import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.entities.WeekReport;
import com.sparta.eng72.traineetracker.services.*;
import com.sparta.eng72.traineetracker.utilities.Pages;
import com.sparta.eng72.traineetracker.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class TraineeHomeController {

    private final WeekReportService weekReportService;
    private final TraineeService traineeService;
    private final CourseGroupService courseGroupService;
    private final CourseService courseService;

    @Autowired
    public TraineeHomeController(WeekReportService weekReportService, TraineeService traineeService, CourseGroupService courseGroupService, CourseService courseService) {
        this.weekReportService = weekReportService;
        this.traineeService = traineeService;
        this.courseGroupService = courseGroupService;
        this.courseService = courseService;
    }

    @GetMapping("/trainee/home")
    public String getTrainerForTraineeHomeGrades(Model model, Principal principal) {
        Trainee trainee = traineeService.getTraineeByUsername(principal.getName()).get();
        CourseGroup courseGroup = courseGroupService.getGroupByID(trainee.getGroupId()).get();
        Course course = courseService.getCourseByID(courseGroup.getCourseId()).get();
        Optional<WeekReport> reports = weekReportService.getPreviousWeekReportByTraineeID(trainee.getTraineeId());
        if (reports.isPresent()){
            model.addAttribute("report",reports.get());
        }
        else {
            model.addAttribute("report",null);
        }
        model.addAttribute("trainee", trainee);
        model.addAttribute("courseGroup",courseGroup);
        model.addAttribute("course", course);
        model.addAttribute("now", LocalDateTime.now());
        return Pages.accessPage(Role.TRAINEE, Pages.TRAINEE_HOME_PAGE);
    }



    @PostMapping("/trainee/updateReport")
    public String submitTraineeFeedbackForm(Integer reportId, String stopTrainee, String startTrainee,
                                            String continueTrainee, String traineeConsulGrade,
                                            String traineeTechGrade){

        WeekReport weekReport = weekReportService.getWeekReportByReportId(reportId).get();
        weekReport.setStopTrainee(stopTrainee);
        weekReport.setStartTrainee(startTrainee);
        weekReport.setContinueTrainee(continueTrainee);
        weekReport.setTechnicalGradeTrainee(traineeTechGrade);
        weekReport.setConsultantGradeTrainee(traineeConsulGrade);
        weekReport.setTraineeConsultantGradeFlag((byte) 1);
        weekReport.setTraineeTechnicalGradeFlag((byte) 1);
        weekReport.setTraineeStartFlag((byte) 1);
        weekReport.setTraineeStopFlag((byte) 1);
        weekReport.setTraineeContinueFlag((byte) 1);
        weekReport.setMostRecentEdit(LocalDateTime.now());

        weekReportService.updateWeekReport(weekReport);
        return Pages.accessPage(Role.TRAINEE, "redirect:"+Pages.TRAINEE_HOME_URL);
    }

}
