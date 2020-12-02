package com.sparta.eng72.traineetracker;


import com.sparta.eng72.traineetracker.controllers.AssessmentController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AssessmentControllerTest {

    @Autowired
    private AssessmentController assessmentController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "mgadhvi@sparta.com", password = "pwd",roles = "TRAINER")
    public void adminUser() throws Exception {
        mockMvc.perform(get("/trainer/assessments")).andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists());
    }

    @Test
    @WithMockUser(username = "mgadhvi@sparta.com", password = "pwd",roles = "TRAINER")
    public void adminUser1() throws Exception {
        mockMvc.perform(get("/trainer/assessments/2")).andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists("traineeId","trainee","assessments"));
    }




}
