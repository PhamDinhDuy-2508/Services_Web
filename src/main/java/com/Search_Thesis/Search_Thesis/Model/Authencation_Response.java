package com.Search_Thesis.Search_Thesis.Model;

import lombok.Data;

@Data
public class Authencation_Response  {

    private  String JWT ;
    public Authencation_Response(String JWT) {
        this.JWT = JWT;
    }
}
