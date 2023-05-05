package com.Search_Thesis.Search_Thesis.Services.SessionService;

import javax.servlet.http.HttpServletRequest;

public interface SessionService {
    void createSession(HttpServletRequest request , String name , String list_json) ;
    String getSession(HttpServletRequest request ,  String name) ;
}
