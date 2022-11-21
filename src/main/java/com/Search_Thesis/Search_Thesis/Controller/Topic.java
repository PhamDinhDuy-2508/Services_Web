package com.Search_Thesis.Search_Thesis.Controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/question")
public class Topic {
    private static final String VIEW_TOPOLOGIE = "question.html";

    @GetMapping("/topic")


    public ModelAndView redirectWithUsingRedirectPrefix(ModelMap model , HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("Document_Admin.html");
        return modelAndView;
    }
}
