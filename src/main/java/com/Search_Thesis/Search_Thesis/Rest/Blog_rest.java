package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Services.JWT_Services;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/ckt")
public class Blog_rest {
    @GetMapping
    public ResponseEntity load_user(HttpServletRequest request){
        HttpSession session =  request.getSession(true) ;
        try{
            String jwt = (String) session.getAttribute("jwt_code");
            JWT_Services jwt_services = new JWT_Services();

            jwt_services.setJwt(jwt);

            JSONObject jsonObject = jwt_services.getPayload();
            String username = (String) jsonObject.get("sub");

            JWT_response jwt_response =  new JWT_response() ;
            jwt_response.setJwt(username);
            return ResponseEntity.ok(jwt_response) ;
        }
        catch (Exception e) {

            System.out.println(e.getMessage());
            return ResponseEntity.ok("null");

        }
    }

    @GetMapping("/load_blog")
    public void load_blog(){
    }
    @PostMapping("/create_blog")
    public void create_blog() {

    }


}
