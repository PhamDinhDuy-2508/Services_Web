package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.DTO.JWTResponse;
import com.Search_Thesis.Search_Thesis.Services.JwtService.JwtServiceImpl.JwtServiceImpl;
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
            JwtServiceImpl jwt_services = new JwtServiceImpl();

            jwt_services.setJwt(jwt);

            JSONObject jsonObject = jwt_services.getPayload();
            String username = (String) jsonObject.get("sub");

            JWTResponse jwt_response =  new JWTResponse() ;
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
