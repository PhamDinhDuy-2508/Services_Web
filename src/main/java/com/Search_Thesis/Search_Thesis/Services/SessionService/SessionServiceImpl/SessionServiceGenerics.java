package com.Search_Thesis.Search_Thesis.Services.SessionService.SessionServiceImpl;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service("SessionServiceGenerics")
public class SessionServiceGenerics<P> {
    public void Create_Session(HttpServletRequest request, String name, P list_json) {
        HttpSession session = request.getSession(true);
        session.setAttribute((String) name, list_json);
        session.setMaxInactiveInterval(60 * 3600);
    }
}
