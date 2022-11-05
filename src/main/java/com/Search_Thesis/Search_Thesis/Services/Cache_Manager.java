package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.Reply;
import com.Search_Thesis.Search_Thesis.Payload.Reply_response;
import com.Search_Thesis.Search_Thesis.resposity.Question_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
@Service
public interface Cache_Manager <T ,X>{
    public T getCache( String id,  Integer...value) ;

    public X Update_Cache(X t , String id , Integer...Value) ;
}
@Service
class Cache_Reply implements  Cache_Manager<Reply_response, Reply> {
    @Autowired
    Question_Repository question_repository ;


    @Override
    @Cacheable(value = "reply_page" , key = "#value")
   public Reply_response  getCache(String id, Integer... value) {


       return  null ;
    }

    @Override
    @CachePut(value = "reply_page" , key = "#value")
    public Reply  Update_Cache( Reply replyList, String id, Integer... Value) {
          return replyList ;
    }




}
