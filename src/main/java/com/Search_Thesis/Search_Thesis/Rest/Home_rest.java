package com.Search_Thesis.Search_Thesis.Rest;


import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.JwtService.JwtService;
import com.Search_Thesis.Search_Thesis.Services.SessionService.CookieServices.CookieServices;
import com.Search_Thesis.Search_Thesis.Services.SessionService.SessionService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController

@RequestMapping("/home")

public class Home_rest {
    @Autowired
    @Qualifier("JwtServices")
    JwtService jwt_services ;
    @Autowired
    SessionService sessionService ;
    @Autowired
    CookieServices cookieServices ;


    @RequestMapping("/**")
    public void handleRequestById (HttpSession httpSession , Model model, HttpServletRequest request) {

        model.addAttribute("test" , "duy") ;
        model.addAttribute("sessionId", httpSession.getId());
        httpSession.setAttribute("test" , "duy");
        httpSession.setMaxInactiveInterval(6000);

    }

    @GetMapping()
    public ModelAndView  display(){

        ModelAndView mav = new ModelAndView("home.html");
        return mav;
    }
    @GetMapping( "/logout")
    public String logoutPage(@CookieValue("login_jwt") String token ,  HttpServletRequest request, HttpServletResponse response) {

        cookieServices.deleteCookie(response , request , "login_jwt");
        sessionService.deleteSession(token);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "delete" ;
    }

    @GetMapping("/get_session")
    public ResponseEntity<?> get_Session_Login(@CookieValue("login_jwt") String token ){
            try {
                String username =  sessionService.getSession(token);
                return ResponseEntity.ok(username);
            }
            catch ( Exception e) {
                return  null ;
            }
        }
}
