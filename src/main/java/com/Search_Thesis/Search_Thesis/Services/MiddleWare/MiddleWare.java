package com.Search_Thesis.Search_Thesis.Services.MiddleWare;

import com.Search_Thesis.Search_Thesis.DTO.SignUpDTO;

import java.util.Map;

public abstract class MiddleWare {
    private MiddleWare next;

    public static MiddleWare link(MiddleWare first, MiddleWare... chain) {
        MiddleWare head = first;
        for (MiddleWare nextChain : chain) {
            head.next = nextChain;
            head = nextChain;
        }
        return first ;
    }
    public abstract boolean check(SignUpDTO sign, Map<String , String> bindingError) ;
    protected  boolean checkNext(SignUpDTO sign , Map<String , String> bindingError) {
        if(next == null) {
            return true ;
        }
        return next.check(sign ,  bindingError) ;
    }
}
