package com.Search_Thesis.Search_Thesis.Services.SessionService.SessionServiceImpl;

import com.Search_Thesis.Search_Thesis.Services.SessionService.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Service("SessionService")
public class SessionServiceImpl implements SessionService {
    private String name ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void createSession(HttpServletRequest  request , String name ,  String list_json ) {
        HttpSession session =  request.getSession(true) ;
        session.setAttribute((String) name, list_json );
        session.setMaxInactiveInterval(60*3600);
    }
    @Override
    public String getSession( HttpServletRequest request ,  String name) {

        HttpSession session =  request.getSession(true) ;
        return (String) session.getAttribute(name);
    }



}
