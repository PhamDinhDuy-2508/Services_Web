package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.*;
import com.Search_Thesis.Search_Thesis.Payload.QuestionCreate;
import com.Search_Thesis.Search_Thesis.Payload.Question_info_response;
import com.Search_Thesis.Search_Thesis.Payload.Reply_request;
import com.Search_Thesis.Search_Thesis.resposity.Question_Repository;
import com.Search_Thesis.Search_Thesis.resposity.Reply_respository;
import com.Search_Thesis.Search_Thesis.resposity.Tag_repository;
import com.Search_Thesis.Search_Thesis.resposity.User_respository;
import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

@Service
public class QandA_Services {

    @Autowired
    Question_Repository question_repository;
    @Autowired
    Tag_repository category_question_repository;
    @Autowired
    Question question;
    @Autowired
    JWT_Services jwt_services;
    @Autowired
    User_respository user_respository;

    @Autowired
    Cloudinary_Services cloudinaryServices;

    @Autowired
    private  Reply reply ;

    @Autowired
    Reply_respository reply_respository ;


    //    @Cacheable(value = "load_question" , key = "{#page}")
    private ExecutorService threadpool = Executors.newCachedThreadPool();
    public int get_size_list() {
        List<Question> list =   question_repository.findAll() ;
        return list.size() ;
    }

    public Page<Question> load_all_with_page(String page, String Filter) {
        int signal = Integer.parseInt(Filter);


        if (signal == 0) {
            Page<Question> page2 = question_repository.findAll(PageRequest.of(Integer.parseInt(page)-1, 10));
            return page2;
        } else if (signal == 1) {
            Page<Question> page1 = question_repository.findAll(PageRequest.of(Integer.parseInt(page), 10,
                    Sort.by(Sort.Direction.DESC, "View")));

            return page1;
        } else if (signal == 2) {
            Page<Question> page1 = question_repository.findAll(PageRequest.of(Integer.parseInt(page), 10,
                    Sort.by(Sort.Direction.DESC, "Vote")));

            return page1;
        } else {
            Page<Question> page1 = question_repository.findAll(PageRequest.of(Integer.parseInt(page), 10));

            return page1;
        }
    }

    public List<Question> load_all() {
        return question_repository.findAll();
    }

    public int get_amount() {
        List<Question> list = question_repository.findAll();
        return list.size();
    }

    public List<Question> get_List_Filter(String Filter) {
        return null;
    }

    @Async
    public void Create_Question(QuestionCreate question, String token) {
        Future<Set<Category_Question>> get_Category_task;


        List<Category_Question> list_tag = new ArrayList<>();

        try {
            Callable callable = () -> {
                return get_Tag_List(question.getCategory());
            };

            Category_Question category_question = new Category_Question();
            category_question.setCategory_id(1);


            Question question1 = new Question();
            if (!question.getEmail_send().equals("")) {
                question1.setEmail(question1.getEmail());
            }

            question1.setContent(question.getContent());
            Runnable r1 = new Runnable() {
                @Override
                public void run() {
                    try {
                        Update_to_Database_and_Cloudiary(question1) ;
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            } ;
            new Thread(r1).start();

            question1.setTitle(question.getTitle());

            jwt_services.setJwt(token);

            JSONObject jsonObject = jwt_services.getPayload();
            int id = (int) jsonObject.get("id");

            User user = user_respository.findById(id);


            LocalDate myObj = LocalDate.now();
            Date date = Date.valueOf(myObj);

            question1.setDate_Create(date);
            user.setQuestionList(Collections.singleton(question1));
            List<Category_Question> list = new ArrayList<>();
            list.add(category_question);
            question1.setCategory_questions(list);


            question1.setCreator(user);

            get_Category_task = threadpool.submit(callable);
            list_tag = (List<Category_Question>) get_Category_task.get();

            question1.setCategory_questions(list_tag);


            System.out.println("testasdasdasdasd");



//            change_byte_to_link_in_content(question1.getContent()) ;
//            Runnable thread = new Content(question1) ;
//            thread.run();


//            question_repository.save(question1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public void Update_to_Database_and_Cloudiary(Question question) throws ExecutionException, InterruptedException {


        CompletableFuture<Content> contentCompletableFuture =
        CompletableFuture.supplyAsync(()->{


            Content content1 =  new Content(question) ;
            content1.find_src();
            System.out.println(content1.getMap_src_image());
            return content1;

        }).thenApplyAsync((content)->{

            HashMap<Integer , String> map_src_image =  content.getMap_src_image() ;
            int length = 20;
            boolean useLetters = true;
            boolean useNumbers = false;
            String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);

            List<Integer> keyset = map_src_image.keySet().stream().toList(); ;

            for (Integer i : keyset ) {
                String name_image =  generatedString +"_"+i ;


                byte[] byte_array =  Base64.getDecoder().decode( map_src_image.get(i).getBytes()) ;

                Map image_url =    cloudinaryServices.upload( name_image, byte_array) ;

                String url =  (String) image_url.get("secure_url");

                content.getMap_src_image().replace(i ,  url) ;

            }
            return content;
        }).thenApplyAsync(content -> {

            content.update_content();
            question.setContent(content.getContent_result().toString());
            System.out.println(question);
            question_repository.save(question) ;
            return content ;

        }) ;
        contentCompletableFuture.get() ;

    }

    public List<Category_Question> get_Tag_List(String check) {
        String[] tag = check.split(",");
        List<Category_Question> tag_List = new ArrayList<>();
        for (int i = 0; i < tag.length; i++) {

            Category_Question category_question = category_question_repository.findByCategory_id(Integer.parseInt(tag[i]));
            tag_List.add(category_question);
        }
        return tag_List;
    }

    @Async
    public void Increment_view(String Question_Id) {

        int Question_id = Integer.parseInt(Question_Id);

        Question question1 = question_repository.findByQuestion_id(Question_id);

        int count = question1.getView() + 1;
        question1.setView(count);
        question_repository.save(question1);


    }


    public Question load_question_detail(int id) {

        Question question1 = question_repository.findByQuestion_id(id);

        return question1;

    }

    public List<Question_info_response> convert_to_info(Page<Question> page) {
        List<Question_info_response> question_info_responses = new ArrayList<>();
        Gson gson = new Gson() ;
        List<Question> list = page.getContent();

        try {
            for (Question question : list) {
                Question_info_response question_info_response = new Question_info_response();

                question_info_response.setQuestion(question);

                User user = question.getCreator();

                question_info_response.setCreator(user);


                question_info_responses.add(question_info_response);

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    return question_info_responses ;
    }
    @Async
    public void upload(MultipartFile  multipartFile , String id_quesion  ,  List<String> pos ) throws IOException {
        for (int i = 0; i < pos.size() ; i ++) {
            int length = 10;
            boolean useLetters = true;
            boolean useNumbers = false;
            String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);

            System.out.println(generatedString);


            String name_image = "/" + generatedString +"_"+pos ;
            if (cloudinaryServices.upload_image_with_Byte(  name_image , multipartFile.getBytes()) == false )  {
                continue;
            }
        }

    }
    @Async
    public void upload_reply(Reply_request reply_request) throws ParseException {


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(now.toString());


       question =  question_repository.findByQuestion_id(Integer.valueOf(reply_request.getQuestion_id())) ;


        jwt_services.setJwt(reply_request.getToken());

        JSONObject jsonObject = jwt_services.getPayload();

        int user_id = (int) jsonObject.get("id");

        User user =  user_respository.findById(user_id) ;

        reply.setUser(user);

        reply.setDate_create(date);

        reply.setContent(reply_request.getContent());
        System.out.println(reply);

        question.setCreator(user);


        question.setReply(Collections.singleton(reply));

        return   ;
    }

    public Question load_question(String id){

        Question question1 =  new Question() ;

        int ID = Integer.valueOf(id)  ;
        question1  = question_repository.findByQuestion_id(ID) ;

        return question1 ;
    }

}





