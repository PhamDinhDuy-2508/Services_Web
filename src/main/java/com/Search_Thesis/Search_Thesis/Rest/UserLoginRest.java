package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.DTO.JWTResponse;
import com.Search_Thesis.Search_Thesis.JWT.JwtTokenProvider;
import com.Search_Thesis.Search_Thesis.DTO.AuthencationRequest;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Security.CustomerDetails;
import com.Search_Thesis.Search_Thesis.Services.LoginServices.Authencation.AuthencationServices;
import com.Search_Thesis.Search_Thesis.Services.SessionService.SessionService;
import com.Search_Thesis.Search_Thesis.repository.SignIn_Respository;
import com.Search_Thesis.Search_Thesis.repository.User_respository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/ckt")


public class UserLoginRest {
    private AuthencationServices authencationServices;
    private SessionService sessionService;

    public UserLoginRest(@Qualifier("AuthencationServices") AuthencationServices authencationServices, @Qualifier("SessionService") SessionService sessionService) {
        this.sessionService = sessionService;
        this.authencationServices = authencationServices;
    }

    @Autowired
    @Qualifier("SessionService")
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> doCheckAccount(@RequestBody AuthencationRequest loginRequest) {
        JWTResponse jwtResponse = authencationServices.authencation(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/authencationRequest")
    public ResponseEntity doCheck(@RequestBody AuthencationRequest authencation_request, HttpServletResponse response, HttpServletRequest request) throws Exception {
        Boolean signal = authencationServices.authencation(authencation_request, request, response);
        return ResponseEntity.ok(signal);
    }

    @PostMapping("/save_session")
    public void save_session(@RequestBody JWTResponse jwt_response) {
        try {
            String token = jwt_response.getJwt();
            String session_str = sessionService.getSession(token);
            if (session_str.isEmpty()) {
                sessionService.createSession(token);
            } else {
                sessionService.restartExpire(token);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

}



