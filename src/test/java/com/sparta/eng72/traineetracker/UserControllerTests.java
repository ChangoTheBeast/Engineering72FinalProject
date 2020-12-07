package com.sparta.eng72.traineetracker;

import com.sparta.eng72.traineetracker.controllers.UserController;
import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.entities.TraineeAttendance;
import com.sparta.eng72.traineetracker.services.AttendanceService;
import com.sparta.eng72.traineetracker.services.TraineeService;
import com.sparta.eng72.traineetracker.utilities.NewUserForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ModelMap;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserController userController;

    @Test
    public void addUserTest(){
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setEmail("unit@test.com");
        newUserForm.setFirstName("Unit");
        newUserForm.setLastName("Test");
        newUserForm.setGroupId(2);
        ModelMap modelMap = new ModelMap();
        userController.addNewUser(newUserForm, modelMap);

        Assertions.assertNotEquals(null,traineeService.getTraineeByUsername("unit@test.com"));
        Trainee trainee = new Trainee();
        trainee = traineeService.getTraineeByUsername("unit@test.com").get();
        userController.deleteTrainee(String.valueOf(trainee.getTraineeId()),modelMap);
    }

    @Test
    public void setDefaultAttendanceTest(){
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
}
