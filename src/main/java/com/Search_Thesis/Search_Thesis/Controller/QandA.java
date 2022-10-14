package com.Search_Thesis.Search_Thesis.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class QandA {
    @RequestMapping("/Q&A")

    public String  QandA(){
        return "Q&A.html";
    }

}
