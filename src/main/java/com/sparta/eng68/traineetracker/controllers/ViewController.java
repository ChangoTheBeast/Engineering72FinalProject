package com.sparta.eng68.traineetracker.controllers;

import com.sparta.eng68.traineetracker.utilities.Pages;
import com.sparta.eng68.traineetracker.utilities.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/trainee/consultancy")
    public String getConsultancySkills() {
        return Pages.accessPage(Role.ANY, Pages.CONSULTANCY);
    }

    @GetMapping("/trainee/guide")
    public String getTraineeGuide() { return Pages.TRAINEE_GUIDE_PAGE;}

    @GetMapping("/pagenotfounderror")
    public String get404() {
        return Pages.accessPage(Role.ANY, Pages.PAGE_NOT_FOUND_ERROR);
    }
}
