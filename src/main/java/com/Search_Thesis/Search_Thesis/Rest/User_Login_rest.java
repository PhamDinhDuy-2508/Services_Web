package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.JWT.JwtTokenProvider;
import com.Search_Thesis.Search_Thesis.DTO.Authencation_Request;
import com.Search_Thesis.Search_Thesis.DTO.Authencation_Response;
import com.Search_Thesis.Search_Thesis.DTO.Login_info;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Security.CustomerDetails;
import com.Search_Thesis.Search_Thesis.Services.Login_Services;
import com.Search_Thesis.Search_Thesis.Services.SessionService.SessionService;
import com.Search_Thesis.Search_Thesis.Services.customerDetailsServices;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/ckt")


public class User_Login_rest {

    @Autowired
    private User user ;



   private UserDetailsService userDetailsService ;
    @Autowired
    SignIn_Respository signIn_respository ;
    @Autowired
    private com.Search_Thesis.Search_Thesis.JWT.jwtUtils jwtUtils ;

    @Autowired
    @Qualifier("SessionService")
    private SessionService session_serviceImpl;

    @Autowired
    User_respository user_respository ;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
//    @Autowired
//    PasswordEncoder passwordEncoder;




    @PostMapping("/login")

    public ResponseEntity< ? > doCheckAccount (@RequestBody Authencation_Request loginRequest) {

        String jwt = "" ;


            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );
                System.out.println("TTTT" + authentication);
                jwt = tokenProvider.generateToken((CustomerDetails) authentication.getPrincipal());

            }
            catch (Exception e) {
//                System.out.println("Test " + authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(
//                                loginRequest.getUsername(),
//                                loginRequest.getPassword()
//                        )));

                System.out.println(e.getMessage());
            }

//            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println(jwt);




        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context

        // Trả về jwt cho người dùng.
        JWT_response  jwt_response =  new JWT_response() ;

        jwt_response.setJwt(jwt);


        return ResponseEntity.ok(jwt_response);
    }

    @PostMapping("/authencationRequest")
    public ResponseEntity  doCheck(@RequestBody Authencation_Request authencation_request , HttpServletResponse response , HttpServletRequest request) throws  Exception {
        Login_Services login_services = new Login_Services() ;

        customerDetailsServices customerDetailsServices = new customerDetailsServices(signIn_respository);
//        try {
//            System.out.println("check");
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authencation_request.getUsername(),
//                            authencation_request.getPassword()));
//        }
//        catch (Exception e) {
//            throw new Exception("Wrong password or username" , e) ;
//        }
        try{

             UserDetails userDetails = customerDetailsServices.loadUserByUsername(authencation_request.getUsername()) ;
             User user1 =  user_respository.findUsersByAccount(userDetails.getUsername()) ;
            if(userDetails == null) {
                System.out.println("NONE");
                return ResponseEntity.notFound().build();

            }
            else {
                final String jwt = jwtUtils.generateToken(userDetails ,  user1.getUser_id());

                login_services.Create_Cookie("login_jwt", jwt, response, request);
                login_services.Create_Cookie("save_pass", authencation_request.getSave_pass(), response, request);

                return ResponseEntity.ok(new Authencation_Response("wrong"));
            }

        }catch (Exception e) {
            System.out.println("Wrong");
            return  ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save_session")
    public  void save_session(@RequestBody JWT_response jwt_response , HttpServletRequest request ,
                              HttpSession session  ){

        HttpSession httpSession =  request.getSession(false);
        Login_Services login_services = new Login_Services();
        String token =  jwt_response.getJwt();
        if(session.getAttribute("jwt_code") == null) {
            System.out.println("NON EXISTED");
            httpSession.setAttribute("jwt_code" , jwt_response.getJwt());
            httpSession.setMaxInactiveInterval(60*3600);
        }
        else {

            httpSession.setAttribute("jwt_code" , "");
            httpSession.removeAttribute("jwt_code");
            httpSession.setMaxInactiveInterval(0);
            System.out.println(token);
            httpSession.setAttribute("jwt_code" , token);
            httpSession.setMaxInactiveInterval(60*3600);
            System.out.println( httpSession.getAttribute("jwt_code"));
        }

    }

    @GetMapping("/process")
    public boolean processCheckAccount (@Valid @RequestBody Login_info login_info , HttpServletRequest request,
                                   HttpServletResponse respons ) {
        Login_Services login_services =  new Login_Services() ;
        return true ;
    }
    @GetMapping("/create-spring-cookie")
    public ResponseEntity setCookie() {

        ResponseCookie resCookie = ResponseCookie.from("user-id2", "c2FtLnNtaXRoQGV4YW1wbGUuY29t")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(1 * 24 * 60 * 60)
                .domain("localhost")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookie.toString()).build();

    }

    @GetMapping("/delete-spring-cookie")
    public ResponseEntity deleteCookie() {

        // create a cookie
        ResponseCookie resCookie = ResponseCookie.from("user-id", null)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookie.toString()).build();
    }

    @GetMapping("/read-spring-cookie")
    public String readCookie(@CookieValue(name = "user-id", defaultValue = "default-user-id") String cookieName) {
        return String.format("value of the cookie with name user-id is: %s", cookieName);
    }


}
@Data
@Component("jwt_response")
 class JWT_response{
    String jwt ;
}
@Data
class loginrequest{
    String username ;
    String password ;
}


