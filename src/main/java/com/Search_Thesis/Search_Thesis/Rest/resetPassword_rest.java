package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.UserService.UserServiceImpl.UserServiceImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/reset_pass")
public class resetPassword_rest {

    @Autowired
    UserServiceImpl user_service;
    @Autowired
    User user ;

    @GetMapping()
    public ModelAndView display(@RequestParam("token") String token) {
        ModelAndView mav = new ModelAndView("reset_pass.html");
        return mav;
    }
    @GetMapping("/load_user")
    public ResponseEntity response(@RequestParam("token" )  String token ){
        try {
            System.out.println(token);
            user =   user_service.getUserBytoken(token);
            user_res user_res =  new user_res() ;

            return  ResponseEntity.ok(user_res) ;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(new user_res()) ;
        }
    }
    @PostMapping("/update")
    public ResponseEntity reset_pass(@RequestBody reset_response reset_response ,@RequestParam("token" )  String token) {
        user = user_service.getUserBytoken(token);

        try {

            user_service.updateUserPassword(reset_response.getPass(), user.getUser_id());


            return ResponseEntity.ok(user);
        } catch (Exception e) {

            System.out.println(e.getMessage());
            return ResponseEntity.ok(new user_res());
        }
    }
}
@Data
@Valid
class  reset_response{
    @Size(min = 3 , message = "wrong")
    private String pass ;
}
@Data
class  user_res{
    private  String response ;
}

