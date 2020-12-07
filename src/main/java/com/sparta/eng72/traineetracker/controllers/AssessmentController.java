package com.sparta.eng72.traineetracker.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.sparta.eng72.traineetracker.entities.Assessment;
import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.services.CodingGamesAPIService;
import com.sparta.eng72.traineetracker.services.CourseGroupService;
import com.sparta.eng72.traineetracker.services.TraineeService;
import com.sparta.eng72.traineetracker.services.TrainerService;
import com.sparta.eng72.traineetracker.utilities.Pages;
import com.sparta.eng72.traineetracker.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class AssessmentController {

    private CodingGamesAPIService codingGamesAPIService;
    private TraineeService traineeService;
    private TrainerService trainerService;
    private CourseGroupService courseGroupService;

    @Autowired
    public AssessmentController(CodingGamesAPIService codingGamesAPIService, TraineeService traineeService, TrainerService trainerService, CourseGroupService courseGroupService) {
        this.codingGamesAPIService = codingGamesAPIService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.courseGroupService = courseGroupService;

    }
  
    @GetMapping("/trainer/assessments")
    public ModelAndView getAllTrainees(ModelMap modelMap, Principal principal){
        List<Trainee> trainees = traineeService.getTraineesByGroupId(trainerService.getTrainerByUsername(principal.getName()).get().getGroupId());
        modelMap.addAttribute("trainees",trainees);
        ModelAndView  modelAndView = new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_ASSESSMENTS));
        modelAndView.addAllObjects(modelMap);
        return modelAndView;
    }

    @GetMapping("/trainer/assessments/{traineeId}")
    public ModelAndView getTrainerTraineeAssessments(@PathVariable Integer traineeId, ModelMap modelMap) {
        getTrainee(traineeId, modelMap);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/trainee/traineeAssessment");
        modelAndView.addAllObjects(modelMap);
        return modelAndView;
    }
  
    private void getTrainee(@PathVariable Integer traineeId, ModelMap modelMap) {
        Trainee trainee = traineeService.getTraineeByID(traineeId).get();
        String username = trainee.getUsername();
        List<JsonNode> assessments = codingGamesAPIService.getAllReportsByEmail(username);
        modelMap.addAttribute("codingGamesAPI", new CodingGamesAPIService());
        modelMap.addAttribute("traineeId", traineeId);
        modelMap.addAttribute("trainee", trainee);
        modelMap.addAttribute("assessments", assessments);
    }


}
