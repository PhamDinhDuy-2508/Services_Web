package com.Search_Thesis.Search_Thesis.Services.QandAServices;

import com.Search_Thesis.Search_Thesis.DTO.*;
import com.Search_Thesis.Search_Thesis.Model.*;
import com.Search_Thesis.Search_Thesis.Services.CacheService.CacheServiceImpl.CacheManager_iml_PAGINATION;
import com.Search_Thesis.Search_Thesis.Services.CacheService.CacheServiceImpl.CacheManager_iml_PAGINATION_comment;
import com.Search_Thesis.Search_Thesis.Services.CacheService.CacheServiceImpl.CacheManager_iml_PAGINATION_question;
import com.Search_Thesis.Search_Thesis.Services.Cloudinary.CloudinaryService;
import com.Search_Thesis.Search_Thesis.Services.JwtService.JwtService;
import com.Search_Thesis.Search_Thesis.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.cache.CacheManager;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Service("QandAServices")
public class QandAServices {
    @Autowired
    CacheManager cacheManager;
    @Autowired
    CacheManager_iml_PAGINATION_comment cacheManager_iml_pagination_comment;
    @Autowired
    Question_Repository question_repository;
    @Autowired
    Tag_repository category_question_repository;
    @Autowired
    Question question;
    @Autowired
    @Qualifier("JwtServices")
    JwtService jwt_services;
    @Autowired
    User_respository user_respository;

    @Autowired
    @Qualifier("CloudinaryService")
    CloudinaryService cloudinaryServices;
    @Autowired
    private Reply reply;

    @Autowired
    Reply_respository reply_respository;
    @Autowired
    Comment_ques_repository comment_ques_repository;


    @Autowired
    CacheManager_iml_PAGINATION cache_manager_question;

    @Autowired
    CacheManager_iml_PAGINATION_question cacheManager_iml_pagination_question;

    private ExecutorService threadpool = Executors.newCachedThreadPool();

    public int get_size_list() {

        List<Question> list = question_repository.findAll();

        return list.size();
    }

    @Cacheable(value = "question_page", key = "#page")
    public List<QuestionDetailResponse> load_all_with_page(String page, String Filter) {

        List<Question> list = load_all();

        PagedListHolder pagedListHolder = new PagedListHolder<>(list);

        pagedListHolder.setPage(Integer.parseInt(page) - 1);
        pagedListHolder.setPageSize(10);
        List<Question> list1 = pagedListHolder.getPageList();
        List<QuestionDetailResponse> question_detail_respons = new ArrayList<>();

        for (Question question1 : list1) {
            QuestionDetailResponse question_detailResponse = new QuestionDetailResponse();
            question_detailResponse.setQuestion(question1);
            question_detailResponse.setReply_size(question1.getReply().size());
            question_detail_respons.add(question_detailResponse);
        }


        ArrayList<QuestionDetailResponse> LIST = new ArrayList<QuestionDetailResponse>(question_detail_respons.subList(0, question_detail_respons.size()));
        return LIST;
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
                question1.setEmail(question.getEmail_send());
            }


            question1.setContent(question.getContent());
            Runnable r1 = new Runnable() {
                @Override
                public void run() {
                    try {
                        Update_to_Database_and_Cloudiary(question1);
//                        question_repository.save(question1) ;
                    } catch (ExecutionException e) {
                        System.out.println(e.getMessage());
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }

                }
            };
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

            ArrayList<Category_Question> LIST = new ArrayList<Category_Question>(list_tag.subList(0, list_tag.size()));
            question1.setCategory_questions(LIST);


//            update_question_cache();

            question_repository.save(question1);
            System.out.println(question_repository.findByQuestion_id(id).getCategory_questions());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public void Update_to_Database_and_Cloudiary(Question question) throws ExecutionException, InterruptedException {


        CompletableFuture<Content> contentCompletableFuture =
                CompletableFuture.supplyAsync(() -> {

                    Content content1 = new Content(question);
                    content1.find_src();

                    return content1;

                }).thenApplyAsync((content) -> {

                    HashMap<Integer, String> map_src_image = content.getMap_src_image();
                    System.out.println(map_src_image.size());
                    if (map_src_image.size() != 0) {

                        int length = 20;
                        boolean useLetters = true;
                        boolean useNumbers = false;
                        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);

                        List<Integer> keyset = map_src_image.keySet().stream().toList();
                        ;

                        for (Integer i : keyset) {
                            String name_image = generatedString + "_" + i;


                            byte[] byte_array = Base64.getDecoder().decode(map_src_image.get(i).getBytes());

                            Map image_url = cloudinaryServices.upload(name_image, byte_array);

                            String url = (String) image_url.get("secure_url");

                            content.getMap_src_image().replace(i, url);

                        }
                    } else {
                        question_repository.save(question);
                    }
                    return content;
                }).thenApplyAsync(content -> {
                    content.update_content();


                    try {

                        question.setContent(content.getContent_result().toString());
//                question_repository.save(question) ;

                    } catch (Exception e) {
                        LocalDateTime localDateTime = LocalDateTime.now();
//                question.setContent(content.getContent());

                        java.util.Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                        question.setDate_Create(date);

//                question_repository.save(question) ;

                    }
                    return content;
                });
        contentCompletableFuture.get();

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
        cacheManager_iml_pagination_question.update_into_View_CaChe(question1);

        question1.setView(count);

        question_repository.save(question1);
    }

    public Question load_question_detail(int id) {

        Question question1 = question_repository.findByQuestion_id(id);
        return question1;
    }


    @Async
    public void upload(MultipartFile multipartFile, String id_quesion, List<String> pos) throws IOException {
        for (int i = 0; i < pos.size(); i++) {
            int length = 10;
            boolean useLetters = true;
            boolean useNumbers = false;

            String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);

            String name_image = "/" + generatedString + "_" + pos;
            if (cloudinaryServices.upload_image_with_Byte(name_image, multipartFile.getBytes()) == false) {
                continue;
            }
        }
    }

    @CacheEvict(value = "reply", key = "#id")
    public ReplyResponse update_Cache(String id, Reply reply) {
        ReplyResponse reply_response = new ReplyResponse();

        return reply_response;
    }

    @Async


    public void upload_reply(ReplyRequest reply_request) throws ParseException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        LocalDateTime now = LocalDateTime.now();

        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(now.toString());

        question = question_repository.findByQuestion_id(Integer.valueOf(reply_request.getQuestion_id()));

        jwt_services.setJwt(reply_request.getToken());

        JSONObject jsonObject = jwt_services.getPayload();

        int user_id = (int) jsonObject.get("id");

        User user = user_respository.findById(user_id);

        reply.setUser(user);

        reply.setDate_create(date);
        reply.setQuestion(question);

        reply.setContent(reply_request.getContent());

        question.setCreator(user);

        question.setReply(Collections.singleton(reply));
        reply_respository.save(reply);

        upload_reply_Cache(reply_request.getQuestion_id());


        return;
    }

    @Cacheable(value = "comment_rep", key = "#page.concat('-').concat( #id_reply)")
    public List<Comment_Reply_Question> load_Comment(String id_reply, String page) {

        Reply rep = reply_respository.findByReply_id(Integer.valueOf(id_reply));
        List<CommentResponse> comment_response_LIST = new ArrayList<>();
        try {
            List<Comment_Reply_Question> list = rep.getComment_reply_questionList().stream().toList();

            return list;

        } catch (Exception e) {
            return null;
        }
    }

    @Cacheable(value = "reply_page", key = "#id.concat('-').concat( #page_num)")


    public List<Reply> response_question_pagination(String id, String page_num) throws JsonProcessingException {
        List<Reply> reply_responses = new ArrayList<>();


        reply_responses = load_reply(id);


        PagedListHolder page = new PagedListHolder(reply_responses);
        page.setPage(Integer.valueOf(page_num) - 1);
        page.setPageSize(8);
        ArrayList<Reply> list = new ArrayList<Reply>(page.getPageList().subList(0, page.getPageList().size()));

        for (Reply reply1 : list) {
            Set<Comment_Reply_Question> replySet = reply1.getComment_reply_questionList().stream().collect(Collectors.toSet());
            reply1.setComment_reply_questionList(replySet);
        }
        return list.stream().toList();

    }

    @Cacheable(value = "question_info", key = "#id")
    public Question load_question(String id) {

        Question question1 = new Question();
        HashMap response = new HashMap<>();

        int ID = Integer.valueOf(id);
        question = question_repository.findByQuestion_id(ID);
        response.put("question", question);

        QuestionDetailResponse question_detailResponse = new QuestionDetailResponse();
        question_detailResponse.setQuestion(question1);


        return question;
    }

    //    @Cacheable(value = "all_rep_question" ,  key = "{#id}")
    public List<Reply> load_reply(String id) {

        Question question1 = question_repository.findByQuestion_id(Integer.parseInt(id));
        List<Reply> replyList = (List<Reply>) question1.getReply();
        Collections.reverse(replyList);
        return replyList;
    }

    @Async
    public void insert_Comment(CommentRequest comment_request) throws ParseException {

        jwt_services.setJwt(comment_request.getToken());

        JSONObject jsonObject = jwt_services.getPayload();
        int user_id = (int) jsonObject.get("id");


        LocalDateTime now = LocalDateTime.now();
        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(now.toString());


        User user = user_respository.findById(user_id);
        Reply reply1 = reply_respository.findByReply_id(Integer.valueOf(comment_request.getReply_id()));

        Comment_Reply_Question comment_reply_question = new Comment_Reply_Question();
        reply1.setComment_reply_questionList(Collections.singleton(comment_reply_question));

        comment_reply_question.setReply(reply1);
        comment_reply_question.setUser_comment(user);

        comment_reply_question.setContent(comment_request.getContent());
        comment_reply_question.setDate(date);


        comment_ques_repository.save(comment_reply_question);
//
//
//        update_comment_cache(comment_request.getReply_id() , comment_request.getId());

        update_comment_cache(comment_request.getId(), comment_request.getReply_id(), comment_reply_question);


    }

    public void update_question_cache() {

        cache_manager_question.Set_object(question);

        cache_manager_question.Update_Cache(10, "question_page");

    }

    public void upload_reply_Cache(String id_question) {


//        cacheManager.getCache("question_page").clear();

        cache_manager_question.Set_object(reply);

        cache_manager_question.Update_Cache(8, "reply_page", id_question);
    }

    @Async
    public void update_comment_cache(String id_question, String id_reply, Comment_Reply_Question comment_reply_question) {

        List<Reply> rep = new ArrayList<>();
        System.out.println(id_reply + "," + id_question);

        Runnable task_update_Cache = () -> {
            cacheManager_iml_pagination_comment.Update_cache_Reply(id_question, id_reply, comment_reply_question, rep);

        };

        new Thread(task_update_Cache).start();
//        cache_manager_question.Set_object(reply);
//
//        cache_manager_question.Update_Cache(8 , "comment_rep" ,  id_reply ) ;
    }

    public List<Question> test_Question_Cache() {
        cacheManager_iml_pagination_question.add_Ques_into_Cache();
        return (List<Question>) cacheManager.getCache("question_page").get(1).get();
    }

    public List<Category_question_id_name> load_Category() {

        ArrayList<Category_question_id_name> LIST = new ArrayList<>();

        List<Category_Question> category_questions = category_question_repository.findAll();

        for (Category_Question category_question : category_questions) {

            Category_question_id_name category_question_id_name = new Category_question_id_name(category_question.getCategory_id(), category_question.getCategory_name());
            LIST.add(category_question_id_name);

        }
        return LIST;
    }

    //    @Cacheable(value = "question_filter" , key = "#SortBy")
    public List<Question> load_all_Filter(String Filter) {
        if (Filter.equals("View")) {
            List<Question> questionList = question_repository.findByViewContaining();

            return questionList;
        } else if (Filter.equals("Replys")) {

            List<Question> questionList = question_repository.findByReplyContaining();

            return questionList;

        } else {
            return null;
        }

    }

    //    @Cacheable(value = "question_page" ,  key = "#page.concat('-').concat( #SortBy)")
    public List<QuestionDetailResponse> load_with_Filter(String page, String Filter) {
        if (Filter.equals("Default")) {
            return load_all_with_page(page, "None");
        }
        List<Question> questionList = new ArrayList<>();
        questionList = load_all_Filter(Filter);


        PagedListHolder pagedListHolder = new PagedListHolder<>(questionList);

        pagedListHolder.setPage(Integer.parseInt(page) - 1);
        pagedListHolder.setPageSize(10);
        List<Question> list1 = pagedListHolder.getPageList();
        List<QuestionDetailResponse> question_detail_respons = new ArrayList<>();

        for (Question question1 : list1) {

            QuestionDetailResponse question_detailResponse = new QuestionDetailResponse();
            question_detailResponse.setQuestion(question1);
            question_detailResponse.setReply_size(question1.getReply().size());
            List<Category_Question> category_questions = question1.getCategory_questions();
            List<Category_question_id_name> category_question_id_names = new ArrayList<>();


            for (Category_Question category_question : category_questions) {

                category_question_id_names.add(new Category_question_id_name(category_question.getCategory_id(), category_question.getCategory_name()));

            }
            question_detailResponse.setCategory_questionList(category_question_id_names);

            question_detail_respons.add(question_detailResponse);

        }

        ArrayList<QuestionDetailResponse> LIST = new ArrayList<QuestionDetailResponse>(question_detail_respons.subList(0, question_detail_respons.size()));
        return LIST;
    }


    public List<QuestionDetailResponse> get_tag_list(String id, String page) {
        Category_Question category_question = category_question_repository.findByCategory_id(Integer.valueOf(id));
        List<Question> list = category_question.getQuestionList();

        PagedListHolder pagedListHolder = new PagedListHolder<>(list);

        pagedListHolder.setPage(Integer.parseInt(page) - 1);
        pagedListHolder.setPageSize(10);
        List<Question> list1 = pagedListHolder.getPageList();
        List<QuestionDetailResponse> question_detail_respons = new ArrayList<>();

        for (Question question1 : list1) {
            QuestionDetailResponse question_detailResponse = new QuestionDetailResponse();
            List<Category_Question> category_questions = question1.getCategory_questions();
            List<Category_question_id_name> category_question_id_names = new ArrayList<>();

            for (Category_Question category_question_ : category_questions) {
                category_question_id_names.add(new Category_question_id_name(category_question.getCategory_id(), category_question.getCategory_name()));
            }
            question_detailResponse.setQuestion(question1);
            question_detailResponse.setReply_size(question1.getReply().size());
            question_detail_respons.add(question_detailResponse);
            question_detailResponse.setCategory_questionList(category_question_id_names);

        }


        ArrayList<QuestionDetailResponse> LIST = new ArrayList<QuestionDetailResponse>(question_detail_respons.subList(0, question_detail_respons.size()));

        return LIST;
    }

    @Cacheable(value = "Active_question")

    public List<Question> get_Active_question() {

        List<Question> active_question = new ArrayList<>();

        List<Question> questionList = question_repository.findAll();
        Collections.reverse(questionList);
        for (Question question1 : questionList) {
            if (question1.getReply().size() == 0) {
                active_question.add(question1);
            }
        }
        return active_question;

    }

    public List<Question> get_question() {
        List<Question> result = new ArrayList<>();
        List<Question> questionList = new ArrayList<>();
        questionList = question_repository.findAll();
        for (Question question : questionList) {
            if (question.getCategory_questions().size() > 2) {
                result.add(question);
            }
        }
        return result;
    }


}



