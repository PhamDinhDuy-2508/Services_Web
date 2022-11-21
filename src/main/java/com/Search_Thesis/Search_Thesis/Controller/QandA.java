package com.Search_Thesis.Search_Thesis.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller

public class QandA {
    private static final String qurstion = "question.html";

    @RequestMapping("/question")

    public String  QandA(){
      return "Q&A.html" ;
    }
    @RequestMapping("/question_info")
    public String  Display_detail(){
        return "question.html" ;
    }
    @RequestMapping("/question_tag")
    public String  Display_tag(){

        return "Topic.html" ;
    }

    @RequestMapping("/Create_question")

    public ModelAndView create(HttpServletRequest  request){
        HttpSession httpSession = request.getSession(true) ;
        String token =  (String) httpSession.getAttribute("jwt_code") ;
        if(token == null) {
            ModelAndView modelAndView = new ModelAndView("/login");
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("Create_Question.html" );

        return modelAndView ;
    }

}
