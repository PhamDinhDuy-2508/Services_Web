package com.Search_Thesis.Search_Thesis.Controller;

import com.Search_Thesis.Search_Thesis.Services.JwtService.JwtService;
import com.Search_Thesis.Search_Thesis.Services.SessionService.SessionService;
import com.Search_Thesis.Search_Thesis.Services.UserService.UserServiceImpl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class Document {

    @Autowired
    UserServiceImpl user_service;

    @Autowired
    @Qualifier("JwtServices")
    JwtService jwt_services;
    private SessionService sessionService  ;
    @Autowired
    @Qualifier("SessionService")
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }


    @RequestMapping("/document")
    public ModelAndView display_view() {
        ModelAndView modelAndView = new ModelAndView("Document_Admin.html");

        return modelAndView;
    }
    @RequestMapping("/document/1")
    public ModelAndView display_view3() {
        ModelAndView modelAndView = new ModelAndView("upload_document.html");
        return modelAndView;
    }

    @RequestMapping("/document_upload")
    public ModelAndView display_view2() {
        ModelAndView modelAndView = new ModelAndView("upload_document.html");
        return modelAndView;
    }
    @RequestMapping("/edit_document")
    public ModelAndView display_view_edit() {
        ModelAndView modelAndView = new ModelAndView("Edit_Document.html");
        return modelAndView;
    }
    @GetMapping("/load_user_token_Edit_page")
    public ModelAndView user_token_Edit_view(HttpServletRequest res) {
        HttpSession session = res.getSession(true);
        String token = (String) session.getAttribute("jwt_code");
        if(token == null) {
            ModelAndView modelAndView = new ModelAndView("/login");
            return modelAndView;

        }
        else {
            ModelAndView modelAndView = new ModelAndView("redirect:/edit_document?token=" + token);
            return modelAndView;

        }

    }
    @GetMapping("/load_user_token")
    public ModelAndView user_token(@CookieValue(name = "login_jwt") String token , HttpServletRequest res) {

        String userToken = "" ;
        try {
            userToken =    sessionService.getSession(token) ;
            if(userToken!=null) {
                ModelAndView modelAndView = new ModelAndView("redirect:/document?token=" + token);
                return modelAndView;
            }
            ModelAndView modelAndView = new ModelAndView("/login");
            return modelAndView;

        }
        catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("/login");
            return modelAndView;

        }

    }
    @GetMapping("/load_category")
    public ModelAndView load_Category(@CookieValue(name = "login_jwt") String token , @RequestParam("category") String categoty) {
        String userToken  = "" ;
        try {
             userToken =    sessionService.getSession(token) ;
            ModelAndView modelAndView = new ModelAndView("redirect:/document?category=" + categoty);
            return modelAndView;
        }
        catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("/login");
            return modelAndView;

        }





    }

    @GetMapping("/load_user")
    public ModelAndView user_token2(HttpServletRequest res) {
        HttpSession session = res.getSession(true);
        String token = (String) session.getAttribute("jwt_code");
        ModelAndView modelAndView = new ModelAndView("redirect:/document?token=" + token);
        return modelAndView;
    }

}
