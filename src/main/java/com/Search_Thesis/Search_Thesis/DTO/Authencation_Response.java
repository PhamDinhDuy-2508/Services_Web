package com.Search_Thesis.Search_Thesis.DTO;

import lombok.Data;

@Data
public class Authencation_Response  {
    private  String JWT ;
    public Authencation_Response(String JWT) {
        this.JWT = JWT;
    }
}
