package com.sparta.eng72.traineetracker.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.sparta.eng72.traineetracker.entities.*;
import com.sparta.eng72.traineetracker.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class TraineeProfileController {

    private final AttendanceService attendanceService;
    private final WeekReportService weekReportService;
    private final CodingGamesAPIService codingGamesAPIService;
    private final TraineeService traineeService;
    private final CourseGroupService courseGroupService;
    private final CourseService courseService;

    @Autowired
    public TraineeProfileController(AttendanceService attendanceService, WeekReportService weekReportService, CodingGamesAPIService codingGamesAPIService, TraineeService traineeService, CourseGroupService courseGroupService, CourseService courseService) {
        this.attendanceService = attendanceService;
        this.weekReportService = weekReportService;
        this.codingGamesAPIService = codingGamesAPIService;
        this.traineeService = traineeService;
        this.courseGroupService = courseGroupService;
        this.courseService = courseService;
    }

    @GetMapping("/trainee/home")
    public String getTraineeProfile(ModelMap modelMap, Principal principal) {
        Trainee trainee = getTrainee(principal);
        CourseGroup courseGroup = getCourseGroup(trainee);
        Course course = getCourse(courseGroup);
        WeekReport report = getWeekReport(trainee);
        List<JsonNode> assessments = getAssessments(trainee);
        List<TraineeAttendance> traineeAttendanceList = attendanceService.getTraineeAttendanceByTraineeId(trainee.getTraineeId());

        double onTime = 0, late = 0, excused = 0, unexcused = 0;
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

        double count = onTime + late + excused + unexcused;
        DecimalFormat decimal = new DecimalFormat("###.##");

        String onTimePercentage = getPercentage(onTime, count, decimal);
        String latePercentage = getPercentage(late, count, decimal);
        String excusedPercentage = getPercentage(excused, count, decimal);
        String unexcusedPercentage = getPercentage(unexcused, count, decimal);

        getTraineeHomeModelMap(modelMap, trainee, courseGroup, course, report, assessments, onTimePercentage, latePercentage, excusedPercentage, unexcusedPercentage);

        return "trainee/traineeProfile";
    }

    @RequestMapping(value="/trainer/viewTrainee", method= RequestMethod.POST, params="btnStatus=profile")
    public String getTraineeProfile(Integer traineeId) {
        return "redirect:/trainer/traineeProfile/" + traineeId;
    }

    @GetMapping("/trainer/traineeProfile/{traineeId}")
    public String getTraineeProfile(ModelMap modelMap, @PathVariable Integer traineeId) {

        Trainee trainee = getTrainee(traineeId);
        CourseGroup courseGroup = getCourseGroup(trainee);
        Course course = getCourse(courseGroup);
        WeekReport report = getWeekReport(trainee);
        List<JsonNode> assessments = getAssessments(trainee);
        List<TraineeAttendance> traineeAttendanceList = attendanceService.getTraineeAttendanceByTraineeId(traineeId);

        double onTime = 0, late = 0, excused = 0, unexcused = 0;
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

        double count = onTime + late + excused + unexcused;
        DecimalFormat decimal = new DecimalFormat("###.##");

        String onTimePercentage = getPercentage(onTime, count, decimal);
        String latePercentage = getPercentage(late, count, decimal);
        String excusedPercentage = getPercentage(excused, count, decimal);
        String unexcusedPercentage = getPercentage(unexcused, count, decimal);

        getTraineeHomeModelMap(modelMap, trainee, courseGroup, course, report, assessments, onTimePercentage, latePercentage, excusedPercentage, unexcusedPercentage);

        return "trainee/traineeProfile";
    }

    private void getTraineeHomeModelMap(ModelMap modelMap, Trainee trainee, CourseGroup courseGroup, Course course, WeekReport report, List<JsonNode> assessments, String onTimePercentage, String latePercentage, String excusedPercentage, String unexcusedPercentage) {
        modelMap.addAttribute("onTimePercentage", onTimePercentage);
        modelMap.addAttribute("latePercentage", latePercentage);
        modelMap.addAttribute("excusedPercentage", excusedPercentage);
        modelMap.addAttribute("unexcusedPercentage", unexcusedPercentage);
        modelMap.addAttribute("previousWeekReport", report);
        modelMap.addAttribute("now", LocalDateTime.now());
        modelMap.addAttribute("codingGamesAPI", new CodingGamesAPIService());
        modelMap.addAttribute("assessments", assessments);
        modelMap.addAttribute("trainee", trainee);
        modelMap.addAttribute("courseGroup", courseGroup);
        modelMap.addAttribute("course", course);
    }

    //Get Trainee as "TRAINEE"
    private Trainee getTrainee(Principal principal) {
        Trainee trainee = null;
        if (traineeService.getTraineeByUsername(principal.getName()).isPresent()){
            trainee = traineeService.getTraineeByUsername(principal.getName()).get();
        }
        return trainee;
    }

    //Get Trainee as "TRAINER"
    private Trainee getTrainee(Integer traineeId) {
        Trainee trainee = null;
        if (traineeService.getTraineeByID(traineeId).isPresent()){
            trainee = traineeService.getTraineeByID(traineeId).get();
        }
        return trainee;
    }

    private CourseGroup getCourseGroup(Trainee trainee) {
        CourseGroup courseGroup = null;
        if (courseGroupService.getGroupByID(trainee != null ? trainee.getGroupId() : null).isPresent()) {
            courseGroup = courseGroupService.getGroupByID(trainee != null ? trainee.getGroupId() : null).get();
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

    private WeekReport getWeekReport(Trainee trainee) {
        WeekReport report = null;
        if (weekReportService.getPreviousWeekReportByTraineeID(trainee.getTraineeId() != null ? trainee.getTraineeId() : null).isPresent()) {
            report = weekReportService.getPreviousWeekReportByTraineeID(trainee.getTraineeId() != null ? trainee.getTraineeId() : null).get();
        }
        return report;
    }

    private List<JsonNode> getAssessments(Trainee trainee) {
        return codingGamesAPIService.getAllReportsByEmail(trainee.getUsername());
    }

    private String getPercentage(double late, double count, DecimalFormat decimal) {
        double percentage = late / count * 100;
        if(percentage >= 0)  {
            return "" + decimal.format(percentage) + "%";
        }
        return "NO ENTRY";
    }

}
