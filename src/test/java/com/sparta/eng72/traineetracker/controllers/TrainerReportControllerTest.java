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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TrainerReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeekReportService weekReportService;

    private final String trainerName = "MGadhvi@sparta.com";
    private final String trainerPw = "startrek";
    private final String traineeName = "bbird@spartaglobal.com";
    private final String traineePw = "test";

    @Test
    @WithMockUser(username = trainerName, password = trainerPw, roles = "TRAINER")
    void postUpdateTrainerReport() throws Exception {

        WeekReport weekReport = weekReportService.getReportByID(1).get();

        this.mockMvc.perform(post("/trainer/updateReport")
                .param("reportId", weekReport.getReportId().toString())
                .param("trainerTechGrade", weekReport.getTechnicalGradeTrainer())
                .param("trainerConsulGrade", weekReport.getConsultantGradeTrainer())
                .param("trainerOverallGrade", weekReport.getOverallGradeTrainer())
                .param("trainerComments", "Test edit to the report")
                .param("stopTrainer", weekReport.getStopTrainer())
                .param("startTrainer", weekReport.getStartTrainer())
                .param("continueTrainer", weekReport.getContinueTrainer()))
                .andExpect(status().is3xxRedirection()).andExpect(model()
                .attributeExists("traineeId", "trainee", "reports", "now"));

        WeekReport updated = weekReportService.getReportByID(1).get();

        Assertions.assertEquals("Test edit to the report", updated.getTrainerComments());

        this.mockMvc.perform(post("/trainer/updateReport")
                .param("reportId", weekReport.getReportId().toString())
                .param("trainerTechGrade", weekReport.getTechnicalGradeTrainer())
                .param("trainerConsulGrade", weekReport.getConsultantGradeTrainer())
                .param("trainerOverallGrade", weekReport.getOverallGradeTrainer())
                .param("trainerComments", weekReport.getTrainerComments())
                .param("stopTrainer", weekReport.getStopTrainer())
                .param("startTrainer", weekReport.getStartTrainer())
                .param("continueTrainer", weekReport.getContinueTrainer()))
                .andExpect(status().is3xxRedirection()).andExpect(model()
                .attributeExists("traineeId", "trainee", "reports", "now"));

        updated = weekReportService.getReportByID(1).get();

        Assertions.assertNotEquals("Test edit to the report", updated.getTrainerComments());
    }

    @Test
    @WithMockUser(username = trainerName, password = trainerPw, roles = "TRAINER")
    void getTrainerFeedbackForm() throws Exception {
        this.mockMvc.perform(post("/trainer/reportTraineeWeekProcessing")
                .param("reportId", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = trainerName, password = trainerPw, roles = "TRAINER")
    void testGetTrainerFeedbackForm() throws Exception {
        this.mockMvc.perform(get("/trainer/report/41/1"))
                .andExpect(status().isOk())
                .andExpect(model()
                        .attributeExists("trainee", "trainerFeedback"));

        this.mockMvc.perform(get("/trainer/report/40/1"))
                .andExpect(status().isOk())
                .andExpect(model()
                        .attributeDoesNotExist("trainee", "trainerFeedback"));
    }

    @Test
    @WithMockUser(username = trainerName, password = trainerPw, roles = "TRAINER")
    void getTrainerWeeklyReports() throws Exception {
        this.mockMvc.perform(post("/trainer/viewTrainee")
                .param("btnStatus", "reports")
                .param("traineeId", "41"))
                .andExpect(status().is3xxRedirection());
    }


}