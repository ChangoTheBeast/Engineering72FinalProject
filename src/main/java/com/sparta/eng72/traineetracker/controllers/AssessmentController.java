package com.sparta.eng72.traineetracker.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.entities.Trainer;
import com.sparta.eng72.traineetracker.services.CodingGamesAPIService;
import com.sparta.eng72.traineetracker.services.CourseGroupService;
import com.sparta.eng72.traineetracker.services.TraineeService;
import com.sparta.eng72.traineetracker.services.TrainerService;
import com.sparta.eng72.traineetracker.utilities.Pages;
import com.sparta.eng72.traineetracker.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class AssessmentController {

    private final CodingGamesAPIService codingGamesAPIService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Autowired
    public AssessmentController(CodingGamesAPIService codingGamesAPIService, TraineeService traineeService, TrainerService trainerService) {
        this.codingGamesAPIService = codingGamesAPIService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }
  
    @GetMapping("/trainer/assessments")
    public ModelAndView getAllTrainees(ModelMap modelMap, Principal principal){
        List<Trainee> trainees = traineeService.getTraineesByGroupId(getTrainer(principal).getGroupId());
        modelMap.addAttribute("trainees", trainees);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_ASSESSMENTS), modelMap);
    }

    @GetMapping("/trainer/assessments/{traineeId}")
    public ModelAndView getTrainerTraineeAssessments(@PathVariable Integer traineeId, ModelMap modelMap) {
        Trainee trainee = getTrainee(traineeId);
        List<JsonNode> assessments = codingGamesAPIService.getAllReportsByEmail(trainee.getUsername());

        getTraineeAssessmentsModelMap(traineeId, modelMap, trainee, assessments);
        return new ModelAndView(Pages.TRAINEE_ASSESSMENTS, modelMap);
    }

    private void getTraineeAssessmentsModelMap(Integer traineeId, ModelMap modelMap, Trainee trainee, List<JsonNode> assessments) {
        modelMap.addAttribute("codingGamesAPI", new CodingGamesAPIService());
        modelMap.addAttribute("traineeId", traineeId);
        modelMap.addAttribute("trainee", trainee);
        modelMap.addAttribute("assessments", assessments);
    }

    private Trainee getTrainee(Integer traineeId) {
        Trainee trainee = null;
        if(traineeService.getTraineeByID(traineeId).isPresent()){
            trainee = traineeService.getTraineeByID(traineeId).get();
        }
        return trainee;
    }

    private Trainer getTrainer(Principal principal) {
        Trainer trainer = null;
        if(trainerService.getTrainerByUsername(principal.getName()).isPresent()){
            trainer = trainerService.getTrainerByUsername(principal.getName()).get();
        }
        return trainer;
    }

}
