package com.Search_Thesis.Search_Thesis.DTO;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Component("sign_up")
@Data
@Valid
public class SignUpDTO {
    @NotBlank(message = "name must be not blank")
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String username;
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid email address")
    private String email;
    @NotBlank
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String password;
    @NotBlank
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String repeat_password;
}
