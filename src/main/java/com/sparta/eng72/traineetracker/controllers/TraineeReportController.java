package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.entities.WeekReport;
import com.sparta.eng72.traineetracker.services.TraineeService;
import com.sparta.eng72.traineetracker.services.WeekReportService;
import com.sparta.eng72.traineetracker.utilities.Pages;
import com.sparta.eng72.traineetracker.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class TraineeReportController {

    private final WeekReportService weekReportService;
    private final TraineeService traineeService;

    @Autowired
    public TraineeReportController(WeekReportService weekReportService, TraineeService traineeService) {
        this.weekReportService = weekReportService;
        this.traineeService = traineeService;
    }

    @GetMapping("/trainee/report/{week}")
    public String getTraineeFeedbackForm(@PathVariable Integer week, Model model, Principal principal) {
        Trainee trainee = getTrainee(principal);
        WeekReport report = getTraineeWeekReport(trainee, week);
        if (report == null) {
            return Pages.accessPage(Role.TRAINEE, Pages.NO_ITEM_IN_DATABASE_ERROR);
        }
        model.addAttribute("traineeFeedback", report);
        return Pages.accessPage(Role.TRAINEE, Pages.TRAINEE_FEEDBACK_FORM_PAGE);
    }

    @GetMapping("/trainee/report")
    public ModelAndView getTraineeWeeklyReports(ModelMap modelMap, Principal principal) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - K:mm a", Locale.ENGLISH);
        Trainee trainee = getTrainee(principal);
        List<WeekReport> reports = getTraineeReports(trainee);
        Collections.reverse(reports);
        getWeeklyReportModelMap(modelMap, formatter, trainee, reports);
        return new ModelAndView("/trainee/traineeReport", modelMap) ;
    }

    @GetMapping("/traineeRecentReport")
    public ModelAndView getRecentReport(Principal principal, ModelMap modelMap) {
        return getRecentReportModelAndView(principal, modelMap);
    }

    @PostMapping("/traineeRecentReport")
    public ModelAndView postRecentReport(Principal principal, ModelMap modelMap) {
        return getRecentReportModelAndView(principal, modelMap);
    }

    @PostMapping("/traineeReportProcessing")
    public RedirectView processReportRequest(Integer reportId, ModelMap modelMap) {
        WeekReport report = getWeekReport(reportId);
        RedirectView redirectView = new RedirectView(Pages.TRAINEE_REPORT_URL + "/" + report.getWeekNum());
        redirectView.setExposeModelAttributes(false);
        return redirectView;
    }


    @PostMapping("/trainee/updateReport")
    public String submitTraineeFeedbackForm(Integer reportId, String stopTrainee, String startTrainee,
                                            String continueTrainee, String traineeConsulGrade,
                                            String traineeTechGrade){

        WeekReport weekReport = setTraineeWeekReport(reportId, stopTrainee, startTrainee, continueTrainee,
                traineeConsulGrade, traineeTechGrade);
        weekReportService.updateWeekReport(weekReport);

        return Pages.accessPage(Role.TRAINEE, "redirect:" + Pages.TRAINEE_HOME_URL);
    }

    private ModelAndView getRecentReportModelAndView(Principal principal, ModelMap modelMap) {
        Trainee trainee = getTrainee(principal);
        WeekReport report = getCurrentWeekReport(trainee);

        if (report == null) {
            return new ModelAndView("redirect:" + Pages.accessPage(Role.TRAINEE, Pages.NO_ITEM_IN_DATABASE_ERROR), modelMap);
        }

        modelMap.addAttribute("traineeFeedback", report);

        RedirectView redirectView = new RedirectView(Pages.TRAINEE_REPORT_URL + "/" + report.getWeekNum());
        redirectView.setExposeModelAttributes(false);

        return new ModelAndView(redirectView, modelMap);
    }

    private WeekReport setTraineeWeekReport(Integer reportId, String stopTrainee, String startTrainee, String continueTrainee, String traineeConsulGrade, String traineeTechGrade) {
        WeekReport weekReport = getWeekReport(reportId);
        weekReport.setStopTrainee(stopTrainee);
        weekReport.setStartTrainee(startTrainee);
        weekReport.setContinueTrainee(continueTrainee);
        weekReport.setTechnicalGradeTrainee(traineeTechGrade);
        weekReport.setConsultantGradeTrainee(traineeConsulGrade);
        weekReport.setTraineeConsultantGradeFlag((byte) 1);
        weekReport.setTraineeTechnicalGradeFlag((byte) 1);
        weekReport.setTraineeStartFlag((byte) 1);
        weekReport.setTraineeStopFlag((byte) 1);
        weekReport.setTraineeContinueFlag((byte) 1);
        weekReport.setMostRecentEdit(LocalDateTime.now());
        return weekReport;
    }

    private List<WeekReport> getTraineeReports(Trainee trainee){
        return weekReportService.getReportsByTraineeID(trainee.getTraineeId());
    }

    private WeekReport getCurrentWeekReport(Trainee trainee) {
        WeekReport report = null;
        if(weekReportService.getCurrentWeekReportByTraineeID(trainee.getTraineeId()).isPresent()){
            report = weekReportService.getCurrentWeekReportByTraineeID(trainee.getTraineeId()).get();
        }
        return report;
    }

    private WeekReport getTraineeWeekReport(Trainee trainee, Integer week) {
        WeekReport report = null;
        if(weekReportService.getWeekReportByTraineeIdAndWeekNum(trainee.getTraineeId(), week).isPresent()){
            report = weekReportService.getWeekReportByTraineeIdAndWeekNum(trainee.getTraineeId(), week).get();
        }
        return report;
    }

    private WeekReport getWeekReport(Integer reportId) {
        WeekReport report = null;
        if(weekReportService.getWeekReportByReportId(reportId).isPresent()){
            report = weekReportService.getWeekReportByReportId(reportId).get();
        }
        return report;
    }

    private WeekReport getWeekReport(Trainee trainee) {
        WeekReport report = null;
        if(weekReportService.getPreviousWeekReportByTraineeID(trainee.getTraineeId()).isPresent()){
            report = weekReportService.getPreviousWeekReportByTraineeID(trainee.getTraineeId()).get();
        }
        return report;
    }

    private Trainee getTrainee(Integer traineeId) {
        Trainee trainee = null;
        if(traineeService.getTraineeByID(traineeId).isPresent()){
            trainee = traineeService.getTraineeByID(traineeId).get();
        }
        return trainee;
    }

    private Trainee getTrainee(Principal principal) {
        Trainee trainee = null;
        if(traineeService.getTraineeByUsername(principal.getName()).isPresent()){
            trainee = traineeService.getTraineeByUsername(principal.getName()).get();
        }
        return trainee;
    }

    private void getWeeklyReportModelMap(ModelMap modelMap, DateTimeFormatter formatter, Trainee trainee, List<WeekReport> reports) {
        modelMap.addAttribute("trainee", trainee);
        modelMap.addAttribute("reports", reports);
        modelMap.addAttribute("now", LocalDateTime.now());
        modelMap.addAttribute("dateFormat", formatter);
    }
}
