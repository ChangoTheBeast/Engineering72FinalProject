package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.entities.CourseGroup;
import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.services.CourseGroupService;
import com.sparta.eng72.traineetracker.services.AttendanceService;
import com.sparta.eng72.traineetracker.services.CourseService;
import com.sparta.eng72.traineetracker.services.TraineeService;
import com.sparta.eng72.traineetracker.utilities.AssignGroupForm;
import com.sparta.eng72.traineetracker.utilities.Pages;
import com.sparta.eng72.traineetracker.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ManagementController {

    public final TraineeService traineeService;
    public final CourseGroupService courseGroupService;
    public final CourseService courseService;

    @Autowired
    public ManagementController(TraineeService traineeService, CourseGroupService courseGroupService, CourseService courseService) {
        this.traineeService = traineeService;
        this.courseGroupService = courseGroupService;
        this.courseService = courseService;
    }

    @GetMapping("/trainer/manageClass")
    public ModelAndView getGroupChange(@ModelAttribute AssignGroupForm assignGroupForm, ModelMap modelMap) {
        getManageClassModelMap(modelMap);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_GROUPS_PAGE), modelMap);
    }


    @PostMapping("/trainer/createGroup")
    public ModelAndView createGroup(@ModelAttribute CourseGroup newClass, ModelMap modelMap) {
        getManageClassModelMap(modelMap);

        for(CourseGroup group : courseGroupService.getAllCourseGroups()){
            if(newClass.getGroupName().equals(group.getGroupName())){
                String error = "A group with name " + newClass.getGroupName() + " already exists";
                modelMap.addAttribute("addError", error);
                return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_GROUPS_PAGE), modelMap);
            }
        }

        newClass.setCurrentWeek(0);
        courseGroupService.saveNewGroup(newClass);

        String success = "New group with name " + newClass.getGroupName() + " successfully created";
        modelMap.addAttribute("addSuccess", success);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_GROUPS_PAGE), modelMap);
    }

    @PostMapping("/trainer/modifyGroup")
    public ModelAndView postGroupChange(@ModelAttribute AssignGroupForm assignGroupForm, ModelMap modelMap) {

        Trainee trainee = traineeService.changeTraineeCourseGroupByID(assignGroupForm.getTraineeId(), assignGroupForm.getGroupId());
        getManageClassModelMap(modelMap);

        String success = "Assigned " + trainee.getFirstName() + " " + trainee.getLastName() + " to " + courseGroupService.getGroupByID(trainee.getGroupId()).get().getGroupName();
        modelMap.addAttribute("modifySuccess", success);
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_GROUPS_PAGE), modelMap);
    }

    private void getManageClassModelMap(ModelMap modelMap) {
        modelMap.addAttribute("assignGroupForm", new AssignGroupForm());
        modelMap.addAttribute("allTrainees", traineeService.getAllTrainees());
        modelMap.addAttribute("allGroups", courseGroupService.getAllCourseGroups());
        modelMap.addAttribute("allCourses", courseService.getAllCourses());
        modelMap.addAttribute("newClass", new CourseGroup());
    }


}
