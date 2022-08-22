package com.Search_Thesis.Search_Thesis.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Blog_Controller {
    @RequestMapping("/blog")

    public String display_BlogPage() {
        return "Blog.html" ;
    }
}
