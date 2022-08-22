package com.Search_Thesis.Search_Thesis.Model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component("login_request")
@Data
public class Authencation_Request  {

    private  String username ;
    private  String password ;
    private  String save_pass ;

}
