package com.Search_Thesis.Search_Thesis.Services.MiddleWare.ValidationUserExist;

import com.Search_Thesis.Search_Thesis.DTO.SignUpDTO;
import com.Search_Thesis.Search_Thesis.Services.MiddleWare.MiddleWare;
import com.Search_Thesis.Search_Thesis.Services.ValidationService.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("UserExistMiddleWare")
public class UserExistUserNameMiddleWare extends MiddleWare {
    private ValidationService validationService ;

    @Override
    public boolean check(SignUpDTO sign , Map<String , String> bindingError) {
        if(!validationService.usernameHasExisted(sign.getUsername())) {
            bindingError.put("Error001" ,  "Username Has been Existed") ;
            return false ;
        }
        return  checkNext(sign,bindingError) ;

    }
    @Autowired
    public UserExistUserNameMiddleWare(ValidationService validationService) {
        this.validationService = validationService;
    }
}
