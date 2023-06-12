package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service("LoginServices")
public class Login_Services {
    public Login_Services(){}
    public User getUser(String password , String username){
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
        response.addCookie(cookie1);
    }

}
