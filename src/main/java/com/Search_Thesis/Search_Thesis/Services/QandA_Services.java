package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.Category_Question;
import com.Search_Thesis.Search_Thesis.Model.Question;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Payload.QuestionCreate;
import com.Search_Thesis.Search_Thesis.resposity.Question_Repository;
import com.Search_Thesis.Search_Thesis.resposity.Tag_repository;
import com.Search_Thesis.Search_Thesis.resposity.User_respository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class QandA_Services {

    @Autowired
    Question_Repository question_repository ;
    @Autowired
    Tag_repository category_question_repository ;
    @Autowired
    Question question ;
    @Autowired
    JWT_Services jwt_services ;
    @Autowired
    User_respository user_respository ;


    //    @Cacheable(value = "load_question" , key = "{#page}")
    private ExecutorService threadpool = Executors.newCachedThreadPool();

    public Page<Question> load_all(String page , String Filter) {
        int signal = Integer.parseInt(Filter);

        if (signal == 0) {
           Page<Question> page2 = question_repository.findAll(PageRequest.of(0, 10));
           return  page2 ;
        } else if (signal == 1) {
            Page<Question>   page1 = question_repository.findAll(PageRequest.of(Integer.parseInt(page), 10,
                    Sort.by(Sort.Direction.DESC, "View")));

            return page1 ;
        }

        else if(signal == 2) {
            Page<Question>   page1 = question_repository.findAll(PageRequest.of(Integer.parseInt(page), 10,
                    Sort.by(Sort.Direction.DESC, "Vote")));

            return page1 ;
        }

        else {
            Page<Question>   page1 = question_repository.findAll(PageRequest.of(Integer.parseInt(page), 10));
            return page1 ;
        }
    }
    public int get_amount() {
        List<Question> list =  question_repository.findAll() ;
        return list.size() ;
    }
    public List<Question> get_List_Filter(String Filter) {
        return null ;
    }

    @Async
    public void Create_Question(QuestionCreate question , String token)  {
        Future<Set<Category_Question>> get_Category_task ;


        List<Category_Question> list_tag =  new ArrayList<>() ;

        try {
            Callable callable = ()->{
                return   get_Tag_List(question.getCategory()) ;
            } ;

            Category_Question category_question =  new Category_Question() ;
            category_question.setCategory_id(1);


            Question question1 = new Question();
            if(!question.getEmail_send().equals("")) {
                question1.setEmail(question1.getEmail());
            }

            question1.setContent(question.getContent());
            question1.setTitle(question.getTitle());

            jwt_services.setJwt(token);

            JSONObject jsonObject = jwt_services.getPayload();
            int id = (int) jsonObject.get("id");

            User user = user_respository.findById(id);


            LocalDate myObj = LocalDate.now();
            Date date = Date.valueOf(myObj);

            question1.setDate_Create(date);
            user.setQuestionList(Collections.singleton(question1));
            List<Category_Question> list =  new ArrayList<>() ;
            list.add(category_question);
            question1.setCategory_questions(list);


            question1.setCreator(user);

            get_Category_task = threadpool.submit(callable) ;
            list_tag = (List<Category_Question>) get_Category_task.get();

            question1.setCategory_questions(list_tag); ;
            question_repository.save(question1);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return  ;
        }
    }

    public List<Category_Question> get_Tag_List(String check) {
        String []  tag = check.split(",") ;
        List<Category_Question> tag_List =  new ArrayList<>()  ;
        for (int i =0; i<  tag.length ; i++) {

           Category_Question category_question =  category_question_repository.findByCategory_id(Integer.parseInt(tag[i])) ;
            tag_List.add(category_question) ;
        }
        return tag_List ;
    }
    @Async
    public void Increment_view(String Question_Id) {

        int Question_id = Integer.parseInt(Question_Id) ;

        Question question1 =  question_repository.findByQuestion_id(Question_id) ;

        int count  = question.getView() +1 ;
        question1.setView(count);

        question_repository.save(question1) ;

    }


    public Question load_question_detail(int id) {
        Question question1 = question_repository.findByQuestion_id(id) ;

        return question1 ;

    }
    @Async
    public void Reply(){

    }



}
