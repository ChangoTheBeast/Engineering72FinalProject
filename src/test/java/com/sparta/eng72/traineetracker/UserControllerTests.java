package com.sparta.eng72.traineetracker;

import com.sparta.eng72.traineetracker.controllers.UserController;
import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.entities.TraineeAttendance;
import com.sparta.eng72.traineetracker.entities.User;
import com.sparta.eng72.traineetracker.services.AttendanceService;
import com.sparta.eng72.traineetracker.services.TraineeService;
import com.sparta.eng72.traineetracker.services.UserService;
import com.sparta.eng72.traineetracker.utilities.NewUserForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private final String trainerName = "MGadhvi@sparta.com";
    private final String trainerPw = "startrek";
    private final String traineeName = "bbird@spartaglobal.com";
    private final String traineePw = "test";

    @Test
    @WithMockUser(roles = "TRAINER")
    public void addUserTest() throws Exception {

        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setEmail("unit@test.com");
        newUserForm.setFirstName("Unit");
        newUserForm.setLastName("Test");
        newUserForm.setGroupId(2);
        ModelMap modelMap = new ModelMap();

        this.mockMvc.perform(post("/trainer/addNewUser")
                .param("firstName", newUserForm.getFirstName())
                .param("email", newUserForm.getEmail())
                .param("lastName", newUserForm.getLastName())
                .param("groupId", newUserForm.getGroupId().toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trainee"));

        Assertions.assertNotEquals(null,traineeService.getTraineeByUsername("unit@test.com"));

        this.mockMvc.perform(post("/trainer/addNewUser")
                .param("firstName", newUserForm.getFirstName())
                .param("email", newUserForm.getEmail())
                .param("lastName", newUserForm.getLastName())
                .param("groupId", newUserForm.getGroupId().toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("email"));

        Trainee trainee = new Trainee();
        trainee = traineeService.getTraineeByUsername("unit@test.com").get();
        userController.deleteTrainee(String.valueOf(trainee.getTraineeId()),modelMap);
    }

    @Test
    public void setDefaultAttendanceTest() {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setEmail("unit@test.com");
        newUserForm.setFirstName("Unit");
        newUserForm.setLastName("Test");
        newUserForm.setGroupId(2);
        ModelMap modelMap = new ModelMap();
        userController.addNewUser(newUserForm, modelMap);

        Trainee trainee = new Trainee();
        List<TraineeAttendance> traineeAttendanceList = new ArrayList();
        trainee = traineeService.getTraineeByUsername("unit@test.com").get();
        traineeAttendanceList = attendanceService.getTraineeAttendanceByTraineeId(trainee.getTraineeId());
        Assertions.assertEquals(5,traineeAttendanceList.get(10).getAttendanceId());
        userController.deleteTrainee(String.valueOf(trainee.getTraineeId()),modelMap);
    }

    @Test
    @WithMockUser(username =traineeName , password = traineePw,roles = "TRAINEE")
    public void getPasswordInitialiserTest() throws Exception {
        this.mockMvc.perform(get("/trainee/tempPassword")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "unit@test.com", password = "pwd", roles = "TRAINEE")
    public void changePasswordForNewUserTest() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setEmail("unit@test.com");
        newUserForm.setFirstName("Unit");
        newUserForm.setLastName("Test");
        newUserForm.setGroupId(2);
        ModelMap modelMap = new ModelMap();
        userController.addNewUser(newUserForm, modelMap);

        this.mockMvc.perform(post("/addFirstPassword").param("password", "test_password")).andExpect(status().isOk());

        Trainee trainee = traineeService.getTraineeByUsername("unit@test.com").get();
        userController.deleteTrainee(trainee.getTraineeId().toString(), modelMap);

    }

    @Test
    public void getChangePasswordScreenTest() throws Exception {
        this.mockMvc.perform(get("/changePassword").param("error", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("showError"));
    }
}
