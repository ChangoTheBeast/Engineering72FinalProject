package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.entities.TraineeAttendance;
import com.sparta.eng72.traineetracker.services.*;
import com.sparta.eng72.traineetracker.utilities.DateCalculator;
import com.sparta.eng72.traineetracker.utilities.NewUserForm;
import com.sparta.eng72.traineetracker.utilities.Pages;
import com.sparta.eng72.traineetracker.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final TraineeService traineeService;
    public final WeekReportService weekReportService;
    private CourseGroupService courseGroupService;
    private AttendanceService attendanceService;

    @Autowired
    public UserController(AttendanceService attendanceService, CourseGroupService courseGroupService, UserService userService, TraineeService traineeService, WeekReportService weekReportService) {
        this.userService = userService;
        this.traineeService = traineeService;
        this.weekReportService = weekReportService;
        this.courseGroupService = courseGroupService;
        this.attendanceService = attendanceService;
    }

    @GetMapping("/trainee/tempPassword")
    public ModelAndView getPasswordInitialiser(ModelMap modelMap) {
        return new ModelAndView(Pages.accessPage(Role.FIRST_TIME_USER, Pages.FIRST_PASSWORD_PAGE), modelMap);
    }

    @PostMapping("/addFirstPassword")
    public ModelAndView changePasswordForNewUser(@RequestParam String password, ModelMap modelMap, Principal principal) {
        String username = principal.getName();
        userService.addFirstPassword(username, password);
        return new ModelAndView(Pages.accessPage(Role.ANY, Pages.LOGOUT_CURRENT_USER), modelMap);
    }

    @PostMapping("/trainer/addNewUser")
    public ModelAndView addNewUser(@ModelAttribute NewUserForm newUserForm, ModelMap modelMap) {

        if (traineeService.getTraineeByUsername(newUserForm.getEmail()).isPresent()) {
            modelMap.addAttribute("email", newUserForm.getEmail());
            return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_NEW_USER_ALREADY_EXISTS_PAGE), modelMap);
        }
        userService.addNewUser(newUserForm.getEmail());
        Trainee trainee = new Trainee();
        trainee.setFirstName(newUserForm.getFirstName());
        trainee.setLastName(newUserForm.getLastName());
        trainee.setUsername(newUserForm.getEmail());
        trainee.setGroupId(newUserForm.getGroupId());
        traineeService.addNewTrainee(trainee);

        int groupId = newUserForm.getGroupId();
        Date startDate = Date.valueOf(courseGroupService.getGroupByID(groupId).get().getStartDate().toLocalDate());
        Date endDate = Date.valueOf(courseGroupService.getGroupByID(groupId).get().getEndDate().toLocalDate());

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);
        endCalendar.add(Calendar.DATE,1);
        List<TraineeAttendance> traineeAttendances = new ArrayList();
        while (calendar.before(endCalendar)) {
            Date date = new java.sql.Date(calendar.getTimeInMillis());
            if (calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7 ){
                calendar.add(Calendar.DATE, 1);
                continue;
            }
            TraineeAttendance traineeAttendance = new TraineeAttendance();
            traineeAttendance.setAttendanceId(5);
            traineeAttendance.setAttendanceDate(date);
            traineeAttendance.setTraineeId(traineeService.getTraineeByUsername(newUserForm.getEmail()).get().getTraineeId());
            traineeAttendance.setDay(calendar.get(Calendar.DAY_OF_WEEK)-1);
            traineeAttendance.setWeek(DateCalculator.getWeek(date,startDate));
            traineeAttendances.add(traineeAttendance);
            calendar.add(Calendar.DATE, 1);
        }
        attendanceService.saveAllAttendances(traineeAttendances);


        modelMap.addAttribute("trainee", trainee);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_NEW_USER_SUCCESS), modelMap);
    }

    @PostMapping("/trainer/deleteTrainee")
    public ModelAndView deleteTrainee(@RequestParam String traineeId, ModelMap modelMap) {
        int traineeIdInt = Integer.parseInt(traineeId);
        Trainee trainee = traineeService.getTraineeByID(traineeIdInt).get();
        userService.deleteUserByUsername(trainee.getUsername());
        weekReportService.deleteReportsByTraineeID(traineeIdInt);
        traineeService.deleteTraineeByID(traineeIdInt);

        modelMap.addAttribute("trainee", trainee);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_DELETE_SUCCESS), modelMap);
    }

    @PostMapping("/passwordChange")
    public ModelAndView changePassword(@RequestParam String password, @RequestParam String oldPassword, ModelMap modelMap, Principal principal, RedirectAttributes redirectAttributes){
        String username = principal.getName();
        if(userService.isPasswordSame(username,oldPassword)){
            userService.changePasswordByUsername(username, password);
            return new ModelAndView("redirect:"+Pages.accessPage(Role.ANY, Pages.LOGOUT_CURRENT_USER), modelMap);
        }else{
            redirectAttributes.addFlashAttribute("error", "true");
            return new ModelAndView("redirect:"+"/changePassword", modelMap);
        }



    }
    @GetMapping("/changePassword")
    public ModelAndView getChangePasswordScreen(@ModelAttribute("error") final Object error, ModelMap modelMap){

        modelMap.addAttribute("showError", error);
        return new ModelAndView(Pages.accessPage(Role.ANY, Pages.CHANGE_PASSWORD_PAGE));
    }

    @PostMapping("/forgotPassword")
    public ModelAndView getNewPassword(@RequestParam String email, ModelMap modelMap){
        if(!(userService.hasUser(email))){
            return new ModelAndView(Pages.accessPage(Role.ANY, Pages.USER_NOT_FOUND_PAGE), modelMap);
        }
        userService.recoverPassword(email);

        return new ModelAndView(Pages.accessPage(Role.ANY, Pages.PASSWORD_SENT_PAGE), modelMap);
    }

    @GetMapping("/recoverPassword")
    public ModelAndView recoverPassword(ModelMap modelMap){
        return new ModelAndView(Pages.accessPage(Role.ANY, Pages.RECOVER_PASSWORD_PAGE), modelMap);
    }

}
