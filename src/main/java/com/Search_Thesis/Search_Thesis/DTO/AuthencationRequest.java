package com.Search_Thesis.Search_Thesis.DTO;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component("login_request")
@Data
public class AuthencationRequest {

    private  String username ;
    private  String password ;
    private  String save_pass ;

}
