package com.Search_Thesis.Search_Thesis.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class Search_Controller {
    @RequestMapping("/search")
    public  String Search_display() {
        return "Search.html" ;
    }

}
