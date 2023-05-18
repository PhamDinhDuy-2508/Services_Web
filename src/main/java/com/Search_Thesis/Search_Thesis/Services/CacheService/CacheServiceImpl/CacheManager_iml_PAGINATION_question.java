package com.Search_Thesis.Search_Thesis.Services.CacheService.CacheServiceImpl;

import com.Search_Thesis.Search_Thesis.Model.Category_Question;
import com.Search_Thesis.Search_Thesis.Model.Question;
import com.Search_Thesis.Search_Thesis.DTO.Category_question_id_name;
import com.Search_Thesis.Search_Thesis.DTO.QuestionDetailResponse;
import com.Search_Thesis.Search_Thesis.Services.CacheService.CacheManager;
import com.Search_Thesis.Search_Thesis.repository.Question_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("CacheManager_iml_PAGINATION_question")

public class CacheManager_iml_PAGINATION_question implements CacheManager<String> {
    @Autowired
    Question_Repository question_repository ;

    @Autowired
    org.springframework.cache.CacheManager cacheManager ;

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
            List<QuestionDetailResponse> question_detail_respons =  new ArrayList<>() ;

            for(Question question1 : list) {

                List<Category_Question> category_questions =  question1.getCategory_questions() ;
                List<Category_question_id_name> category_question_id_names =  new ArrayList<>() ;
                QuestionDetailResponse question_detailResponse =  new QuestionDetailResponse() ;
                question_detailResponse.setQuestion(question1);
                question_detailResponse.setReply_size(question1.getReply().size());

                for (Category_Question category_question : category_questions ) {

                    category_question_id_names.add(new Category_question_id_name(category_question.getCategory_id() , category_question.getCategory_name())) ;

                }
                question_detailResponse.setCategory_questionList(category_question_id_names);

                question_detail_respons.add(question_detailResponse) ;
            }

            ArrayList<QuestionDetailResponse> LIST = new ArrayList<QuestionDetailResponse>(question_detail_respons.subList(0, question_detail_respons.size()));

            cacheManager.getCache("question_page").put(String.valueOf(i+1) , LIST);
        }


        if(compoment_in_last_page != 0 ) {

            pagedListHolder.setPageSize(pagedListHolder.getPageSize());
            pagedListHolder.setPage(total_page+1);
            cacheManager.getCache("question_page").evict(total_page+1); ;
            List<Question> list  = pagedListHolder.getPageList() ;
            List<QuestionDetailResponse> question_detail_respons =  new ArrayList<>() ;


            for(Question question1 : list) {
                List<Category_Question> category_questions =  question1.getCategory_questions() ;
                List<Category_question_id_name> category_question_id_names =  new ArrayList<>() ;

                QuestionDetailResponse question_detailResponse =  new QuestionDetailResponse() ;
                question_detailResponse.setQuestion(question1);
                question_detailResponse.setReply_size(question1.getReply().size());
                for (Category_Question category_question : category_questions ) {

                    category_question_id_names.add(new Category_question_id_name(category_question.getCategory_id() , category_question.getCategory_name())) ;

                }
                question_detailResponse.setCategory_questionList(category_question_id_names);

                question_detail_respons.add(question_detailResponse) ;

            }

            ArrayList<QuestionDetailResponse> LIST = new ArrayList<QuestionDetailResponse>(question_detail_respons.subList(0, question_detail_respons.size()));

            cacheManager.getCache("question_page").put(total_page+1 , LIST); ;
        }
        return  false ;

    }
    public boolean update_into_View_CaChe(Question question) {
        int total = question_repository.findAll().size() ;
        List<QuestionDetailResponse> list_pre =  new ArrayList<>() ;
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
                list_pre = (List<QuestionDetailResponse>) cacheManager.getCache("question_page").get(Last_page+"-View").get();

            }
            List<QuestionDetailResponse> list = (List<QuestionDetailResponse>) cacheManager.getCache("question_page").get(page+"-View").get();

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
    public int Update_Pos(String id , List<QuestionDetailResponse> question_detailResponseList_current, List<QuestionDetailResponse> question_detailResponseList_Pre) {
        int pos = 0 ;
        QuestionDetailResponse question =  new QuestionDetailResponse() ;
        for(QuestionDetailResponse question_detailResponse : question_detailResponseList_current) {
            if(question_detailResponse.getQuestion().getQuestion_id() ==  Integer.valueOf(id)) {
                question = question_detailResponse;
                break;
            }
            pos ++ ;
        }
        if(pos != 0 ) {
            for (int i = pos; i < question_detailResponseList_current.size(); i++) {
                if (question_detailResponseList_current.get(i).getQuestion().getView() < question_detailResponseList_current.get(pos).getQuestion().getView()) {
                    question_detailResponseList_current.remove(pos);
                    question_detailResponseList_current.add(i, question);
                }
            }
            return  0 ;
        }
        else {
            for (int i = pos; i < question_detailResponseList_Pre.size(); i++) {
                if (question_detailResponseList_Pre.get(i).getQuestion().getView() < question.getQuestion().getView()) {
                    question_detailResponseList_current.remove(pos);
                    question_detailResponseList_current.add(question_detailResponseList_Pre.get(9));
                    question_detailResponseList_Pre.remove(9) ;
                    question_detailResponseList_Pre.add(i, question);
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
