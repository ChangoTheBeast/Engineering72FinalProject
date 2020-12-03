package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.utilities.Pages;
import com.sparta.eng72.traineetracker.utilities.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @GetMapping("/")
    public String getSimpleRedirect() {
        return "redirect:"+Pages.accessPage(Role.ANY, Pages.LOGIN_PAGE_URL);
    }



    @GetMapping("/login")
    public String getLogin(ModelMap modelMap,
                           @ModelAttribute("loginResult") final Object loginResult) {

        modelMap.addAttribute("showError", loginResult);


        return Pages.accessPage(Role.ANY, Pages.LOGIN_PAGE);
    }

    @GetMapping("/loginFailure")
    public ModelAndView getLoginFailure(ModelMap modelMap,
                                        RedirectAttributes redirectAttributes,
                                        HttpServletRequest request) {

        redirectAttributes.addFlashAttribute("loginResult", "true");

        return new ModelAndView("redirect:/"+Pages.LOGIN_PAGE_URL, modelMap);
    }

    @PostMapping
    public ModelAndView postLoginFailure(ModelMap modelMap,
                                         RedirectAttributes redirectAttributes,
                                         HttpServletRequest request) {
        return new ModelAndView("redirect:/"+Pages.LOGIN_PAGE_URL, modelMap);
    }

}
