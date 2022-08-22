package com.Search_Thesis.Search_Thesis.Event;

import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;

public class Load_User_Event extends ApplicationEvent {

    private  String username ;
    private HttpServletRequest request ;
    public Load_User_Event(Object source, String username , HttpServletRequest request) {
        super(source);
        this.username =  username ;
        this.request = request ;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
