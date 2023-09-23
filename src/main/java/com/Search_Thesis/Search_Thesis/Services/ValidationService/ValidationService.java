package com.Search_Thesis.Search_Thesis.Services.ValidationService;

import com.Search_Thesis.Search_Thesis.DTO.SignUpDTO;
import com.Search_Thesis.Search_Thesis.Services.MiddleWare.MiddleWare;
import com.Search_Thesis.Search_Thesis.repository.User_respository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("ValidationService")
public class ValidationService {
    private MiddleWare middleWare;
    private User_respository userRepository;

    public boolean usernameHasExisted(String username) {
        return userRepository.findUsersByAccount(username) == null;
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 5;
    }

    public boolean compareToConfirmPassWord(String password, String confirmPassword) {
        if (StringUtils.isNoneBlank(password) && StringUtils.isNoneBlank(confirmPassword)) {
            return password.equals(confirmPassword);
        }
        return false;
    }

    @Autowired
    public void setUserRepository(User_respository userRepository) {
        this.userRepository = userRepository;
    }

    public void setMiddleWare(MiddleWare middleWare) {
        this.middleWare = middleWare;
    }

    public Boolean register(SignUpDTO sign , Map<String , String> bindingError) {
        if (null == sign) {
            return true;
        }
        return middleWare.check(sign , bindingError);
    }
}

