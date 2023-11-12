package com.Search_Thesis.Search_Thesis.Services.LoginServices.Authencation.AuthencationService;

import com.Search_Thesis.Search_Thesis.DTO.AuthencationRequest;
import com.Search_Thesis.Search_Thesis.DTO.JWTResponse;
import com.Search_Thesis.Search_Thesis.JWT.JwtTokenProvider;
import com.Search_Thesis.Search_Thesis.Security.CustomerDetails;
import com.Search_Thesis.Search_Thesis.Security.UserServices_Sercurity;
import com.Search_Thesis.Search_Thesis.Services.LoginServices.Authencation.AuthencationServices;
import com.Search_Thesis.Search_Thesis.Services.SessionService.CookieServices.CookieServices;
import com.Search_Thesis.Search_Thesis.Services.SessionService.SessionService;
import com.Search_Thesis.Search_Thesis.repository.SignIn_Respository;
import com.Search_Thesis.Search_Thesis.repository.User_respository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service("AuthencationServices")

public class AuthencationServiceImpl implements AuthencationServices {
    private SignIn_Respository signIn_respository;
    private User_respository user_respository;
    private com.Search_Thesis.Search_Thesis.JWT.jwtUtils jwtUtils;
    private CookieServices cookieServices;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private SessionService sessionService;

    @Autowired
    private UserServices_Sercurity userServicesSercurity ;

    @Autowired
    public AuthencationServiceImpl(SignIn_Respository signIn_respository,
                                   User_respository user_respository,
                                   @Qualifier("jwtUtils") com.Search_Thesis.Search_Thesis.JWT.jwtUtils jwtUtils,
                                   @Qualifier("CookieServices") CookieServices cookieServices,
                                   AuthenticationManager authenticationManager,
                                   JwtTokenProvider tokenProvider, @Qualifier("SessionService") SessionService sessionService) {
        this.signIn_respository = signIn_respository;
        this.user_respository = user_respository;
        this.jwtUtils = jwtUtils;
        this.cookieServices = cookieServices;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.sessionService = sessionService;
    }

    @Override
    public Boolean authencation(AuthencationRequest authencationRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authencationRequest.getUsername(),
                            authencationRequest.getPassword()
                    )
            );
            final String jwt = jwtUtils.generateToken((CustomerDetails)authentication.getPrincipal(),1);
            SecurityContextHolder.getContext().setAuthentication(authentication);


            cookieServices.createCookie(response, "login_jwt", jwt);
            cookieServices.createCookie(response, "save_pass", authencationRequest.getSave_pass());
            sessionService.createSession(jwt);

            return Boolean.TRUE;

        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public JWTResponse authencation(AuthencationRequest loginRequest) {
        String jwt= "";
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            jwt = tokenProvider.generateToken((CustomerDetails) authentication.getPrincipal());

        } catch (Exception e) {
            throw new NullPointerException();
        }
        JWTResponse jwt_response = new JWTResponse();

        jwt_response.setJwt(jwt);
        return jwt_response;
    }
}
