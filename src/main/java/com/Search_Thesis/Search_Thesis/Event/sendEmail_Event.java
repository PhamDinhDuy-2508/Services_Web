package com.Search_Thesis.Search_Thesis.Event;

import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;

public class sendEmail_Event extends ApplicationEvent {

    private String email ;

    private HttpServletRequest  request ;
    private  String token ;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public sendEmail_Event(Object source, String email , HttpServletRequest request , String token) {
        super(source);

        this.email = email;
        this.request =  request ;
        this.token = token ;

    }


}
