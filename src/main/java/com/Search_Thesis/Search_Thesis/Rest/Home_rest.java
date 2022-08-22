package com.Search_Thesis.Search_Thesis.Rest;


import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.JWT_Services;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@SessionAttributes("jwt_code")
@RequestMapping("/home")

public class Home_rest {
    @Autowired
    private User user ;


    @RequestMapping("/**")
    public void handleRequestById (HttpSession httpSession , Model model, HttpServletRequest request) {

        model.addAttribute("test" , "duy") ;
        model.addAttribute("sessionId", httpSession.getId());
        httpSession.setAttribute("test" , "duy");
        httpSession.setMaxInactiveInterval(6000);

        HttpSession Session = request.getSession() ;

        String test = (String) Session.getAttribute("test");

    }

    @GetMapping()
    public ModelAndView  display(){

        ModelAndView mav = new ModelAndView("home.html");
        return mav;
    }

    @GetMapping("/get_session")
    public ResponseEntity get_Session_Login(HttpServletRequest request ){

        HttpSession Session = request.getSession(true) ;

        try {

            String jwt = (String) Session.getAttribute("jwt_code");
            JWT_Services jwt_services = new JWT_Services();

            jwt_services.setJwt(jwt);

            JSONObject jsonObject = jwt_services.getPayload();
            String username = (String) jsonObject.get("sub");

            JWT_response jwt_response =  new JWT_response() ;
            jwt_response.setJwt(username);
            return ResponseEntity.ok(jwt_response) ;

        }
        catch (Exception e) {

            System.out.println(e.getMessage());
            return ResponseEntity.ok("null");

        }
    }



}
