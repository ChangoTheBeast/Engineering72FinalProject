package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.entities.Course;
import com.sparta.eng72.traineetracker.entities.CourseGroup;
import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.services.CourseGroupService;
import com.sparta.eng72.traineetracker.services.CourseService;
import com.sparta.eng72.traineetracker.services.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;

@Controller
public class TraineeProfileController {

    TraineeService traineeService;
    CourseGroupService courseGroupService;
    CourseService courseService;
    AssessmentController assessmentController;
    AttendanceController attendanceController;
    TraineeReportController traineeReportController;
    TrainerReportController trainerReportController;
    TraineeHomeController traineeHomeController;

    @Autowired
    public TraineeProfileController(TraineeService traineeService, CourseGroupService courseGroupService, CourseService courseService, AssessmentController assessmentController, AttendanceController attendanceController, TraineeReportController traineeReportController, TrainerReportController trainerReportController, TraineeHomeController traineeHomeController) {
        this.traineeService = traineeService;
        this.courseGroupService = courseGroupService;
        this.courseService = courseService;
        this.assessmentController = assessmentController;
        this.attendanceController = attendanceController;
        this.traineeReportController = traineeReportController;
        this.trainerReportController = trainerReportController;
        this.traineeHomeController = traineeHomeController;
    }

    @GetMapping("/trainee/traineeProfile")
    public String getTraineeProfile(ModelMap modelMap, Principal principal) {
        Trainee trainee = getTrainee(principal);
        CourseGroup courseGroup = getCourseGroup(trainee);
        Course course = getCourse(courseGroup);

        /**
         * CCFCP - Controller Controller Factory Controller Pattern
         * Send modelMap to add attributes of the traineeReportController in its @GetMapping method
         */

        ModelAndView assessmentModelAndView = assessmentController.getTrainerTraineeAssessments(trainee.getTraineeId(), modelMap);
        ModelAndView attendanceModelAndView = attendanceController.getTraineeAttendance(modelMap, principal);
        ModelAndView attendancePercentages = attendanceController.getTraineeAttendancePercentage(principal, modelMap);
        ModelAndView previousWeekReportModelAndView = traineeHomeController.getTrainerForTraineeHomeGrades(modelMap, principal);
        ModelAndView weeklyReportsModelAndView = traineeReportController.getTraineeWeeklyReports(modelMap, principal);

        modelMap.mergeAttributes(assessmentModelAndView.getModelMap());
        modelMap.mergeAttributes(attendanceModelAndView.getModelMap());
        modelMap.mergeAttributes(attendancePercentages.getModelMap());
        modelMap.mergeAttributes(previousWeekReportModelAndView.getModelMap());
        modelMap.mergeAttributes(weeklyReportsModelAndView.getModelMap());

        modelMap.addAttribute("trainee", trainee);
        modelMap.addAttribute("courseGroup", courseGroup);
        modelMap.addAttribute("course", course);

        return "trainee/traineeProfile";
//        return Pages.accessPage(Role.TRAINEE, Pages.TRAINEE_PROFILE_PAGE);
    }



    @RequestMapping(value="/trainer/viewTrainee", method= RequestMethod.POST, params="btnStatus=profile")
    public String getTraineeAttendance(Integer traineeId) {
        return "redirect:/trainer/traineeProfile/"+traineeId;
    }

    @GetMapping("/trainer/traineeProfile/{traineeId}")
    public String getTraineeProfile(ModelMap modelMap, @PathVariable Integer traineeId) {

        Trainee trainee = getTrainee(traineeId);
        CourseGroup courseGroup = getCourseGroup(trainee);
        Course course = getCourse(courseGroup);

        /**
         * CCFCP - Controller Controller Factory Controller Pattern
         * Send modelMap to add attributes of the traineeReportController in its @GetMapping method
         */

        ModelAndView assessmentModelAndView = assessmentController.getTrainerTraineeAssessments(traineeId, modelMap);
        ModelAndView attendanceModelAndView = attendanceController.getTraineeAttendanceWithPath(traineeId, modelMap);
        ModelAndView attendancePercentages = attendanceController.getTraineeAttendancePercentage(traineeId, modelMap);
        ModelAndView previousWeekReportModelAndView = traineeHomeController.getTrainerForTraineeHomeGrades(traineeId, modelMap);
        ModelAndView weeklyReportsModelAndView = trainerReportController.getTrainerWeeklyReportsWithPath(traineeId, modelMap);

        modelMap.mergeAttributes(assessmentModelAndView.getModelMap());
        modelMap.mergeAttributes(attendanceModelAndView.getModelMap());
        modelMap.mergeAttributes(attendancePercentages.getModelMap());
        modelMap.mergeAttributes(previousWeekReportModelAndView.getModelMap());
        modelMap.mergeAttributes(weeklyReportsModelAndView.getModelMap());

        modelMap.addAttribute("trainee", trainee);
        modelMap.addAttribute("courseGroup", courseGroup);
        modelMap.addAttribute("course", course);

        return "trainee/traineeProfile";
//        return Pages.accessPage(Role.TRAINEE, Pages.TRAINEE_PROFILE_PAGE);
    }

    //Get Trainee as "TRAINEE"
    private Trainee getTrainee(Principal principal) {
        Trainee trainee = null;
        if (traineeService.getTraineeByUsername(principal.getName()).isPresent()){
            trainee = traineeService.getTraineeByUsername(principal.getName()).get();
        }
        return trainee;
    }

    //Get Trainee as "TRAINER"
    private Trainee getTrainee(Integer traineeId) {
        Trainee trainee = null;
        if (traineeService.getTraineeByID(traineeId).isPresent()){
            trainee = traineeService.getTraineeByID(traineeId).get();
        }
        return trainee;
    }

    private CourseGroup getCourseGroup(Trainee trainee) {
        CourseGroup courseGroup = null;
        if (courseGroupService.getGroupByID(trainee != null ? trainee.getGroupId() : null).isPresent()) {
            courseGroup = courseGroupService.getGroupByID(trainee != null ? trainee.getGroupId() : null).get();
        }
        return courseGroup;
    }

    private Course getCourse(CourseGroup courseGroup) {
        Course course = null;
        if (courseService.getCourseByID(courseGroup != null ? courseGroup.getCourseId() : null).isPresent()) {
            course = courseService.getCourseByID(courseGroup != null ? courseGroup.getCourseId() : null).get();
        }
        return course;
    }

}
