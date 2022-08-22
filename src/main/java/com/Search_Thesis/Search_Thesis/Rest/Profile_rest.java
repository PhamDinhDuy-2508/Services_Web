package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Event.Load_User_Event;
import com.Search_Thesis.Search_Thesis.JWT.jwtUtils;
import com.Search_Thesis.Search_Thesis.Model.Authencation_Response;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.JWT_Services;
import com.Search_Thesis.Search_Thesis.Services.User_Serrvices;
import com.Search_Thesis.Search_Thesis.resposity.User_respository;
import com.google.gson.Gson;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RequestMapping("/profile")
@RestController
public class Profile_rest {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher ;

    @Autowired
    User_Serrvices user_serrvices ;

    @Autowired
    User_respository user_respository ;

    @Autowired
    User user ;
    @Autowired
    private jwtUtils jwtUtils ;

    @Autowired
    JWT_Services jwt_services ;


    @GetMapping()
    public ModelAndView display(@RequestParam("username") String username) {
        ModelAndView mav = new ModelAndView("proffile.html");
        return mav;
    }

    @GetMapping("/load_data")
    public ResponseEntity load_data(@RequestParam("username") String username , HttpServletRequest request){
        HttpSession httpSession =  request.getSession(true) ;

        String _jwt =(String) httpSession.getAttribute("jwt_code") ;

        JWT_Services jwt_services = new JWT_Services();

        jwt_services.setJwt(_jwt);

        JSONObject jsonObject = jwt_services.getPayload();

        String username1 = (String) jsonObject.get("sub");

            try {


                applicationEventPublisher.publishEvent(new Load_User_Event(this,username1, request));

                user =  user_serrvices.load_user_by_username(username1,  request);


                return ResponseEntity.ok(user) ;

            }
            catch (Exception e){
                JWT_response jwt_responses = new JWT_response() ;
                jwt_responses.setJwt(""); ;
                return ResponseEntity.ok(jwt_responses);
        }


    }
    @PutMapping("/update_info")
    public  ResponseEntity update_info(@RequestBody Profile_update_response profile_update_response , HttpServletRequest request){
        Gson gson =  new Gson() ;

        var httpSession =  request.getSession(true) ;

        String json_string = (String) httpSession.getAttribute("user");

        User user=gson .fromJson(json_string, User.class);


        if(user == null){
            return ResponseEntity.ok("Error") ;
        }
        else {
            User user1 = profile_update_response.save_into_user(user);
            this.user_respository.save(user1) ;
            return  ResponseEntity.ok(user1) ;
        }
    }
    @GetMapping("/load_pass")

    public ResponseEntity load_pass( @RequestParam("username") String username , HttpServletRequest request){

            if(user_serrvices.checkRequestParam(user_serrvices.getUserName_from_jwt(request) , username)){
               String jwt_response =  jwtUtils.generateToken(user_serrvices.load_user_from_Session(request).getPassword());

               return  ResponseEntity.ok(new Authencation_Response(jwt_response)) ;
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

            }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @PutMapping("/update_pass")

    public  ResponseEntity update_pass  (@Valid  @RequestBody Password_response password_response,
                               @RequestParam("username") String username ,
                               HttpServletRequest request ) throws IllegalStateException {
        Error_res error_res =  new Error_res() ;

        if(user_serrvices.checkRequestParam(user_serrvices.getUserName_from_jwt(request) , username)){
            User user1 =  user_serrvices.load_user_from_Session(request) ;
            if(!user1.getPassword().equals(password_response.getOld_password())) {
                error_res.setType("1");
                error_res.setError("mật Khẩu không chính xác");
               return ResponseEntity.ok(error_res) ;
            }
            else if(!password_response.getConfirm_password().equals(password_response.getNew_password())){
                error_res.setType("1");
                error_res.setError("mật Khẩu  không khớp với mật khẩu vừa tạo");
                return ResponseEntity.ok(error_res) ;
            }
            else {
                System.out.println(user_serrvices.updateUserPassword(user1.getPassword() ,  user1.getUser_id())) ;
            }
        }

        return  ResponseEntity.ok(error_res) ;
    }

}
@Data
class  User_request{
    private String username ;
}
@Data
class  Profile_update_response{

    private String first_name ;
    private String last_name ;
    private String email ;
    private String home_number ;
    private String country ;
    private String province ;
    private String state ;
    private String street ;
    private  String phone_number ;

    public  User save_into_user( User user){
        user.setFirst_name(getFirst_name());
        user.setLast_name(getLast_name());
        user.setEmail(getEmail());
        user.setHome_number(getHome_number());
        user.setCountry(getCountry());
        user.setProvince(getProvince());
        user.setState(getState());
        user.setStreet(getStreet());
        user.setPhone(getPhone_number());

        return  user ;
    }

    @Override
    public String toString() {
        return "Profile_update_response{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", home_number='" + home_number + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", state='" + state + '\'' +
                ", street='" + street + '\'' +
                ", phone_number='" + phone_number + '\'' +
                '}';

    }
}
@Component("p_res")
@Data
@Valid
class  Password_response
{

    private String old_password ;

    @NotBlank
    @Size(min = 3 , message="Name must be at least 3 characters long" )
    private String new_password ;

    @NotBlank
    @Size(min = 3 , message="Name must be at least 3 characters long" )
    private String confirm_password ;

    @Override
    public String toString() {
        return "Password_response{" +
                "old_password='" + old_password + '\'' +
                ", new_password='" + new_password + '\'' +
                ", confirm_password='" + confirm_password + '\'' +
                '}';
    }
}
@Data
class Error_res{
    private String type ;
    private  String error ;

}

