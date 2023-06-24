package com.Search_Thesis.Search_Thesis.DTO;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component("jwt_response")
public class JWTResponse {
    String jwt;
}
