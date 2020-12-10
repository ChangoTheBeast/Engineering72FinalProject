package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.entities.WeekReport;
import com.sparta.eng72.traineetracker.services.WeekReportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TraineeHomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WeekReportService weekReportService;

    @Test
    @WithMockUser(roles = "TRAINEE")
    void submitTraineeFeedbackForm() throws Exception {
        WeekReport weekReport = weekReportService.getReportByID(1).get();
        this.mockMvc.perform(post("/trainee/updateReport")
                .param("reportId", weekReport.getReportId().toString())
                .param("stopTrainee", "Test Stop Edit")
                .param("startTrainee", "Test Start Edit")
                .param("continueTrainee", "Test Continue Edit")
                .param("traineeConsulGrade", weekReport.getConsultantGradeTrainee())
                .param("traineeTechGrade", weekReport.getTechnicalGradeTrainee()))
                .andExpect(status().is3xxRedirection());

        WeekReport newReport = weekReportService.getReportByID(1).get();

        Assertions.assertEquals("Test Stop Edit", newReport.getStopTrainee());
        Assertions.assertEquals("Test Start Edit", newReport.getStartTrainee());
        Assertions.assertEquals("Test Continue Edit", newReport.getContinueTrainee());

        this.mockMvc.perform(post("/trainee/updateReport")
                .param("reportId", newReport.getReportId().toString())
                .param("stopTrainee", weekReport.getStopTrainee())
                .param("startTrainee", weekReport.getStartTrainee())
                .param("continueTrainee", weekReport.getContinueTrainee())
                .param("traineeConsulGrade", newReport.getConsultantGradeTrainee())
                .param("traineeTechGrade", newReport.getTechnicalGradeTrainee()))
                .andExpect(status().is3xxRedirection());

        newReport = weekReportService.getReportByID(1).get();

        Assertions.assertNotEquals("Test Stop Edit", newReport.getStopTrainee());
        Assertions.assertNotEquals("Test Start Edit", newReport.getStartTrainee());
        Assertions.assertNotEquals("Test Continue Edit", newReport.getContinueTrainee());
    }
}