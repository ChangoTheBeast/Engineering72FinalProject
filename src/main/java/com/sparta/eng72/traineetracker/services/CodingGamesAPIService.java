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
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CodingGamesAPIService implements CodingGamesAPI {

    public CodingGamesAPIService() {}
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
        String providedName = "lFarmer@spartaglobal.com";
        CodingGamesAPI codingGamesAPI = new CodingGamesAPIService();
        CodingGamesAPIService codingGamesAPIService = new CodingGamesAPIService();
        JsonNode testNode = codingGamesAPI.getAllReportsByEmail(providedName).get(0);
        System.out.println(codingGamesAPIService.getAssessmentGrade(codingGamesAPIService.getAssessmentScore(testNode)));
//        System.out.println(codingGamesAPIService.getAssessmentDesignScore(testNode));
//        System.out.println(codingGamesAPIService.getAssessmentLanguageKnowledgeScore(testNode));
//        System.out.println(codingGamesAPIService.getAssessmentProblemSolvingScore(testNode));
//        System.out.println(codingGamesAPI
//                .getAllReportsByEmail(providedName)
//                .get(0)
//                .get("report")
//                .get("technologies")
//                .fieldNames()
//                .next());
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

    public String getAssessmentName(JsonNode assessment) {
        return assessment.get("report").get("technologies").fieldNames().next();
    }

    public double getAssessmentScore(JsonNode assessment) {
        double score = assessment.get("report").get("score").asDouble();
        DecimalFormat df = new DecimalFormat("###.#");
        return Double.parseDouble(df.format(score));
    }

    public String getAssessmentGrade(int assessmentScore) {
        if(assessmentScore >= 95.8){
            return "A+";
        }else if(assessmentScore >= 91.6){
            return "A";
        }else if(assessmentScore >= 87.4){
            return "A-";
        }else if(assessmentScore >= 83.3){
            return "B+";
        }else if(assessmentScore >= 79.1){
            return "B";
        }else if(assessmentScore >= 75.0){
            return "B-";
        }else if(assessmentScore >= 70.8){
            return "C+";
        }else if(assessmentScore >= 66.6){
            return "C";
        }else if(assessmentScore >= 62.5){
            return "C-";
        }else if(assessmentScore >= 58.3){
            return "D+";
        }else if(assessmentScore >= 54.2){
            return "D";
        }else if(assessmentScore >= 50.0){
            return "D-";
        }
        else{
            return "FAIL";
        }
    }


    public String getAssessmentDate(JsonNode assessment) {
        long epoch = assessment.get("start_time").asLong();
        String date = new java.text.SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date (epoch));
        return date;
    }



    public int getAssessmentDuration(JsonNode assessment) {
        return assessment.get("report").get("total_duration").asInt();
    }

    public int getAssessmentComparativeScore(JsonNode assessment) {
        return assessment.get("report").get("comparative_score").asInt();
    }

    public int getAssessmentDesignScore(JsonNode assessment) {
        return assessment.get("report").get("technologies").get(getAssessmentName(assessment)).get("skills").get("Design").get("score").asInt();
    }

    public int getAssessmentLanguageKnowledgeScore(JsonNode assessment) {
        return assessment.get("report").get("technologies").get(getAssessmentName(assessment)).get("skills").get("Language knowledge").get("score").asInt();
    }

    public int getAssessmentProblemSolvingScore(JsonNode assessment) {
        return assessment.get("report").get("technologies").get(getAssessmentName(assessment)).get("skills").get("Problem solving").get("score").asInt();
    }



    public List<String> getAssessmentTags(JsonNode assessment) {

        Iterator<JsonNode> iterator = assessment.get("tags").iterator();
        List<String> tagsList = new ArrayList<>();
        while (iterator.hasNext()) {
            tagsList.add(iterator.next().asText());
        }
        return tagsList;
    }
}
