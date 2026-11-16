package com.thetouchlinelab.fpl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LandingPageController {

    @GetMapping("/")
    public ModelAndView landingPage(Model model) {
        ModelAndView modelAndView = new ModelAndView("landing_page");

        return modelAndView;
    }


}
