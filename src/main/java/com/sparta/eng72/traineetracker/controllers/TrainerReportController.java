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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
public class TrainerReportController {

    private final TraineeService traineeService;
    private final WeekReportService weekReportService;

    @Autowired
    public TrainerReportController(TraineeService traineeService, WeekReportService weekReportService) {
        this.traineeService = traineeService;
        this.weekReportService = weekReportService;
    }

    @PostMapping("/trainer/updateReport")
    public ModelAndView postUpdateTrainerReport(ModelMap modelMap, Integer reportId, String trainerTechGrade,
                                                String trainerConsulGrade, String trainerOverallGrade, String trainerComments,
                                                String stopTrainer, String startTrainer, String continueTrainer) {

        WeekReport weekReport = weekReportService.getWeekReportByReportId(reportId).get();
        Integer traineeId = weekReport.getTraineeId();
        Trainee trainee = traineeService.getTraineeByID(traineeId).get();
        List <WeekReport> reports = weekReportService.getReportsByTraineeID(traineeId);

        Collections.reverse(reports);

        weekReport.setStopTrainer(stopTrainer);
        weekReport.setStartTrainer(startTrainer);
        weekReport.setContinueTrainer(continueTrainer);
        weekReport.setTrainerComments(trainerComments);
        weekReport.setTechnicalGradeTrainer(trainerTechGrade);
        weekReport.setConsultantGradeTrainer(trainerConsulGrade);
        weekReport.setOverallGradeTrainer(trainerOverallGrade);
        weekReport.setTrainerCompletedFlag((byte) 1);

        weekReportService.updateWeekReport(weekReport);
        modelMap.addAttribute("traineeId", traineeId);
        modelMap.addAttribute("trainee", trainee);
        modelMap.addAttribute("reports", reports);
        modelMap.addAttribute("now", LocalDateTime.now());

        RedirectView redirectView = new RedirectView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_HOME_URL));
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView, modelMap);
    }

    @PostMapping("/trainer/reportTraineeWeekProcessing")
    public ModelAndView getTrainerFeedbackForm(Integer reportId, ModelMap modelMap) {

        WeekReport weekReport = weekReportService.getWeekReportByReportId(reportId).get();

        RedirectView redirectView = new RedirectView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_REPORT_URL+"/"+weekReport.getTraineeId()+"/"+weekReport.getWeekNum()));
        redirectView.setExposeModelAttributes(false);
        ModelAndView modelAndView = new ModelAndView(redirectView, modelMap);
        return modelAndView;
    }

    @GetMapping("/trainer/report/{traineeId}/{weekNum}")
    public ModelAndView getTrainerFeedbackForm(@PathVariable Integer traineeId, @PathVariable Integer weekNum, ModelMap modelMap) {
        Optional<WeekReport> optionalWeekReport = weekReportService.getWeekReportByTraineeIdAndWeekNum(traineeId, weekNum);
        if (optionalWeekReport.isEmpty()) {
            return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.NO_ITEM_IN_DATABASE_ERROR), modelMap);
        }
        WeekReport report = optionalWeekReport.get();
        modelMap.addAttribute("trainee", traineeService.getTraineeByID(traineeId).get());
        modelMap.addAttribute("trainerFeedback", report);

        return new ModelAndView(Pages.accessPage(Role.TRAINER, Pages.TRAINER_FEEDBACK_FORM_PAGE), modelMap);
    }


    @RequestMapping(value="/trainer/viewTrainee", method = RequestMethod.POST, params="btnStatus=reports")
    public String getTrainerWeeklyReports(Integer traineeId, Model model) {
        return Pages.accessPage(Role.TRAINER, "redirect:"+Pages.TRAINER_REPORT_URL+"/"+traineeId);
    }

    @GetMapping("/trainer/report/{traineeId}")
    public ModelAndView getTrainerWeeklyReportsWithPath(@PathVariable Integer traineeId, ModelMap modelMap) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - K:mm a", Locale.ENGLISH);
        Trainee trainee = traineeService.getTraineeByID(traineeId).get();
        List<WeekReport> reports = weekReportService.getReportsByTraineeID(traineeId);
        Collections.reverse(reports);
        modelMap.addAttribute("traineeId", traineeId);
        modelMap.addAttribute("trainee", trainee);
        modelMap.addAttribute("reports", reports);
        modelMap.addAttribute("now", LocalDateTime.now());
        modelMap.addAttribute("dateFormat", formatter);
        return new ModelAndView("redirect:/trainee/traineeReport/" + traineeId, modelMap);
    }
}
