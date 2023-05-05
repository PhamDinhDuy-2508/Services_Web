package com.Search_Thesis.Search_Thesis.Services.JwtService;

import org.json.JSONObject;
public interface JwtService {
    void setJwt(String jwt) ;
    JSONObject getHeader() ;
    JSONObject getPayload() ;
}
