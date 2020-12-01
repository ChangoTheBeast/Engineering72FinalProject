package com.sparta.eng68.traineetracker.controllers;

import com.sparta.eng68.traineetracker.entities.Assessment;
import com.sparta.eng68.traineetracker.entities.Trainee;
import com.sparta.eng68.traineetracker.services.CodingGamesAPIService;
import com.sparta.eng68.traineetracker.services.TraineeService;
import com.sparta.eng68.traineetracker.utilities.Pages;
import com.sparta.eng68.traineetracker.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Controller
public class AssessmentController {

    private CodingGamesAPIService codingGamesAPIService;
    private TraineeService traineeService;

    @Autowired
    public AssessmentController(CodingGamesAPIService codingGamesAPIService, TraineeService traineeService) {
        this.codingGamesAPIService = codingGamesAPIService;
        this.traineeService = traineeService;
    }




    @GetMapping("/trainer/assessments/{traineeId}")
    public String getTraineeAssessments(@PathVariable Integer traineeId, Model model) {
        Trainee trainee = traineeService.getTraineeByID(traineeId).get();
        List<Assessment> assessments = codingGamesAPIService.getAllAssessmentsByEmail(traineeService.getTraineeByID(traineeId).get().getUsername());
        model.addAttribute("traineeId", traineeId);
        model.addAttribute("trainee", trainee);
        model.addAttribute("assessments", assessments);
        return Pages.accessPage(Role.TRAINER, Pages.TRAINER_ASSESSMENTS);

    }


}
