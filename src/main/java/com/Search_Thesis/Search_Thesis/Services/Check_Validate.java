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

    public User Add_User(String username ,  String email , String password) {
        User user1 =  new User() ;
        user1.setEmail(email);
        user1.setPassword(password);
        user1.setAccount(username);
        return  user1 ;
    }

}
