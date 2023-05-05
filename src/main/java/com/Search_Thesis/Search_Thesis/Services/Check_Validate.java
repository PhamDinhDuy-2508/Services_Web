package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.repository.SignUp_Respository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class Check_Validate {
    @Autowired
    private SignUp_Respository signUp_respository ;
    @Autowired
    private  User user ;

    public boolean check_user_name_is_existed(String user_name) {
        try{

            if( signUp_respository.findByAccount(user_name) == null ){
                return false ;
            }
            return  true ;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false ;
        }

    }
    public boolean check_password_is_Corrected(String password_  , String repeat_password_){

        if(password_.equals(repeat_password_)){
            return true ;
        }
        else {
            return  false ;
        }

    }
    public boolean check_email_is_Existed(String email) {
        try {
            User user1 =  new User() ;
            user1 = signUp_respository.findByEmail(email) ;
            if(signUp_respository.findByEmail(email) == null){
                return false ;
            }
            return  true ;

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false ;
        }
    }
    public User Add_User(String username ,  String email , String password) {
        User user1 =  new User() ;
        user1.setEmail(email);
        user1.setPassword(password);
        user1.setAccount(username);
        return  user1 ;
    }

}
