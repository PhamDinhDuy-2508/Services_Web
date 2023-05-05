package com.Search_Thesis.Search_Thesis.Services.JwtService.JwtServiceImpl;
import com.Search_Thesis.Search_Thesis.Services.JwtService.JwtService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service("JwtServices")
public class JwtServiceImpl implements JwtService {
    private String jwt;
    @Override
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }
    @Override
    public JSONObject getHeader() {

        Base64.Decoder decoder = Base64.getUrlDecoder();

        String part[] = this.jwt.split("\\.");

        String header = new String(decoder.decode(part[0]));

        JSONObject json = new JSONObject(header);

        return json;
    }
    @Override
    public JSONObject getPayload() {

        Base64.Decoder decoder = Base64.getUrlDecoder();

        String part[] = this.jwt.split("\\.");

        String header = new String(decoder.decode(part[1]));

        JSONObject json = new JSONObject(header);

        return json;
    }

}
