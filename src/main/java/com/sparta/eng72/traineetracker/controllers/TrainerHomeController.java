package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.entities.*;
import com.sparta.eng72.traineetracker.utilities.NewUserForm;
import com.sparta.eng72.traineetracker.services.*;
import com.sparta.eng72.traineetracker.utilities.Pages;
import com.sparta.eng72.traineetracker.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TrainerHomeController {

    public final TraineeService traineeService;
    public final TrainerService trainerService;
    public final WeekReportService weekReportService;
    public final CourseGroupService courseGroupService;
    public final CourseService courseService;
    public final AttendanceService attendanceService;

    @Autowired
    public TrainerHomeController(TraineeService traineeService, TrainerService trainerService, WeekReportService weekReportService, CourseGroupService courseGroupService, CourseService courseService, AttendanceService attendanceService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.weekReportService = weekReportService;
        this.courseGroupService = courseGroupService;
        this.courseService = courseService;
        this.attendanceService = attendanceService;
    }

    @GetMapping("/trainer/home")
    public String getTrainerHome(ModelMap modelMap, Principal principal) {
        List<Trainee> traineeList = traineeService.getTraineesByGroupId(trainerService.getTrainerByUsername(principal.getName()).get().getGroupId());
        List<Trainee> missedDeadlineList = new ArrayList<>();
        List<Trainee> needToCompleteList = new ArrayList<>();
        List<Integer> courseDurationList = new ArrayList<>();
        List<Integer> traineeCounter = new ArrayList<>();
        int counter = 0;
        List<List<WeekReport>> traineeCompletedList = new ArrayList<>();


        Integer groupId = trainerService.getTrainerByUsername(principal.getName()).get().getGroupId();
        Integer courseId = courseGroupService.getGroupByID(groupId).get().getCourseId();
        Integer courseDuration = courseService.getGroupByID(courseId).get().getDuration();

        for(int i = 1; i <= courseDuration; i++){
            courseDurationList.add(i);
        }
        for (Trainee trainee: traineeList) {
            traineeCounter.add(counter);
            counter++;
            List<WeekReport> weekReports = weekReportService.getReportsByTraineeID(trainee.getTraineeId());
            traineeCompletedList.add(weekReports);
            Optional<WeekReport> weekReport = weekReportService.getCurrentWeekReportByTraineeID(trainee.getTraineeId());
            if(weekReport.isPresent()){
                if(weekReport.get().getDeadline().isBefore(LocalDateTime.now()) && weekReport.get().getTraineeSubmittedFlag() == 0){
                    missedDeadlineList.add(trainee);
                }

                if(weekReport.get().getTrainerCompletedFlag() == 0){
                    needToCompleteList.add(trainee);
                }
            }
        }

        Trainer trainer = trainerService.getTrainerByUsername(principal.getName()).get();
        CourseGroup courseGroup = courseGroupService.getGroupByID(trainer.getGroupId()).get();
        Course course = courseService.getCourseByID(courseGroup.getCourseId()).get();

        double onTime = 0, late = 0, excused = 0, unexcused = 0;
        for(Trainee trainee : traineeList){
            List<TraineeAttendance> traineeAttendanceList = attendanceService.getTraineeAttendanceByTraineeId(trainee.getTraineeId());
            for(TraineeAttendance attendance : traineeAttendanceList){
                switch (attendance.getAttendanceId()) {
                    case 1:
                        onTime++;
                        continue;
                    case 2:
                        late++;
                        continue;
                    case 3:
                        excused++;
                        continue;
                    case 4:
                        unexcused++;
                        continue;
                    default:
                        continue;
                }
            }
        }

        double count = onTime + late + excused + unexcused;
        DecimalFormat decimal = new DecimalFormat("###.##");

        String onTimePercentage = "" + decimal.format(onTime/count * 100) + "%";
        String latePercentage = "" + decimal.format(late/count * 100) + "%";
        String excusedPercentage = "" + decimal.format(excused/count * 100) + "%";
        String unexcusedPercentage = "" + decimal.format(unexcused/count * 100) + "%";

        modelMap.addAttribute("groupOnTime", onTimePercentage);
        modelMap.addAttribute("groupLate", latePercentage);
        modelMap.addAttribute("groupExcused", excusedPercentage);
        modelMap.addAttribute("groupUnexcused", unexcusedPercentage);

        modelMap.addAttribute("trainer", trainer);
        modelMap.addAttribute("courseGroup",courseGroup);
        modelMap.addAttribute("course", course);
        modelMap.addAttribute("traineeList", traineeList);
        modelMap.addAttribute("missedDeadlineList", missedDeadlineList);
        modelMap.addAttribute("toCompleteList", needToCompleteList);
        modelMap.addAttribute("courseDuration", courseDuration);
        modelMap.addAttribute("courseDurationList", courseDurationList);
        modelMap.addAttribute("traineeCompletedList", traineeCompletedList);
        modelMap.addAttribute("traineeCounter", traineeCounter);
//        return Pages.accessPage(Role.TRAINER, Pages.TRAINER_HOME_PAGE);
        return "/trainer/trainerHome";
    }


    @GetMapping("/trainer/manageTrainee")
    public ModelAndView newUserForm(ModelMap modelMap) {
        modelMap.addAttribute("newUserForm", new NewUserForm());
        modelMap.addAttribute("allGroups", courseGroupService.getAllCourseGroups());
        modelMap.addAttribute("allTrainees", traineeService.getAllTrainees());
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_NEW_USER_PAGE), modelMap);
    }


}
