package com.Search_Thesis.Search_Thesis.JWT;

import com.Search_Thesis.Search_Thesis.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service("jwtUtils")
public class jwtUtils {
    private String SECRET_KEY = "phamduy2508";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails, int ID) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("password", userDetails.getPassword());
        claims.put("id", ID);

        return createToken(claims, userDetails.getUsername());
    }

    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("First_name", userDetails.getFirst_name());
        claims.put("Last_name", userDetails.getLast_name());
        claims.put("Phone_number", userDetails.getPhone());
        claims.put("home_number", userDetails.getHome_number());
        claims.put("Street", userDetails.getStreet());
        claims.put("State", userDetails.getState());
        claims.put("Email", userDetails.getEmail());
        claims.put("Province", userDetails.getProvince());
        claims.put("Country", userDetails.getCountry());


        return createToken(claims, userDetails.getAccount());
    }

    public String generateToken(String password) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("password", password);
        return createToken(claims, "");

    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
