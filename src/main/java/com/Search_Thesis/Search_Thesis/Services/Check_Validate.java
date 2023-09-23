package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.Role;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.Utils.Constant.Constant;
import com.Search_Thesis.Search_Thesis.repository.SignUp_Respository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashSet;

@Service
@Data
public class Check_Validate {
    private SignUp_Respository signUp_respository ;
    private  Role role ;
    public User Add_User(String username ,  String email , String password) {
        User user1 =  new User() ;
        role.setRoleID(Constant.USER_ROLE);
        user1.setEmail(email);
        user1.setPassword(password);
        user1.setAccount(username);
        user1.setRoles(new LinkedHashSet<>(Arrays.asList(role)));
        return  user1 ;
    }
    @Autowired
    public void setSignUp_respository(SignUp_Respository signUp_respository) {
        this.signUp_respository = signUp_respository;
    }
    @Autowired
    public void setRole(Role role) {
        this.role = role;
    }
}
