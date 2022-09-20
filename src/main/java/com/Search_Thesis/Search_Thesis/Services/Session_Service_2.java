package com.Search_Thesis.Search_Thesis.Services;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
@Service
public class Session_Service_2 <P>{
    public void Create_Session(HttpServletRequest request , String name , P list_json ) {

        HttpSession session =  request.getSession(true) ;
        session.setAttribute((String) name, list_json );
        session.setMaxInactiveInterval(60*3600);

    }

    public P Get_Session( HttpServletRequest request ,  String name) {

        HttpSession session =  request.getSession(true) ;

        return (P) session.getAttribute(name);
    }
    public boolean Check_Session_Existed(HttpServletRequest request ,  String name){
        HttpSession session =  request.getSession(true) ;
        if(session.getAttribute(name) == null) {
            return false ;
        }
        else {
            return  true ;
        }

    }
}
