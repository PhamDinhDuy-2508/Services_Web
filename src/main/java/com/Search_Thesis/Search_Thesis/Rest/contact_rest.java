package com.Search_Thesis.Search_Thesis.Rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/contact")
public class contact_rest {

    @GetMapping()
    public ModelAndView display_view() {
        ModelAndView mav = new ModelAndView("Contact.html");
        return  mav ;
    }



}
