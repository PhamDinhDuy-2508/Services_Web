package com.Search_Thesis.Search_Thesis.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String  , String > halderValidArgument(MethodArgumentNotValidException exception) {
        Map<String , String> errorHashMap = new HashMap<>() ;

        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errorHashMap.put(fieldError.getField() ,  fieldError.getDefaultMessage()) ;
        });

        return  errorHashMap ;
    }
}
