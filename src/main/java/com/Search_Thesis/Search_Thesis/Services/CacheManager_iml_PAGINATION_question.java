package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.Question;
import com.Search_Thesis.Search_Thesis.Payload.Question_detail_response;
import com.Search_Thesis.Search_Thesis.resposity.Question_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service

public class CacheManager_iml_PAGINATION_question implements Cache_Manager<String> {
    @Autowired
    Question_Repository question_repository ;

    @Autowired
    CacheManager cacheManager ;

    @Override
    public String getCache(String id, String... value) {
        return null;
    }
    public boolean add_Ques_into_Cache() {
        List<Question> questionList = question_repository.findAll() ;
        PagedListHolder<Question> pagedListHolder =  new PagedListHolder<>(questionList) ;



        int total_page =    questionList.size()/10  ;

        int compoment_in_last_page = questionList.size() - total_page * 10;

        for(int i = 0; i < total_page ; i++) {
            pagedListHolder.setPageSize(10);
            pagedListHolder.setPage(i+1);
            cacheManager.getCache("question_page").evict(String.valueOf(i+1)); ;
            List<Question> list  = pagedListHolder.getPageList() ;
            List<Question_detail_response> question_detail_responses =  new ArrayList<>() ;


            for(Question question1 : list) {
                Question_detail_response question_detail_response =  new Question_detail_response() ;
                question_detail_response.setQuestion(question1);
                question_detail_response.setReply_size(question1.getReply().size());
                question_detail_responses.add(question_detail_response) ;
            }

            ArrayList<Question_detail_response> LIST = new ArrayList<Question_detail_response>(question_detail_responses.subList(0, question_detail_responses.size()));


            cacheManager.getCache("question_page").put(String.valueOf(i+1) , LIST); ;

            System.out.println(cacheManager.getCache("question_page").get(String.valueOf(i+1)).get());


        }
        if(compoment_in_last_page != 0 ) {

            pagedListHolder.setPageSize(pagedListHolder.getPageSize());
            pagedListHolder.setPage(total_page+1);
            cacheManager.getCache("question_page").evict(total_page+1); ;
            List<Question> list  = pagedListHolder.getPageList() ;
            List<Question_detail_response> question_detail_responses =  new ArrayList<>() ;


            for(Question question1 : list) {
                Question_detail_response question_detail_response =  new Question_detail_response() ;
                question_detail_response.setQuestion(question1);
                question_detail_response.setReply_size(question1.getReply().size());
                question_detail_responses.add(question_detail_response) ;
            }

            ArrayList<Question_detail_response> LIST = new ArrayList<Question_detail_response>(question_detail_responses.subList(0, question_detail_responses.size()));

            cacheManager.getCache("question_page").put(total_page+1 , LIST); ;
        }
        return  false ;

    }



    @Override
    public boolean Update_Cache(int page_size, String t, String... Value) {
        return false;
    }
}
