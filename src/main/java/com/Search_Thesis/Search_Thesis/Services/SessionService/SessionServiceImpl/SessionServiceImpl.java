package com.Search_Thesis.Search_Thesis.Services.SessionService.SessionServiceImpl;

import com.Search_Thesis.Search_Thesis.Services.SessionService.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.cfg.defs.CreditCardNumberDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;
import  com.Search_Thesis.Search_Thesis.JWT.jwtUtils ;

@Slf4j
@Service("SessionService")
public class SessionServiceImpl implements SessionService {
    private final String HashKey = "session";
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private RedisTemplate redisTemplate;

    @Autowired
    private void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private jwtUtils jwtUtils ;
    @Autowired
    public void setJwtUtils(com.Search_Thesis.Search_Thesis.JWT.jwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void createSession(String token) {
        String username = jwtUtils.extractUsername(token) ;
        redisTemplate.opsForHash().put(this.HashKey, token, username);
        redisTemplate.expire(token, 45, TimeUnit.MINUTES);
    }
    @Override
    public void createSession(HttpServletRequest request, String name, String list_json) {
        HttpSession session = request.getSession(true);
        session.setAttribute((String) name, list_json);
        session.setMaxInactiveInterval(60 * 3600);
    }

    @Override
    public String getSession(HttpServletRequest request, String name) {

        HttpSession session = request.getSession(true);
        return (String) session.getAttribute(name);
    }

    @Override
    public String getSession(String jwt) {

        return redisTemplate.opsForHash().get(this.HashKey, jwt).toString();
    }
    @Override
    public void restartExpire(String token) {
        redisTemplate.expire(token , 45 , TimeUnit.MINUTES   )  ;
    }

    @Override
    public void deleteSession(String token) {
        redisTemplate.opsForHash().delete(this.HashKey , token) ;
    }


}
