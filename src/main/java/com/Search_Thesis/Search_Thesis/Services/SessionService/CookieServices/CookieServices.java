package com.Search_Thesis.Search_Thesis.Services.SessionService.CookieServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CookieServices {
    void createCookie(HttpServletResponse response , String key , String value) ;
    void deleteCookie(HttpServletResponse response, HttpServletRequest request , String name)  ;
    String getCookie(HttpServletRequest  request, String name) ;
}
