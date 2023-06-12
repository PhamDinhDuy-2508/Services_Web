package com.Search_Thesis.Search_Thesis.Services.UserService.UserServiceImpl;

import com.Search_Thesis.Search_Thesis.Event.Event.LoadUserEvent;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.JwtService.JwtServiceImpl.JwtServiceImpl;
import com.Search_Thesis.Search_Thesis.repository.SignIn_Respository;
import com.Search_Thesis.Search_Thesis.repository.User_respository;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl {
    @Autowired
    User_respository user_respository ;
    @Autowired
    SignIn_Respository signIn_respository ;
    @Autowired
    User user ;
    private  String username;
    @Async
    @EventListener
    public void Create_USer(LoadUserEvent load_user_event )   {
        username =  load_user_event.getUsername() ;
        user = signIn_respository.findUsersByAccount(username) ;

        HttpSession httpSession =  load_user_event.getRequest().getSession(true);

        httpSession.setAttribute("user" , new Gson().toJson(user).toString() );

        httpSession.setMaxInactiveInterval(60*3500);

    }
    public User load_user_by_username(String username , HttpServletRequest request){

        user = user_respository.getUserByAccount(username) ;

        return user ;
    }
    public String getUserName_from_jwt( HttpServletRequest request){
        HttpSession session =  request.getSession(true) ;

        String _jwt =(String) session.getAttribute("jwt_code") ;
        JwtServiceImpl jwt_services = new JwtServiceImpl();

        jwt_services.setJwt(_jwt);

        JSONObject jsonObject = jwt_services.getPayload();

        String username1 = (String) jsonObject.get("sub");

        return username1 ;
    }
    public int getUSerIdFromJwt(String token) {
        JwtServiceImpl jwt_services = new JwtServiceImpl();
        jwt_services.setJwt(token);
        JSONObject jsonObject = jwt_services.getPayload();
        int user_id = (int) jsonObject.get("id");
        return  user_id ;
    }
    public  boolean checkRequestParam(String username1  , String requestParam){
        if(username1.equals(requestParam)) {
            return true ;
        }
        else {
            return false ;
        }
    }
    public User load_user_from_Session(HttpServletRequest request){
        Gson gson =  new Gson() ;

        var httpSession =  request.getSession(true) ;

        String json_string = (String) httpSession.getAttribute("user");
        User user=gson .fromJson(json_string, User.class);

        return user ;
    }
    public User getUserBytoken(String token){
            user =    user_respository.getUserByResettoken(token) ;
        try {
            if(user_respository == null) {
                return null ;
            }
            else {
                return user ;
            }
        }
        catch (Exception e) {
            return null ;
        }
    }
    public boolean updateUserPassword(String pass , int id){
        try {
            user_respository.updateUserPassword(pass ,(id));
            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return  false ;
        }
    }

    public User getUserByEmailAndUpdateToken(String email , String token ){
        User user1 = user_respository.findUsersByEmail(email);

        try {
            if(user1 != null) {
                user1.setResettoken(token);
                user_respository.save(user1);

            }
            return  user1 ;

        }
        catch (Exception e) {

            System.out.println( e.getMessage());
            return  null ;

        }
    }


}
