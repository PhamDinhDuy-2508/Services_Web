package com.Search_Thesis.Search_Thesis.Services;


import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class JWT_Services {
    private  String jwt ;

    private  String Header ;
    private String Payload ;

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }


    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

    public JSONObject getHeader() {
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String part[]  = this.jwt.split("\\.") ;

        String header =  new String(decoder.decode(part[0])) ;

        JSONObject json = new JSONObject(header);


        return json ;
    }

    public void setHeader(String header) {
        Header = header;
    }

    public JSONObject getPayload() {

        Base64.Decoder decoder = Base64.getUrlDecoder();

        String part[]  = this.jwt.split("\\.") ;

        String header =  new String(decoder.decode(part[1])) ;

        JSONObject json = new JSONObject(header);


        return json ;
    }

    public void setPayload(String payload) {
        Payload = payload;
    }
}
