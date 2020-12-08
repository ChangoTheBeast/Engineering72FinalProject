package com.sparta.eng72.traineetracker.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainerHomeControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "TRAINER")
    public void newUserFormTest() throws Exception {
        this.mockMvc.perform(get("/trainer/addTrainee"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("newUserForm", "allGroups", "allTrainees"));
    }

}
