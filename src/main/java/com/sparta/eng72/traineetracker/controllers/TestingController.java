package com.sparta.eng72.traineetracker.controllers;

import com.sparta.eng72.traineetracker.utilities.Pages;
import com.sparta.eng72.traineetracker.utilities.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestingController {

    @GetMapping("/home")
    public String getHomePage() {
        return Pages.accessPage(Role.ANY, Pages.HOME_PAGE);
    }
}
