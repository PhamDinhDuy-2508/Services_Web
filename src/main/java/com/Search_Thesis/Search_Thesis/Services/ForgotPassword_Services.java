package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.UserService.UserServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForgotPassword_Services {
    public String random_token  ;

    @Autowired
    UserServiceImpl user_service;

    @Autowired
    User user ;

    @Autowired
    EmailServices email_services ;


    public User checkUser(String email , String token) {
        System.out.println(Thread.currentThread().getName());

        user =  user_service.getUserByEmailAndUpdateToken(email, token ) ;
        return  user ;
    }

}
