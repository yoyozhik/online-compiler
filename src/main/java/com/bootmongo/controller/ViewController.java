package com.bootmongo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class ViewController {

    @RequestMapping(value = "/")
    public ModelAndView uploadView() {
       return new ModelAndView("index");
    }

    @RequestMapping(value = "/sales-train-create-problem")
    public ModelAndView redirectToSalesTrain() {
       return new ModelAndView("sales-train-create-problem");
    }
}