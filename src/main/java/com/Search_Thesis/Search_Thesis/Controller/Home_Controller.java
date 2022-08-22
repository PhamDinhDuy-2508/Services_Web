package com.Search_Thesis.Search_Thesis.Controller;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller

public class Home_Controller {

    public String DisplayHomePage(HttpServletRequest request) {

        return "home.html";

    }


}
