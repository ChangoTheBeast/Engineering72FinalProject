package com.sparta.eng68.traineetracker.interfaces;

import com.fasterxml.jackson.databind.JsonNode;
import com.sparta.eng68.traineetracker.entities.Assessment;
import com.sparta.eng68.traineetracker.entities.Trainee;

import java.util.List;

public interface CodingGamesAPI {
    List<JsonNode> getAllReportsByEmail();
    List<Assessment> getAllAssessmentsByEmail();
}
