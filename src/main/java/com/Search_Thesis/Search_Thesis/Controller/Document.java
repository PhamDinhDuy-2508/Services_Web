package com.Search_Thesis.Search_Thesis.Controller;

import com.Search_Thesis.Search_Thesis.Services.JWT_Services;
import com.Search_Thesis.Search_Thesis.Services.User_Serrvices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j

@Controller


public class Document {

    @Autowired
    User_Serrvices user_serrvices;

    @Autowired
    JWT_Services jwt_services;

    @RequestMapping("/document")
    public ModelAndView display_view() {
        System.out.println("test");
        ModelAndView modelAndView = new ModelAndView("Document_Admin.html");

        return modelAndView;
    }

    @RequestMapping("/document_upload")
    public ModelAndView display_view2() {
        ModelAndView modelAndView = new ModelAndView("upload_document.html");
        return modelAndView;
    }

    @GetMapping("/load_user_token")
    public ModelAndView user_token(HttpServletRequest res) {
        HttpSession session = res.getSession(true);
        String token = (String) session.getAttribute("jwt_code");
        ModelAndView modelAndView = new ModelAndView("redirect:/document?token=" + token);
        return modelAndView;
    }

    @GetMapping("/load_user")
    public ModelAndView user_token2(HttpServletRequest res) {
        HttpSession session = res.getSession(true);
        String token = (String) session.getAttribute("jwt_code");
        ModelAndView modelAndView = new ModelAndView("redirect:/document?token=" + token);
        return modelAndView;
    }
}
