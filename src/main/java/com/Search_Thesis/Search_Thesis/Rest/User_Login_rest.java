package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Model.Authencation_Request;
import com.Search_Thesis.Search_Thesis.Model.Authencation_Response;
import com.Search_Thesis.Search_Thesis.Model.Login_info;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.Login_Services;
import com.Search_Thesis.Search_Thesis.Services.customerDetailsServices;
import com.Search_Thesis.Search_Thesis.resposity.SignIn_Respository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/ckt")
@SessionAttributes


public class User_Login_rest {

    @Autowired
    private User user ;

    @Autowired
    private AuthenticationManager authenticationManager  ;

   private UserDetailsService userDetailsService ;
    @Autowired
    SignIn_Respository signIn_respository ;
    @Autowired
    private com.Search_Thesis.Search_Thesis.JWT.jwtUtils jwtUtils ;

    @PostMapping("/login")
    public boolean doCheckAccount (HttpServletRequest request) {

        Login_Services login_services =  new Login_Services() ;
        Cookie[] allCookies = request.getCookies();

        return true ;
    }




    @PostMapping("/authencationRequest")
    public ResponseEntity<?> doCheck(@RequestBody Authencation_Request authencation_request , HttpServletResponse response , HttpServletRequest request) throws  Exception {
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
            final UserDetails userDetails = customerDetailsServices.loadUserByUsername(authencation_request.getUsername()) ;

            final  String jwt = jwtUtils.generateToken(userDetails) ;

            login_services.Create_Cookie("login_jwt" ,jwt, response,request);
            login_services.Create_Cookie("save_pass" , authencation_request.getSave_pass(), response , request);


            return ResponseEntity.ok(new Authencation_Response(jwt));

        }catch (Exception e) {
            return ResponseEntity.ok(new Authencation_Response("Wrong"))  ;
        }
    }

    @PostMapping("/save_session")
    public  void save_session(@RequestBody JWT_response jwt_response , HttpServletRequest request ){
       Login_Services login_services = new Login_Services();
//       login_services.Create_Session("jwt_code" , authencation_response.getJwt(), request, response);
        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute("jwt_code" , jwt_response.getJwt());

        httpSession.setMaxInactiveInterval(60*3600);
        HttpSession Session = request.getSession() ;
        String test = (String) Session.getAttribute("jwt_code");
        System.out.println(test);
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


