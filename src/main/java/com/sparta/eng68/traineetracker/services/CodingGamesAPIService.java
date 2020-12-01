package com.sparta.eng68.traineetracker.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.eng68.traineetracker.entities.Assessment;
import com.sparta.eng68.traineetracker.entities.Trainee;
import com.sparta.eng68.traineetracker.interfaces.CodingGamesAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CodingGamesAPIService implements CodingGamesAPI {

    TraineeService traineeService;

    @Autowired
    public CodingGamesAPIService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;

        String providedName = "jship@spartaglobal.com";

        try {
            jsonNode = objectMapper.readTree(new File("src/main/resources/static/CodingGame.json"));
            for (int i = 0; i < jsonNode.get("tests").size(); i++){
                if(jsonNode.get("tests").get(i).get("candidate_email").asText().equals(providedName)) {
                    System.out.println(jsonNode.get("tests").get(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getAllTestIDs() {
        List<Trainee> trainees = traineeService.getAllTrainees();
        List<String> traineeEmails = new ArrayList<>();

        List<Integer> traineeIDs = new ArrayList<>();

        for (Trainee trainee : trainees) {
            traineeEmails.add(trainee.getUsername());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(new File("src/main/resources/static/CodingGame.json"));
            for (int i = 0; i < jsonNode.get("tests").size(); i++){
                if (traineeEmails.contains(jsonNode.get("tests").get(i).get("candidate_email").asText())) {
                    traineeIDs.add(Integer.parseInt(jsonNode.get("tests").get(i).get("test_id").asText()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return traineeIDs;
    }

    public JsonNode getAssessmentByCandidateEmail(String email) {
        return null;
    }

    @Override
    public List<JsonNode> getAllReportsByEmail() {
        return null;
    }

    @Override
    public List<Assessment> getAllAssessmentsByEmail() {
        return null;
    }
}
