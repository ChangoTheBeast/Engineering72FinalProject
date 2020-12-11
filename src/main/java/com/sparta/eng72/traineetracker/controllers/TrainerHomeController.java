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

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final WeekReportService weekReportService;
    private final CourseGroupService courseGroupService;
    private final CourseService courseService;
    private final AttendanceService attendanceService;

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

        Trainer trainer = getTrainer(principal);
        Integer groupId = trainer.getGroupId();
        CourseGroup courseGroup = getCourseGroup(groupId);
        Course course = getCourse(courseGroup);
        Integer courseDuration = course.getDuration();

        List<Integer> courseDurationList = getCourseDurationList(courseDuration);

        List<Trainee> traineeList = traineeService.getTraineesByGroupId(trainer.getGroupId());
        List<Integer> traineeCounter = new ArrayList<>();

        int counter = 0;

        List<List<WeekReport>> traineeCompletedList = new ArrayList<>();
        List<Trainee> missedDeadlineList = new ArrayList<>();
        List<Trainee> needToCompleteList = new ArrayList<>();

        for (Trainee trainee: traineeList) {
            traineeCounter.add(counter);
            counter++;
            List<WeekReport> weekReports = getTraineeReports(trainee);
            traineeCompletedList.add(weekReports);
            WeekReport weekReport = getCurrentWeekReport(trainee);
            if(weekReport != null){
                if(weekReport.getDeadline().isBefore(LocalDateTime.now()) && weekReport.getTraineeSubmittedFlag() == 0){
                    missedDeadlineList.add(trainee);
                }
                if(weekReport.getTrainerCompletedFlag() == 0){
                    needToCompleteList.add(trainee);
                }
            }
        }

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

        String onTimePercentage = getPercentage(onTime, count, decimal);
        String latePercentage = getPercentage(late, count, decimal);
        String excusedPercentage = getPercentage(excused, count, decimal);
        String unexcusedPercentage = getPercentage(unexcused, count, decimal);

        getTrainerHomeModelMap(modelMap, courseDuration, courseDurationList, traineeList, traineeCounter,
                traineeCompletedList, missedDeadlineList, needToCompleteList, trainer, courseGroup, course,
                onTimePercentage, latePercentage, excusedPercentage, unexcusedPercentage);
        return Pages.accessPage(Role.TRAINER, Pages.TRAINER_HOME_PAGE);
    }

    @GetMapping("/trainer/manageTrainee")
    public ModelAndView newUserForm(ModelMap modelMap) {
        modelMap.addAttribute("newUserForm", new NewUserForm());
        modelMap.addAttribute("allGroups", courseGroupService.getAllCourseGroups());
        modelMap.addAttribute("allTrainees", traineeService.getAllTrainees());
        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_NEW_USER_PAGE), modelMap);
    }

    private void getTrainerHomeModelMap(ModelMap modelMap, Integer courseDuration, List<Integer> courseDurationList, List<Trainee> traineeList, List<Integer> traineeCounter, List<List<WeekReport>> traineeCompletedList, List<Trainee> missedDeadlineList, List<Trainee> needToCompleteList, Trainer trainer, CourseGroup courseGroup, Course course, String onTimePercentage, String latePercentage, String excusedPercentage, String unexcusedPercentage) {
        modelMap.addAttribute("groupOnTime", onTimePercentage);
        modelMap.addAttribute("groupLate", latePercentage);
        modelMap.addAttribute("groupExcused", excusedPercentage);
        modelMap.addAttribute("groupUnexcused", unexcusedPercentage);
        modelMap.addAttribute("trainer", trainer);
        modelMap.addAttribute("courseGroup", courseGroup);
        modelMap.addAttribute("course", course);
        modelMap.addAttribute("traineeList", traineeList);
        modelMap.addAttribute("missedDeadlineList", missedDeadlineList);
        modelMap.addAttribute("toCompleteList", needToCompleteList);
        modelMap.addAttribute("courseDuration", courseDuration);
        modelMap.addAttribute("courseDurationList", courseDurationList);
        modelMap.addAttribute("traineeCompletedList", traineeCompletedList);
        modelMap.addAttribute("traineeCounter", traineeCounter);
    }

    private List<Integer> getCourseDurationList(Integer courseDuration) {
        List<Integer> courseDurationList = new ArrayList<>();
        for(int i = 1; i <= courseDuration; i++){
            courseDurationList.add(i);
        }
        return courseDurationList;
    }

    private Trainer getTrainer(Principal principal) {
        Trainer trainer = null;
        if (trainerService.getTrainerByUsername(principal.getName()).isPresent()){
            trainer = trainerService.getTrainerByUsername(principal.getName()).get();
        }
        return trainer;
    }

    private CourseGroup getCourseGroup(Integer groupId) {
        CourseGroup courseGroup = null;
        if (courseGroupService.getGroupByID(groupId).isPresent()) {
            courseGroup = courseGroupService.getGroupByID(groupId).get();
        }
        return courseGroup;
    }

    private Course getCourse(CourseGroup courseGroup) {
        Course course = null;
        if (courseService.getCourseByID(courseGroup != null ? courseGroup.getCourseId() : null).isPresent()) {
            course = courseService.getCourseByID(courseGroup != null ? courseGroup.getCourseId() : null).get();
        }
        return course;
    }

    private WeekReport getCurrentWeekReport(Trainee trainee) {
        WeekReport report = null;
        if(weekReportService.getCurrentWeekReportByTraineeID(trainee.getTraineeId()).isPresent()){
            report = weekReportService.getCurrentWeekReportByTraineeID(trainee.getTraineeId()).get();
        }
        return report;
    }

    private List<WeekReport> getTraineeReports(Trainee trainee){
        return weekReportService.getReportsByTraineeID(trainee.getTraineeId());
    }

    private String getPercentage(double late, double count, DecimalFormat decimal) {
        double percentage = late / count * 100;
        if(percentage >= 0)  {
            return "" + decimal.format(percentage) + "%";
        }
        return "NO ENTRY";
    }

}
