package com.Search_Thesis.Search_Thesis.Services;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/v1")
public class Account_Service implements  Account_Interface{


    @PostMapping("/check")

    public User_Login addTodo( @RequestBody User_Login user) {
        System.out.println(user.getPassword());
        return  user ;
    }

    @Override
    public boolean check_account() {
        return false;
    }
}

@Component("login_info")
@Data
class  User_Login {
    private String  user_name ;
    private  String password ;

}
