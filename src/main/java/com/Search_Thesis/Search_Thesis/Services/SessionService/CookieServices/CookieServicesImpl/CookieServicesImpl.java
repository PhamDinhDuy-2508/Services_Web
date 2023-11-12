package com.Search_Thesis.Search_Thesis.Services.SessionService.CookieServices.CookieServicesImpl;

import com.Search_Thesis.Search_Thesis.Services.SessionService.CookieServices.CookieServices;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Service("CookieServices")
public class CookieServicesImpl implements CookieServices {
    @Override
    public void createCookie(HttpServletResponse response , String user_name , String password) {
        Cookie cookie1 =  new Cookie(user_name ,password) ;
        cookie1.setDomain("localhost");
        cookie1.setPath("/");
        cookie1.setMaxAge(60*3600);
        response.addCookie(cookie1);
    }

    @Override
    public void deleteCookie(HttpServletResponse response, HttpServletRequest request , String name) {
        Cookie cookies[] =  request.getCookies() ;
        if (cookies != null)
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(name)){
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
    }

    @Override
    public String getCookie(HttpServletRequest request,String name) {
        Cookie cookies[] =  request.getCookies() ;
        if (cookies != null)
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(name)){
                    return cookie.getValue() ;
                }
            }
        return "" ;
    }

}
