package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.repository.Question_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@Scope("prototype")
@Service
public class CacheManager_iml_PAGINATION implements Cache_Manager<String> {
    private Object object;
    @Autowired
    Question_Repository question_repository;

    @Autowired
    CacheManager cacheManager;



    public void Set_object(Object object) {
        this.object = object;
    }

    @Override
    public String getCache(String id, String... value) {
        return null;
    }

    @Override
    public boolean Update_Cache(int page_size, String name_cache, String... cache_key) {
        try {

            String Cache_key = Arrays.stream(cache_key).toList().get(0);

            List<Object> list = Collections.singletonList(question_repository.findAll());

            PagedListHolder pagedListHolder = new PagedListHolder<>(list);

            pagedListHolder.setPageSize(page_size);

            int total_page = pagedListHolder.getPageCount();
            if (cache_key == null) {

                cacheManager.getCache(name_cache).clear();

            }

            for (int i = 1; i <= total_page; i++) {
                pagedListHolder.setPageSize(page_size);
                pagedListHolder.setPage(i);
                String key =   Cache_key +  "-" +  String.valueOf(i) ;
                System.out.println(key);

                if (Cache_key != null) {

                    cacheManager.getCache(name_cache).evict(key);

                }

//                cacheManager.getCache(name_cache).put(key, lst.stream().toList());
            }

            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return  false ;
        }
    }
}
