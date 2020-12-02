package com.sparta.eng72.traineetracker.interfaces;

import com.fasterxml.jackson.databind.JsonNode;
import com.sparta.eng72.traineetracker.entities.Assessment;
import com.sparta.eng72.traineetracker.entities.Trainee;

import java.util.List;

public interface CodingGamesAPI {
    List<JsonNode> getAllReportsByEmail(String email);
    List<Assessment> getAllAssessmentsByEmail(String email);
}
