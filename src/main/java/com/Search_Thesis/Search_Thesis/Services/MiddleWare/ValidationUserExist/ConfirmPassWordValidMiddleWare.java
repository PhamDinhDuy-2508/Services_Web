package com.Search_Thesis.Search_Thesis.Services.MiddleWare.ValidationUserExist;

import com.Search_Thesis.Search_Thesis.DTO.SignUpDTO;
import com.Search_Thesis.Search_Thesis.Services.MiddleWare.MiddleWare;
import com.Search_Thesis.Search_Thesis.Services.ValidationService.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("ConfirmPassWordValidMiddleWare")
public class ConfirmPassWordValidMiddleWare extends MiddleWare {
    private final ValidationService validationService ;

    @Override
    public boolean check(SignUpDTO sign , Map<String , String> bindingError) {
        if(validationService.compareToConfirmPassWord(sign.getPassword(),sign.getRepeat_password())) {
           return checkNext(sign , bindingError) ;
        }
        bindingError.put("Error002" , "Repeat Password is invalid") ;
        return false ;
    }
    @Autowired
    public ConfirmPassWordValidMiddleWare(ValidationService validationService) {
        this.validationService = validationService;
    }
}
