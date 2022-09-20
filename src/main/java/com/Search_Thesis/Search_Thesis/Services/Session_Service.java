package com.Search_Thesis.Search_Thesis.Services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Service
public class Session_Service {
    private String name ;
    private  String json_list ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJson_list() {
        return json_list;
    }

    public void setJson_list(String json_list) {
        this.json_list = json_list;
    }

    public void Create_Session(HttpServletRequest  request , String name ,  String list_json ) {

        System.out.println(list_json);
        HttpSession session =  request.getSession(true) ;
        session.setAttribute((String) name, list_json );
        session.setMaxInactiveInterval(60*3600);

    }

    public String Get_Session( HttpServletRequest request ,  String name) {

        HttpSession session =  request.getSession(true) ;

        return (String) session.getAttribute(name);
    }
    public void Check_Session_Existed(HttpServletRequest request ,  String name){

    }



}
