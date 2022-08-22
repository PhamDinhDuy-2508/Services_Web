package com.Search_Thesis.Search_Thesis.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Register_Controller {
    @RequestMapping("/sign_up")
    public String sign_up() {
        return "register.html" ;
    }
}
