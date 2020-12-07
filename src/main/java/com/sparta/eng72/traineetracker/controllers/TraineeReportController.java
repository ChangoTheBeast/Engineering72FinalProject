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
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
public class TraineeReportController {
    WeekReportService weekReportService;
    TraineeService traineeService;

    @Autowired
    public TraineeReportController(WeekReportService weekReportService, TraineeService traineeService) {
        this.weekReportService = weekReportService;
        this.traineeService = traineeService;
    }

    @GetMapping("/trainee/report/{week}")
    public String getTraineeFeedbackForm(@PathVariable Integer week, Model model, Principal principal) {
        Integer traineeId = traineeService.getTraineeByUsername(principal.getName()).get().getTraineeId();

        Optional<WeekReport> weekReportOptional = weekReportService.getWeekReportByTraineeIdAndWeekNum(traineeId, week);
        if (weekReportOptional.isEmpty()) {
            return Pages.accessPage(Role.TRAINEE, Pages.NO_ITEM_IN_DATABASE_ERROR);
        }
        model.addAttribute("traineeFeedback", weekReportOptional.get());
        return Pages.accessPage(Role.TRAINEE, Pages.TRAINEE_FEEDBACK_FORM_PAGE);
    }

    @PostMapping("/traineeRecentReport")
    public ModelAndView postRecentReport(Principal principal, ModelMap modelMap) {
        Trainee trainee = traineeService.getTraineeByUsername(principal.getName()).get();

        Optional<WeekReport> optionalWeekReport = weekReportService.getCurrentWeekReportByTraineeID(trainee.getTraineeId());

        if (optionalWeekReport.isEmpty()) {
            return new ModelAndView("redirect:"+Pages.accessPage(Role.TRAINEE, Pages.NO_ITEM_IN_DATABASE_ERROR), modelMap);
        } else {
            modelMap.addAttribute("traineeFeedback", optionalWeekReport.get());
        }

        RedirectView redirectView = new RedirectView(Pages.TRAINEE_REPORT_URL+"/"+optionalWeekReport.get().getWeekNum());
        redirectView.setExposeModelAttributes(false);
        ModelAndView modelAndView = new ModelAndView(redirectView, modelMap);

        return modelAndView;
    }

    @GetMapping("/traineeRecentReport")
    public ModelAndView getRecentReport(Principal principal, ModelMap modelMap) {
        Trainee trainee = traineeService.getTraineeByUsername(principal.getName()).get();

        Optional<WeekReport> optionalWeekReport = weekReportService.getCurrentWeekReportByTraineeID(trainee.getTraineeId());

        if (optionalWeekReport.isEmpty()) {
            return new ModelAndView("redirect:"+Pages.accessPage(Role.TRAINEE, Pages.NO_ITEM_IN_DATABASE_ERROR), modelMap);
        } else {
            modelMap.addAttribute("traineeFeedback", optionalWeekReport.get());
        }

        RedirectView redirectView = new RedirectView(Pages.TRAINEE_REPORT_URL+"/"+optionalWeekReport.get().getWeekNum());
        redirectView.setExposeModelAttributes(false);
        ModelAndView modelAndView = new ModelAndView(redirectView, modelMap);

        return modelAndView;
    }

    @PostMapping("/traineeReportProcessing")
    public RedirectView processReportRequest(Integer reportId, ModelMap modelMap) {
        WeekReport weekReport = weekReportService.getWeekReportByReportId(reportId).get();

        Integer weekNum = weekReport.getWeekNum();

        RedirectView redirectView = new RedirectView(Pages.TRAINEE_REPORT_URL+"/"+weekNum);
        redirectView.setExposeModelAttributes(false);

        return redirectView;
    }


    @GetMapping("/trainee/report")
    public ModelMap getTraineeWeeklyReports(ModelMap modelMap, Principal principal) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - K:mm a", Locale.ENGLISH);
        Integer traineeId = traineeService.getTraineeByUsername(principal.getName()).get().getTraineeId();
        Trainee trainee = traineeService.getTraineeByID(traineeId).get();
        List<WeekReport> reports = weekReportService.getReportsByTraineeID(traineeId);
        Collections.reverse(reports);
        modelMap.addAttribute("trainee", trainee);
        modelMap.addAttribute("reports", reports);
        modelMap.addAttribute("now", LocalDateTime.now());
        modelMap.addAttribute("dateFormat", formatter);
        return modelMap;
    }
}
