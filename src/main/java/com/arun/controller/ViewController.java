package com.arun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {

    @RequestMapping(value = "/")
    public ModelAndView homeView() {
       return new ModelAndView("home");
    }

}