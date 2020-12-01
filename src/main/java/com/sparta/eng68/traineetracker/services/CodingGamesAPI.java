package com.sparta.eng68.traineetracker.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class CodingGamesAPI {

    TraineeService traineeService;

    @Autowired
    public CodingGamesAPI(TraineeService traineeService) {
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

    public void getAllTestIDs() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(new File("src/main/resources/static/CodingGame.json"));
            for (int i = 0; i < jsonNode.get("tests").size(); i++){
                if (traineeService.getAllEmails.contains(jsonNode.get("tests").get(i).get("candidate_email"))) {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonNode getAssessmentByCandidateEmail(String email) {
        return null;
    }
}
