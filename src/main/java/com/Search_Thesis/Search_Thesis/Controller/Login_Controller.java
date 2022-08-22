package com.Search_Thesis.Search_Thesis.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Login_Controller {
    @RequestMapping("/login")
    public String display() {
        return "login.html" ;
    }
}
