package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.entities.CourseGroup;
import com.sparta.eng72.traineetracker.repositories.CourseGroupRepository;
import com.sparta.eng72.traineetracker.services.CourseGroupService;
import com.sparta.eng72.traineetracker.services.TraineeService;
import com.sparta.eng72.traineetracker.utilities.AssignGroupForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ManagementControllerTest {

    @Autowired
    private CourseGroupService courseGroupService;

    @Autowired
    private CourseGroupRepository courseGroupRepository;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private MockMvc mockMvc;

    private final String trainerName = "MGadhvi@sparta.com";
    private final String trainerPw = "startrek";
    private final String traineeName = "bbird@spartaglobal.com";
    private final String traineePw = "test";


    @Test
    @WithMockUser(username = trainerName, password = trainerPw, roles = "TRAINER")
    public void getTrainerGroupViewTest() throws Exception {
        this.mockMvc.perform(get("/trainer/group"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(
                        "assignGroupForm",
                        "allTrainees",
                        "allGroups",
                        "allCourses",
                        "newClass"));
    }

    @Test
    @WithMockUser(username = trainerName, password = trainerPw, roles = "TRAINER")
    public void createGroupTest() throws Exception {
        CourseGroup newClass = new CourseGroup();
        newClass.setGroupName("Engineering 72");
        newClass.setCourseId(1);
        newClass.setCurrentWeek(1);
        newClass.setEndDate(LocalDateTime.now());
        newClass.setStartDate(LocalDateTime.now());

        this.mockMvc.perform(post("/trainer/createGroup")
                .param("groupName", newClass.getGroupName())
                .param("courseId", newClass.getCourseId().toString())
                .param("currentWeek", newClass.getCurrentWeek().toString())
                .param("startDate", newClass.getStartDate().toString())
                .param("endDate", newClass.getEndDate().toString())).andExpect(status().isOk());

        newClass.setGroupName("Engineering 74");

        this.mockMvc.perform(post("/trainer/createGroup")
                .param("groupName", newClass.getGroupName())
                .param("courseId", newClass.getCourseId().toString())
                .param("currentWeek", newClass.getCurrentWeek().toString())
                .param("startDate", newClass.getStartDate().toString())
                .param("endDate", newClass.getEndDate().toString())).andExpect(status().isOk());

        CourseGroup courseGroup = courseGroupRepository.findCourseGroupByGroupName("Engineering 74");

        Assertions.assertNotNull(courseGroup);

        courseGroupRepository.delete(courseGroup);
    }

    @Test
    @WithMockUser(username = trainerName, password = trainerPw, roles = "TRAINER")
    public void getGroupChangeTest() throws Exception {
        this.mockMvc.perform(get("/trainer/manageClass"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allTrainees", "allGroups", "assignGroupForm", "allCourses", "newClass"));
    }

    @Test
    @WithMockUser(username = trainerName, password = trainerPw, roles = "TRAINER")
    public void postGroupChangeTest() throws Exception {
        AssignGroupForm assignGroupForm = new AssignGroupForm();
        assignGroupForm.setGroupId(3);
        assignGroupForm.setTraineeId(2);

        this.mockMvc.perform(post("/trainer/modifyGroup")
                .param("groupId", assignGroupForm.getGroupId().toString())
                .param("traineeId", assignGroupForm.getTraineeId().toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("modifySuccess"));

        Assertions.assertEquals(3, traineeService.getTraineeByID(2).get().getGroupId());

        traineeService.changeTraineeCourseGroupByID(2, 1);
    }

}
