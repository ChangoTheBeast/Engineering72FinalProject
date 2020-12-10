package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.services.TraineeService;
import com.sparta.eng72.traineetracker.utilities.NewUserForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
class TraineeReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private TraineeService traineeService;

    private final String trainerName = "MGadhvi@sparta.com";
    private final String trainerPw = "startrek";
    private final String traineeName = "bbird@spartaglobal.com";
    private final String traineePw = "test";

    @Test
    @WithMockUser(username = traineeName, password = traineePw, roles = "TRAINEE")
    void getTraineeFeedbackFormTest() throws Exception {
        this.mockMvc.perform(get("/trainee/report/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("traineeFeedback"));


    }

    @Test
    @WithMockUser(username = "TKhan@sparta.com", password = "test", roles = "TRAINEE")
    void getNoTraineeFeedbackFormTest() throws Exception {
        this.mockMvc.perform(get("/trainee/report/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("traineeFeedback"));
    }

    @Test
    @WithMockUser(username = traineeName, password = traineePw, roles = "TRAINEE")
    void postRecentReportTest() throws Exception {
        this.mockMvc.perform(post("/traineeRecentReport"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("traineeFeedback"));
    }

    @Test
    @WithMockUser(username = "TKhan@sparta.com", password = "test", roles = "TRAINEE")
    void postNoRecentReportTest() throws Exception {
        this.mockMvc.perform(post("/traineeRecentReport"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeDoesNotExist("traineeFeedback"));
    }

    @Test
    @WithMockUser(username = traineeName, password = traineePw, roles = "TRAINEE")
    void getRecentReportTest() throws Exception {
        this.mockMvc.perform(get("/traineeRecentReport"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("traineeFeedback"));
    }

    @Test
    @WithMockUser(username = traineeName, password = traineePw, roles = "TRAINEE")
    void processReportRequestTest() throws Exception {
        this.mockMvc.perform(post("/traineeReportProcessing")
                .param("reportId", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void getTraineeWeeklyReportsTest() {
    }
}