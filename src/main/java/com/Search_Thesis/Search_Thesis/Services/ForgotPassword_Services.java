package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Event.sendEmail_Event;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.UserService.UserServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Service
public class ForgotPassword_Services {
    public String random_token  ;

    @Autowired
    UserServiceImpl user_service;

    @Autowired
    User user ;

    @Autowired
    Email_Services email_services ;


    public User checkUser(String email , String token) {
        System.out.println(Thread.currentThread().getName());

        user =  user_service.getUserByEmailAndUpdateToken(email, token ) ;
        return  user ;
    }

    @Async
    @EventListener
    public void ForgotPassword(sendEmail_Event sendEmail_event) throws MessagingException, UnsupportedEncodingException {

        user =  user_service.getUserByEmailAndUpdateToken(sendEmail_event.getEmail(), sendEmail_event.getToken()) ;
         if(user == null) {
             return;
         }
         else {
             String resetPasswordLink = Utility.getSiteURL(sendEmail_event.getRequest()) + "/reset_pass?token=" + sendEmail_event.getToken();
//             System.out.println(resetPasswordLink);

             email_services.Send_Email(sendEmail_event.getEmail(),   resetPasswordLink);
        }
    }
    public User getByToken(String token){
      return  null ;
    }
}
class Utility {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
