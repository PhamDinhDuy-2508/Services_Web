package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.*;
import com.Search_Thesis.Search_Thesis.Payload.*;
import com.Search_Thesis.Search_Thesis.resposity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    Cache_Services cache_services ;

    @Autowired
    private  Reply reply ;

    @Autowired
    Reply_respository reply_respository ;
    @Autowired
    Comment_ques_repository comment_ques_repository;

    private Reply_response reply_response ;

    @Autowired
    Cache_Services<List<Question>  , Question, Integer> listCache_services_quesion ;

    @Autowired
    Cache_Reply cache_manager ;


    private ExecutorService threadpool = Executors.newCachedThreadPool();
    public int get_size_list() {

        List<Question> list =   question_repository.findAll() ;

        return list.size() ;
    }
//    @Cacheable(value = "quesgtion_page"  , key = "#page")
    public List<Question> load_all_with_page(String page, String Filter) {

        if(listCache_services_quesion.get_Cache(Integer.valueOf(page)) != null) {
            List<Question> list = listCache_services_quesion.get_Cache(Integer.valueOf(page));
            return  list ;
        }
        else {
            List<Question> list = load_all();

            PagedListHolder pagedListHolder = new PagedListHolder<>(list);

            pagedListHolder.setPage(Integer.parseInt(page) - 1);
            pagedListHolder.setPageSize(10);

            try {

                listCache_services_quesion.Create_Cache(pagedListHolder.getPageList(), Integer.valueOf(page));
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return pagedListHolder.getPageList();
        }


    }
    @Cacheable(value = "listquestion" )

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
                question1.setEmail(question.getEmail_send());
            }


            question1.setContent(question.getContent());
            Runnable r1 = new Runnable() {
                @Override
                public void run() {
                    try {
                        Update_to_Database_and_Cloudiary(question1) ;
                    } catch (ExecutionException e) {
                        System.out.println(e.getMessage());
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
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
            System.out.println(content);
            return content;
        }).thenApplyAsync(content -> {
            content.update_content();
            question.setContent(content.getContent_result().toString());
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

    @Async
    public void upload(MultipartFile  multipartFile , String id_quesion  ,  List<String> pos ) throws IOException {
        for (int i = 0; i < pos.size() ; i ++) {
            int length = 10;
            boolean useLetters = true;
            boolean useNumbers = false;

            String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);

            String name_image = "/" + generatedString +"_"+pos ;
            if (cloudinaryServices.upload_image_with_Byte(  name_image , multipartFile.getBytes()) == false )  {
                continue;
            }
        }

    }

    @CacheEvict(value = "reply" , key = "#id")
    public Reply_response update_Cache(String id , Reply reply) {
        Reply_response reply_response = new Reply_response() ;

        return  reply_response ;
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
        reply.setQuestion(question);

        reply.setContent(reply_request.getContent());

        reply_respository.save(reply) ;

        question.setCreator(user);

        question.setReply(Collections.singleton(reply));
//        response_question_pagination_reload()


        return   ;
    }
    @Cacheable(value = "comment_rep" ,  key = "#id_reply")
    public  List<Comment_Reply_Question>  load_Comment(String id_reply) {

       Reply rep =  reply_respository.findByReply_id(Integer.valueOf(id_reply)) ;
       List<Comment_response> comment_response_LIST =  new ArrayList<>() ;
       try {
           List<Comment_Reply_Question>  list =  rep.getComment_reply_questionList().stream().toList() ;
           return list ;

       }
       catch (Exception e) {
           return null ;
       }
    }
    @CacheEvict(value = "reply_page" )
    public boolean response_question_pagination_reload(String id , int page_num) throws JsonProcessingException {

        Question question1 =  question_repository.findByQuestion_id(Integer.parseInt(id)) ;


        List<Reply> replyList =   (List<Reply>) question1.getReply();


        Collections.reverse(replyList);

        return  true  ;
    }


    @Cacheable(value = "reply_page" , key = "{#page_num,  #id}")

    public Reply_response response_question_pagination(String id , int page_num) throws JsonProcessingException {

        List<Reply> reply_responses = load_reply(id) ;
        PagedListHolder page = new PagedListHolder(reply_responses);
        page.setPage(page_num-1);
        page.setPageSize(8);
        ArrayList<Reply> list = new ArrayList<Reply>(page.getPageList().subList(0, page.getPageList().size()));

        reply_response = new Reply_response() ;

        reply_response.setReply(list);
        reply_response.setSize(page.getPageCount());

        return reply_response  ;

    }
    @Cacheable(value = "question_info" , key = "#id")
    public Question load_question(String id){

        Question question1 =  new Question() ;
        HashMap response = new HashMap<>() ;

        int ID = Integer.valueOf(id)  ;
        question  = question_repository.findByQuestion_id(ID) ;
        response.put("question" , question) ;

        Question_detail_response question_detail_response = new Question_detail_response() ;
        question_detail_response.setQuestion(question1);


        return question ;
    }
    @Cacheable(value = "reply_id" ,  key = "#id")
    public List<Reply>  load_reply(String id) {

        Question question1 =  question_repository.findByQuestion_id(Integer.parseInt(id)) ;
        List<Reply> replyList =   (List<Reply>) question1.getReply();
        Collections.reverse(replyList);
        return  replyList ;
    }

    @Async
    public void insert_Comment(Comment_Request comment_request) throws ParseException {

        jwt_services.setJwt(comment_request.getToken());
        JSONObject jsonObject = jwt_services.getPayload();
        int user_id = (int) jsonObject.get("id");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(now.toString());


        User user =  user_respository.findById(user_id) ;
        Reply reply1 = reply_respository.findByReply_id(Integer.valueOf(comment_request.getReply_id())) ;

        Comment_Reply_Question comment_reply_question = new Comment_Reply_Question() ;
        reply1.setComment_reply_questionList(Collections.singleton(comment_reply_question));

        comment_reply_question.setReply(reply1);
        comment_reply_question.setUser_comment(user);

        comment_reply_question.setContent(comment_request.getContent());
        comment_reply_question.setDate(date);

        comment_ques_repository.save(comment_reply_question) ;

    }



}



