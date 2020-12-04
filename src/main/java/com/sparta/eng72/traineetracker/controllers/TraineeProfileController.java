package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.entities.Course;
import com.sparta.eng72.traineetracker.entities.CourseGroup;
import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.services.CourseGroupService;
import com.sparta.eng72.traineetracker.services.CourseService;
import com.sparta.eng72.traineetracker.services.TraineeService;
import com.sparta.eng72.traineetracker.utilities.Pages;
import com.sparta.eng72.traineetracker.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class TraineeProfileController {

    TraineeService traineeService;
    CourseGroupService courseGroupService;
    CourseService courseService;
    AssessmentController assessmentController;
    TraineeReportController traineeReportController;

    @Autowired
    public TraineeProfileController(TraineeService traineeService, CourseGroupService courseGroupService, CourseService courseService, AssessmentController assessmentController, TraineeReportController traineeReportController) {
        this.traineeService = traineeService;
        this.courseGroupService = courseGroupService;
        this.courseService = courseService;
        this.assessmentController = assessmentController;
        this.traineeReportController = traineeReportController;
    }

    @GetMapping("/trainee/traineeProfile")
    public String getTrainerHome(ModelMap modelMap, Principal principal) {
        Trainee trainee = null;
        CourseGroup courseGroup = null;
        Course course = null;

        if (traineeService.getTraineeByUsername(principal.getName()).isPresent()){
            trainee = traineeService.getTraineeByUsername(principal.getName()).get();
        }

        if (courseGroupService.getGroupByID(trainee != null ? trainee.getGroupId() : null).isPresent()){
            courseGroup = courseGroupService.getGroupByID(trainee != null ? trainee.getGroupId() : null).get();
        }

        if(courseService.getCourseByID(courseGroup != null ? courseGroup.getCourseId() : null).isPresent()){
            course = courseService.getCourseByID(courseGroup != null ? courseGroup.getCourseId() : null).get();
        }


        /**
         * CCFCP - Controller Controller Factory Controller Pattern
         * Send modelMap to add attributes of the traineeReportController in its @GetMapping method
         * */

        modelMap = traineeReportController.getTraineeWeeklyReports(modelMap, principal);
        modelMap = assessmentController.getTrainerTraineeAssessments(trainee.getTraineeId(), modelMap);

        modelMap.addAttribute("trainee", trainee);
        modelMap.addAttribute("courseGroup", courseGroup);
        modelMap.addAttribute("course", course);

        return "trainee/traineeProfile";
//        return Pages.accessPage(Role.TRAINEE, Pages.TRAINEE_PROFILE_PAGE);
    }
}
