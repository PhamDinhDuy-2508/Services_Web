package com.Search_Thesis.Search_Thesis.Services;

import org.springframework.stereotype.Service;

@Service
public interface Cache_Manager <X>{
    public X getCache( X id,  X...value) ;

    public boolean Update_Cache(int page_size , X t  , X...Value) ;

}

