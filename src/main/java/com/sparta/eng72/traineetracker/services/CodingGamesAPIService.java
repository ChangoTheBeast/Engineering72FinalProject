package com.sparta.eng72.traineetracker.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.eng72.traineetracker.entities.Assessment;
import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.interfaces.CodingGamesAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CodingGamesAPIService implements CodingGamesAPI {

    private static JsonNode jsonNode = null;

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonNode = objectMapper.readTree(new File("src/main/resources/static/CodingGame.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    TraineeService traineeService;

    @Autowired
    public CodingGamesAPIService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    public static void main(String[] args) {
        String providedName = "jship@spartaglobal.com";
        for (int i = 0; i < jsonNode.get("tests").size(); i++){
            if(jsonNode.get("tests").get(i).get("candidate_email").asText().equals(providedName)) {
                System.out.println(jsonNode.get("tests").get(i));
            }
        }
    }

    public List<Integer> getAllTestIDs() {
        List<Trainee> trainees = traineeService.getAllTrainees();
        List<String> traineeEmails = new ArrayList<>();

        List<Integer> traineeIDs = new ArrayList<>();

        for (Trainee trainee : trainees) {
            traineeEmails.add(trainee.getUsername());
        }

        for (int i = 0; i < jsonNode.get("tests").size(); i++){
            if (traineeEmails.contains(jsonNode.get("tests").get(i).get("candidate_email").asText())) {
                traineeIDs.add(Integer.parseInt(jsonNode.get("tests").get(i).get("test_id").asText()));
            }
        }

        return traineeIDs;
    }

    @Override
    public List<JsonNode> getAllReportsByEmail(String email) {
        List<JsonNode> traineeAssessments = new ArrayList<>();

        for (int i = 0; i < jsonNode.get("tests").size(); i++) {
            if (email.equals(jsonNode.get("tests").get(i).get("candidate_email").asText())) {
                traineeAssessments.add(jsonNode.get("tests").get(i));
            }
        }
        return traineeAssessments;
    }

    //TODO If needed; When we get API access
    @Override
    public List<Assessment> getAllAssessmentsByEmail(String email) {
        return null;
    }
}
