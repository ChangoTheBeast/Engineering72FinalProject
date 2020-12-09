package com.sparta.eng72.traineetracker.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loginTest() throws Exception {
        this.mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(model().attributeExists("showError"));
    }

    @Test
    public void loginFailureTest() throws Exception {
        this.mockMvc.perform(get("/loginFailure")).andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("loginResult"));
    }

    @Test
    public void postLoginFailureTest() throws Exception {
        this.mockMvc.perform(post("/loginFailure")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void redirectToLoginTest() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().is3xxRedirection());
    }
}
