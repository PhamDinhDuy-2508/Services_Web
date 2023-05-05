package com.Search_Thesis.Search_Thesis.Rest;


import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.JwtService.JwtService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
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
    private User user ;
    @Autowired
    @Qualifier("JwtServices")
    JwtService jwt_services ;
    @Autowired
    private HttpSession httpSession;


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
    @GetMapping( "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response , HttpSession session) {
        Cookie cookies[] =  request.getCookies() ;

        if (cookies != null)
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName());

                if(cookie.getName().equals("login_jwt")){
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }

            }
        session.invalidate();

        session =  request.getSession(true) ;
//        session.setAttribute("jwt_code" , "");
//        session.setMaxInactiveInterval(0);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "delete" ;
    }

    @GetMapping("/get_session")
    public ResponseEntity get_Session_Login(HttpServletRequest request , HttpSession session ){
        httpSession =  request.getSession(false) ;

            try {
                String jwt = (String) httpSession.getAttribute("jwt_code");
                jwt_services.setJwt(jwt);
                System.out.println(jwt);

                JSONObject jsonObject = jwt_services.getPayload();
                String username = (String) jsonObject.get("sub");

                System.out.println(username);

                JWT_response jwt_response = new JWT_response();
                jwt_response.setJwt(username);
                System.out.println(jwt);
                return ResponseEntity.ok(username);
            }
            catch ( Exception e) {
                System.out.println(e.getMessage());
                return  null ;
            }

        }





}
