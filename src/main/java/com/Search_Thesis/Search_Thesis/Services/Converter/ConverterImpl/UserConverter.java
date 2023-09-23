package com.Search_Thesis.Search_Thesis.Services.Converter.ConverterImpl;

import com.Search_Thesis.Search_Thesis.DTO.SignUpDTO;

import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.Converter.Converter;

import org.springframework.stereotype.Service;

@Service("userSignUpConverter")
public class UserConverter extends Converter<SignUpDTO, User> {


    public UserConverter() {
        super(UserConverter::convertFromEntity, UserConverter::convertFromDto);
    }

    public static SignUpDTO convertFromDto(final User user) {
        SignUpDTO sign = new SignUpDTO();
        sign.setEmail(user.getEmail());
        sign.setPassword(user.getPassword());
        sign.setUsername(user.getAccount());
        return sign;
    }

    public static User convertFromEntity(final SignUpDTO dto) {
        User user = new User();
        user.setAccount(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        return user;
    }
}
