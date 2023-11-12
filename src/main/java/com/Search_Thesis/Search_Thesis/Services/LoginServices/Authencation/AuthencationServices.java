package com.Search_Thesis.Search_Thesis.Services.LoginServices.Authencation;

import com.Search_Thesis.Search_Thesis.DTO.AuthencationRequest;
import com.Search_Thesis.Search_Thesis.DTO.JWTResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthencationServices {
    Boolean authencation(AuthencationRequest authencationReques , HttpServletRequest request , HttpServletResponse response)  ;
    JWTResponse authencation(AuthencationRequest authencationRequest) ;


}
