package com.sparta.eng72.traineetracker.controllers;

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
public class TraineeProfileControllerTests {

    private final String trainerName = "MGadhvi@sparta.com";
    private final String trainerPw = "startrek";
    private final String traineeName = "bbird@spartaglobal.com";
    private final String traineePw = "test";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username =traineeName , password = traineePw,roles = "TRAINEE")
    public void traineeGetTraineeProfileTest() throws Exception {
        this.mockMvc.perform(get("/trainee/traineeProfile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(
                        "trainee",
                        "courseGroup",
                        "course",
                        "codingGamesAPI",
                        "traineeId",
                        "assessments",
                        "attendanceReports",
                        "onTimePercentage",
                        "latePercentage",
                        "excusedPercentage",
                        "unexcusedPercentage",
                        "currentWeek",
                        "dateFormat",
                        "now",
                        "reports",
                        "previousWeekReport"
                )
        );
    }

    @Test
    @WithMockUser(username = trainerName, password = trainerPw, roles = "TRAINER")
    public void getTraineeProfileTest() throws Exception {
        this.mockMvc.perform(post("/trainer/viewTrainee").param("btnStatus", "profile")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = trainerName, password = trainerPw, roles = "TRAINER")
    public void trainerGetTraineeProfileTest() throws Exception {
        this.mockMvc.perform(get("/trainer/traineeProfile/41")).andExpect(status().isOk())
                .andExpect(model().attributeExists(
                        "trainee",
                        "courseGroup",
                        "course",
                        "codingGamesAPI",
                        "traineeId",
                        "assessments",
                        "attendanceReports",
                        "onTimePercentage",
                        "latePercentage",
                        "excusedPercentage",
                        "unexcusedPercentage",
                        "currentWeek",
                        "dateFormat",
                        "now",
                        "reports",
                        "previousWeekReport"
                )
        );
    }

    @Test
    public void getTraineeTest() {

    }
}
