package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class Login_Services {
    public Login_Services(){}

    public User getuser(String password , String username){
        User user =  new User() ;
        user.setPassword(password);
        user.setAccount(username);
        return  user ;
    }
    public void Create_Cookie(String user_name ,String password  , HttpServletResponse  response , HttpServletRequest request){
        Cookie cookie1 =  new Cookie(user_name ,password) ;
        cookie1.setDomain("localhost");
        cookie1.setPath("/");
        cookie1.setMaxAge(60*3600);



//        cookie.setSecure(true);
//        cookie.setHttpOnly(true);
//
//        cookie1.setSecure(true);
//        cookie1.setHttpOnly(true);

        response.addCookie(cookie1);
        Cookie[] allCookies = request.getCookies();


        System.out.println(allCookies.length);


    }
    public  void Create_Session(String user_name ,String password ,  HttpServletRequest request , HttpServletResponse response){
        HttpSession session  =  request.getSession(true);
        session.setAttribute(user_name ,password);

        session.setMaxInactiveInterval(60*3600);

    }
   public List<Cookie> getCookiesFromPath( HttpServletRequest request, String path) {
        Cookie[] allCookies = request.getCookies();

        if (path == null || path.isEmpty()) { // convert cookie array to cookie list
            return Arrays.asList(allCookies);
        }

        List<Cookie> cookieList = new ArrayList<Cookie>();
        return cookieList;
    }

}
