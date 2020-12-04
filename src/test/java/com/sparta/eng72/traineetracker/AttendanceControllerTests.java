package com.sparta.eng72.traineetracker;

import com.sparta.eng72.traineetracker.controllers.AttendanceController;
import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.entities.Trainer;
import com.sparta.eng72.traineetracker.services.TraineeService;
import com.sparta.eng72.traineetracker.services.TrainerService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AttendanceControllerTests {
    @Autowired
    private AttendanceController attendanceController;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private MockMvc mockMvc;

    private final String trainerName = "MGadhvi@sparta.com";
    private final String trainerPw = "startrek";
    private final String traineeName = "";
    private final String traineePw = "";


    @Test
    @WithMockUser(username =trainerName , password = trainerPw,roles = "TRAINER")
    public void getTraineeListTest() throws Exception{
        Optional<Trainer> trainer = trainerService.getTrainerByUsername("MGadhvi@sparta.com");
        List<Trainee> trainees = traineeService.getTraineesByGroupId(trainer.get().getGroupId());
        this.mockMvc.perform(get("/trainer/home")).andDo(print()).andExpect(status().isOk()).andExpect(model().attribute(
                "traineeList",trainees));
    }

    @Test
    @WithMockUser(username =trainerName , password = trainerPw,roles = "TRAINER")
    public void displayAttendanceTest() throws Exception {
        this.mockMvc.perform(get("/trainer/traineeAttendance/5")).andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists(
                "attendance","trainee","traineeId"));
    }

    @Test
    @WithMockUser(username =trainerName , password = trainerPw,roles = "TRAINER")
    public void getCorrectTraineeTest() throws Exception {
        this.mockMvc.perform(get("/trainer/traineeAttendance/5")).andDo(print()).andExpect(status().isOk()).andExpect(model().attribute(
                "traineeId",5));
    }

    @Test
    @WithMockUser(username =trainerName , password = trainerPw,roles = "TRAINER")
    public void attendanceEntryTest() throws Exception {
        this.mockMvc.perform(get("/trainer/attendanceEntry")).andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists(
                "date","trainee"));
    }

    @Test
    @WithMockUser(username =traineeName , password = traineePw,roles = "TRAINEE")
    public void traineeViewAttendanceTest() throws Exception {
        this.mockMvc.perform(get("/trainee/traineeAttendance/5")).andDo(print()).andExpect(status().isOk()).andExpect(model().attribute(
                "traineeId",5));
    }


}
