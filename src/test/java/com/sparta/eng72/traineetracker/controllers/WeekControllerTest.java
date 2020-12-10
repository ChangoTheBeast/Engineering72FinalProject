package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.entities.CourseGroup;
import com.sparta.eng72.traineetracker.repositories.CourseGroupRepository;
import com.sparta.eng72.traineetracker.services.CourseGroupService;
import com.sparta.eng72.traineetracker.services.TraineeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WeekControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TraineeService traineeService;

    @Autowired
    CourseGroupService courseGroupService;

    @Autowired
    CourseGroupRepository courseGroupRepository;

    @Test
    @WithMockUser(roles = "TRAINER")
    void getNewWeek() throws Exception {
        CourseGroup newClass = new CourseGroup();
        newClass.setGroupName("Engineering 74");
        newClass.setCourseId(1);
        newClass.setCurrentWeek(1);
        newClass.setEndDate(LocalDateTime.now().plusMonths(2));
        newClass.setStartDate(LocalDateTime.now());
        courseGroupService.saveNewGroup(newClass);

        newClass = courseGroupRepository.findCourseGroupByGroupName("Engineering 74");

        traineeService.changeTraineeCourseGroupByID(2, newClass.getGroupId());

        this.mockMvc.perform(post("/trainer/addNewWeek")
                .param("groupId", newClass.getGroupId().toString()))
                .andExpect(status().isOk())
                .andExpect(model()
                        .attributeExists("success"));

        newClass = courseGroupRepository.findCourseGroupByGroupName("Engineering 74");

        traineeService.changeTraineeCourseGroupByID(2, 1);

        courseGroupRepository.delete(newClass);

    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void newWeekPage() throws Exception {
        this.mockMvc.perform(get("/trainer/newWeek"))
                .andExpect(status().isOk())
                .andExpect(model()
                        .attributeExists("allGroups"));
    }
}