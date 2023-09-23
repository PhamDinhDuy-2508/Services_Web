package com.Search_Thesis.Search_Thesis.Rest;


import com.Search_Thesis.Search_Thesis.DTO.SignUpDTO;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.Check_Validate;
import com.Search_Thesis.Search_Thesis.Services.RegisterService.RegisterService;
import com.Search_Thesis.Search_Thesis.repository.SignUp_Respository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ckt")
public class UserSignUp {
    private Check_Validate check_validate;
    @Autowired
    private SignUp_Respository signUp_respository;
    @Autowired
    private User user;
    @Autowired
    private RegisterService registerService;

    @PostMapping("/check_register")
    public ResponseEntity<String> check(@Valid @RequestBody SignUpDTO sign_up, BindingResult bindingResult) throws IOException {
        Map<String, String> errors = registerService.validate(sign_up);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok("error");
        } else {
            if (errors.isEmpty()) {
                return ResponseEntity.ok("ok");
            } else {
                List<String> errorList = new ArrayList<>(errors.keySet());
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < errorList.size(); i++) {
                    stringBuilder.append(errorList.get(i) + ",");
                }
                stringBuilder.deleteCharAt(stringBuilder.length()-1);
                return ResponseEntity.ok(stringBuilder.toString());
            }
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register_user(@RequestBody SignUpDTO sign_up) throws IOException {
        Map<String, String> errors = registerService.validate(sign_up);
        try {
            if (errors.isEmpty()) {
                registerService.registerUser(sign_up);
                return ResponseEntity.ok("ok");
            } else {
                return ResponseEntity.status(409).body("Cannot Register");
            }
        } catch (Exception e) {
            return ResponseEntity.status(409).body( e.getMessage().toString());
        }
    }

    public boolean check_user_name_is_existed(String user_name) {
        try {

            if (signUp_respository.findByAccount(user_name) == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean check_password_is_Corrected(String password_, String repeat_password_) {
        if (password_.equals(repeat_password_)) {
            return true;
        } else {

            return false;
        }
    }

    public boolean check_email_is_Existed(String email) {
        try {
            User user1 = new User();
            user1 = signUp_respository.findByEmail(email);
            if (signUp_respository.findByEmail(email) == null) {
                return false;
            }
            System.out.println(user1.getEmail());
            return true;

        } catch (Exception e) {
            return false;
        }
    }

}
