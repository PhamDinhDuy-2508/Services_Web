package com.Search_Thesis.Search_Thesis.Services.RegisterService;

import com.Search_Thesis.Search_Thesis.DTO.SignUpDTO;
import com.Search_Thesis.Search_Thesis.Model.Role;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.Converter.Converter;
import com.Search_Thesis.Search_Thesis.Services.MiddleWare.MiddleWare;
import com.Search_Thesis.Search_Thesis.Services.MiddleWare.ValidationUserExist.ConfirmPassWordValidMiddleWare;
import com.Search_Thesis.Search_Thesis.Services.MiddleWare.ValidationUserExist.UserExistUserNameMiddleWare;
import com.Search_Thesis.Search_Thesis.Services.MiddleWare.ValidationUserExist.UserExistedPasswordIsValidMiddleWare;
import com.Search_Thesis.Search_Thesis.Services.Utils.Constant.Constant;
import com.Search_Thesis.Search_Thesis.Services.ValidationService.ValidationService;
import com.Search_Thesis.Search_Thesis.repository.User_respository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class RegisterService {

    private final ValidationService validationService;
    private final User_respository userRespository;
    private Environment environment;

    private Converter<SignUpDTO, User> userSignUpConverter;

    @Autowired
    RegisterService(User_respository user_respository, ValidationService validationService) {
        this.validationService = validationService;
        this.userRespository = user_respository;
    }

    @PostConstruct
    private void init() {
        MiddleWare middleware = MiddleWare.link(
                new UserExistUserNameMiddleWare(validationService),
                new UserExistedPasswordIsValidMiddleWare(validationService),
                new ConfirmPassWordValidMiddleWare(validationService)
        );
        validationService.setMiddleWare(middleware);
    }
    @Transactional
    public void registerUser(SignUpDTO sign) {
        Role role = new Role();
        role.setRoleID(Integer.parseInt(Objects.requireNonNull(environment.getProperty(Constant.USER_ROLE_KEY))));
        User user = userSignUpConverter.convertFromDto(sign);
        user.setRoles(Set.of(role));
        userRespository.saveAndFlush(user);
    }

    public Map<String, String> validate(SignUpDTO signUpData) {
        Map<String, String> bindingError = new LinkedHashMap<>();
        if (signUpData != null) {
            validationService.register(signUpData, bindingError);
            return bindingError;
        }
        return null;
    }

    @Autowired
    private void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Autowired
    private void setUserSignUpConverter(@Qualifier("userSignUpConverter") Converter<SignUpDTO, User> userSignUpConverter) {
        this.userSignUpConverter = userSignUpConverter;
    }
}

