package com.sparta.eng72.traineetracker.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ViewControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(roles = "TRAINEE")
    void getConsultancySkills() throws Exception {
        this.mockMvc.perform(get("/consultancy")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "TRAINEE")
    void getTraineeGuide() throws Exception {
        this.mockMvc.perform(get("/guide")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "TRAINEE")
    void get404() throws Exception {
        this.mockMvc.perform(get("/pagenotfounderror")).andExpect(status().isOk());
    }
}