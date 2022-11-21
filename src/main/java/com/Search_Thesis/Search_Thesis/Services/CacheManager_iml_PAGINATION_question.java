package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.Category_Question;
import com.Search_Thesis.Search_Thesis.Model.Question;
import com.Search_Thesis.Search_Thesis.Payload.Category_question_id_name;
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
            System.out.println(pagedListHolder.getPageList().size());

            pagedListHolder.setPageSize(10);
            pagedListHolder.setPage(i);
            cacheManager.getCache("question_page").evict(String.valueOf(i)); ;
            List<Question> list  = pagedListHolder.getPageList() ;
            List<Question_detail_response> question_detail_responses =  new ArrayList<>() ;

            for(Question question1 : list) {

                List<Category_Question> category_questions =  question1.getCategory_questions() ;
                List<Category_question_id_name> category_question_id_names =  new ArrayList<>() ;
                Question_detail_response question_detail_response =  new Question_detail_response() ;
                question_detail_response.setQuestion(question1);
                question_detail_response.setReply_size(question1.getReply().size());

                for (Category_Question category_question : category_questions ) {

                    category_question_id_names.add(new Category_question_id_name(category_question.getCategory_id() , category_question.getCategory_name())) ;

                }
                question_detail_response.setCategory_questionList(category_question_id_names);

                question_detail_responses.add(question_detail_response) ;
            }

            ArrayList<Question_detail_response> LIST = new ArrayList<Question_detail_response>(question_detail_responses.subList(0, question_detail_responses.size()));

            cacheManager.getCache("question_page").put(String.valueOf(i+1) , LIST);
        }


        if(compoment_in_last_page != 0 ) {

            pagedListHolder.setPageSize(pagedListHolder.getPageSize());
            pagedListHolder.setPage(total_page+1);
            cacheManager.getCache("question_page").evict(total_page+1); ;
            List<Question> list  = pagedListHolder.getPageList() ;
            List<Question_detail_response> question_detail_responses =  new ArrayList<>() ;


            for(Question question1 : list) {
                List<Category_Question> category_questions =  question1.getCategory_questions() ;
                List<Category_question_id_name> category_question_id_names =  new ArrayList<>() ;

                Question_detail_response question_detail_response =  new Question_detail_response() ;
                question_detail_response.setQuestion(question1);
                question_detail_response.setReply_size(question1.getReply().size());
                for (Category_Question category_question : category_questions ) {

                    category_question_id_names.add(new Category_question_id_name(category_question.getCategory_id() , category_question.getCategory_name())) ;

                }
                question_detail_response.setCategory_questionList(category_question_id_names);

                question_detail_responses.add(question_detail_response) ;

            }

            ArrayList<Question_detail_response> LIST = new ArrayList<Question_detail_response>(question_detail_responses.subList(0, question_detail_responses.size()));

            cacheManager.getCache("question_page").put(total_page+1 , LIST); ;
        }
        return  false ;

    }
    public boolean update_into_View_CaChe(Question question) {
        int total = question_repository.findAll().size() ;
        List<Question_detail_response> list_pre =  new ArrayList<>() ;
        int total_page =  total/10 ;
        if(total > total_page*10) {
            total_page += 1 ;
        }
        for(int i = 0; i < total_page ; i++) {
            String page =  String.valueOf(i+1) ;
            String Last_page = String.valueOf(i);
            if(Last_page.equals("0")) {
                list_pre = null ;
            }
            else {
                list_pre = (List<Question_detail_response>) cacheManager.getCache("question_page").get(Last_page+"-View").get();

            }
            List<Question_detail_response> list = (List<Question_detail_response>) cacheManager.getCache("question_page").get(page+"-View").get();

            if(question.getView() < list.get(0).getQuestion().getView() && question.getView() > list.get(9).getQuestion().getView()) {

              if(  Update_Pos(String.valueOf(question.getQuestion_id()) ,  list , list_pre) == 0 ) {
//                cacheManager.getCache("question_page").get(page+"-View");
                  cacheManager.getCache("question_page").evict(page+"-View");
                  cacheManager.getCache("question_page").put(page+"-View"  ,list);
              }
              else {
                  cacheManager.getCache("question_page").evict(Last_page+"-View");
                  cacheManager.getCache("question_page").put(Last_page+"-View"  ,list_pre);
                  cacheManager.getCache("question_page").evict(page+"-View");
                  cacheManager.getCache("question_page").put(page+"-View"  ,list);
              }

            }


        }
        return  false ;
    }
    public int Update_Pos(String id ,  List<Question_detail_response> question_detail_responseList_current , List<Question_detail_response> question_detail_responseList_Pre) {
        int pos = 0 ;
        Question_detail_response question =  new Question_detail_response() ;
        for(Question_detail_response question_detail_response : question_detail_responseList_current) {
            if(question_detail_response.getQuestion().getQuestion_id() ==  Integer.valueOf(id)) {
                question =  question_detail_response ;
                break;
            }
            pos ++ ;
        }
        if(pos != 0 ) {
            for (int i = pos; i < question_detail_responseList_current.size(); i++) {
                if (question_detail_responseList_current.get(i).getQuestion().getView() < question_detail_responseList_current.get(pos).getQuestion().getView()) {
                    question_detail_responseList_current.remove(pos);
                    question_detail_responseList_current.add(i, question);
                }
            }
            return  0 ;
        }
        else {
            for (int i = pos; i < question_detail_responseList_Pre.size(); i++) {
                if (question_detail_responseList_Pre.get(i).getQuestion().getView() < question.getQuestion().getView()) {
                    question_detail_responseList_current.remove(pos);
                    question_detail_responseList_current.add(question_detail_responseList_Pre.get(9));
                    question_detail_responseList_Pre.remove(9) ;
                    question_detail_responseList_Pre.add(i, question);
                }
            }
            return  1 ;
        }


    }


    @Override
    public boolean Update_Cache(int page_size, String t, String... Value) {
        return false;
    }
}
