package com.Search_Thesis.Search_Thesis.Rest;


import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.Check_Validate;
import com.Search_Thesis.Search_Thesis.resposity.SignUp_Respository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.IOException;

@RestController
@RequestMapping("/api/ckt")
public class User_Sign_Up {
    private Check_Validate check_validate ;
    @Autowired
    private SignUp_Respository signUp_respository ;
    @Autowired
    private  User user ;



    @PostMapping("/check_register")
    public String check(@Valid @RequestBody Sign_up sign_up , BindingResult bindingResult , HttpServletResponse response) throws IOException {

        check_validate = new Check_Validate();
        if (bindingResult.hasErrors()) {
            return "error";
        } else {
            if (check_email_is_Existed(sign_up.getEmail())) {
                return "3";
            } else if (check_user_name_is_existed(sign_up.getUsername())) {
                return "1";
            } else if (!check_password_is_Corrected(sign_up.getPassword(), sign_up.getRepeat_password())) {
                return "2";
            } else {
                return "oke" ;
            }
        }
    }
    @PostMapping("/register")
    public String  register_user( HttpServletResponse  response,  @RequestBody Sign_up sign_up) throws IOException {
        try {
            check_validate =  new Check_Validate();
            User user1 = new User() ;
            user1 = check_validate.Add_User(sign_up.getUsername() , sign_up.getEmail() ,  sign_up.getPassword() );
            signUp_respository.save(user1) ;
            return "Complete";

        }catch (Exception e) {
            return  e.getMessage().toString() ;
        }
    }

    public boolean check_user_name_is_existed(String user_name) {
        try{

            if( signUp_respository.findByAccount(user_name) == null ){
                return false ;
            }
            return  true ;
        }
        catch (Exception e) {
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
            System.out.println(user1.getEmail());
            return  true ;

        }
        catch (Exception e) {
            return false ;
        }
    }

}
@Component("sign_up")
@Data
@Valid
class Sign_up{
    @NotBlank(message = "name must be not blank")
    @Size(min = 3 ,  message="Name must be at least 3 characters long")
    private String username ;

    @NotBlank(message="Email must not be blank")
    @Email(message = "Please provide a valid email address" )
    private String email ;

    @NotBlank
    @Size(min = 3 , message="Name must be at least 3 characters long" )
    private String password ;

    @NotBlank
    @Size(min = 3 , message="Name must be at least 3 characters long" )
    private String repeat_password ;




}
