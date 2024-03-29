package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.DTO.*;
import com.Search_Thesis.Search_Thesis.Model.Comment_Reply_Question;
import com.Search_Thesis.Search_Thesis.Model.Question;
import com.Search_Thesis.Search_Thesis.Model.Reply;
import com.Search_Thesis.Search_Thesis.Services.Drive.DriveService;
import com.Search_Thesis.Search_Thesis.Services.QandAServices.QandAServices;
import com.Search_Thesis.Search_Thesis.repository.Question_Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ckt")
public class QandA_rest {

    @Autowired
    QandAServices qandA_services ;
    @Autowired
    Question_Repository question_repository ;
    @Autowired
    @Qualifier("DriveService")
    DriveService drive_service ;

    @Autowired
    QuestionInfoResponse question_infoResponse;


    @GetMapping("/get_list_size")

    public ResponseEntity<?> load_number(){
        return ResponseEntity.ok( qandA_services.get_size_list() );
    }

    @GetMapping("load_Filter/{Filter}/{page}")
    public ResponseEntity<?> load_Filter(@PathVariable String Filter, @PathVariable String page) {
        try {
            List<QuestionDetailResponse> questionList = qandA_services.load_with_Filter(page, Filter);
            return  ResponseEntity.ok(questionList) ;

        }
        catch (Exception e) {
            return  ResponseEntity.ok(e.getMessage()) ;
        }
    }
    @GetMapping("/load_all/{page}")
    public ResponseEntity<?> load_all(@RequestParam("Filter") String Filter, @PathVariable String page){
        try {
            List<QuestionDetailResponse> list = qandA_services.load_all_with_page(page, Filter);

            return ResponseEntity.ok( list);

        }
        catch (Exception e) {
            return ResponseEntity.ok( null);
        }
    }

    @GetMapping("/get_number_page")
    public ResponseEntity<Integer> get_number() {
        return ResponseEntity.ok(qandA_services.get_amount() ) ;
    }

    @GetMapping("/load_By_Category")
    public  ResponseEntity load_By_category(String Category) {
        List<Question> questionList  = new ArrayList<>() ;
        return null ;
    }

    @PostMapping("/create_question")
    public void Create_Question(@Valid @RequestBody QuestionCreate create_question , @RequestParam("token") String token) throws ParseException {

        qandA_services.Create_Question(create_question ,  token);

    }
    @GetMapping("/load_detail_question/{id}")
    public ResponseEntity<?> Load_Question(@PathVariable String id) {

        qandA_services.Increment_view(id);



        Question question =  qandA_services.load_question_detail(Integer.parseInt(id)) ;

        question_infoResponse.setQuestion(question);

        question_infoResponse.setCreator(question.getCreator());

        return ResponseEntity.ok(question_infoResponse) ;
    }

   @GetMapping("/load_question/{id}")
    public ResponseEntity<?> load_quest(@PathVariable String id) {
       Question question1 = qandA_services.load_question(id);

       qandA_services.Increment_view(id);

       question_infoResponse.setQuestion(question1);


       question_infoResponse.setCreator(question1.getCreator());

      return   ResponseEntity.ok(question_infoResponse) ;
   }

    @PostMapping("/reply")
    public ResponseEntity<?> upload_reply( @RequestBody ReplyRequest reply_request) {
        try {
            qandA_services.upload_reply(reply_request);
            return ResponseEntity.ok(true ) ;
        }
        catch (Exception e) {
            return  ResponseEntity.ok(false ) ;
        }
   }
   @GetMapping("/load_reply/{question_id}")
   public ResponseEntity<?> get_reply(@PathVariable String question_id) {
        try {
            List<Reply> replyList = qandA_services.load_reply(question_id);
            return  ResponseEntity.ok(replyList) ;

        }
        catch (Exception e) {
            return  ResponseEntity.ok(null) ;

        }

   }
   @PostMapping("/comment")
   public ResponseEntity<?> Comment( @Valid @RequestBody CommentRequest comment_request) {
       System.out.println(comment_request);

        try {
            qandA_services.insert_Comment(comment_request);
            return ResponseEntity.ok(true) ;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(false) ;

        }
   }
   @GetMapping("/load_comment/{id}") // add id_question
   public ResponseEntity<?> Comment(@PathVariable String id) {
       try {
           List<Comment_Reply_Question> lisgt =  qandA_services.load_Comment(id, "1");
           return ResponseEntity.ok(lisgt) ;
       }
       catch (Exception e) {

           return ResponseEntity.ok(null) ;

       }
   }
   @GetMapping("/load_page_reply/{id}/{page}")
   public  ResponseEntity<?> load_reply_page(@PathVariable String page, @PathVariable String id) throws JsonProcessingException {
        try {
            List<Reply> Page = qandA_services.response_question_pagination(id, page);

            return ResponseEntity.ok(Page) ;

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(null) ;
        }
   }
   @GetMapping("/test_cache_cmt/{id_ques}/{id_rep}")
   public  ResponseEntity<?> test_API(@PathVariable String id_ques, @PathVariable String id_rep){

        Comment_Reply_Question comment_reply_question =  new Comment_Reply_Question() ;

        comment_reply_question.setContent("phamdinhduy_test");

        qandA_services.update_comment_cache(id_rep , id_ques , comment_reply_question);

        return ResponseEntity.ok(true) ;

   }

   @GetMapping("/load_topic")
   public ResponseEntity<?> load_topic() {
        try {
            JSONObject json = new JSONObject();
            System.out.println(qandA_services.load_Category().size());
            json.put("size_topic", qandA_services.load_Category().size());
            json.put("size_post", qandA_services.load_all().size());
            json.put("topic_detail", qandA_services.load_Category());
            return ResponseEntity.ok( json.toString() ) ;

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return ResponseEntity.ok( "" ) ;

        }
   }

   @GetMapping("/load_By_Category/{id}/{page}")
   public  ResponseEntity<?> load_By_Category(@PathVariable String id, @PathVariable String page) {
      List<QuestionDetailResponse> questionList =   qandA_services.get_tag_list(id , page) ;

       return ResponseEntity.ok(questionList) ;

   }

   @GetMapping("/test_Cache_ques")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok( qandA_services.test_Question_Cache() )  ;
   }
   @GetMapping("/test_google_Drive")
    public  ResponseEntity<?> test_2() throws Exception {

        return ResponseEntity.ok( drive_service.listFolderContent("1Zq8yhHNPbcNV3pH7Zz8MV28cKxZc3SVm"));

   }

   @GetMapping("/Active_question")
    public ResponseEntity<?> Active_topic() {

      List<Question>  questionList  =   qandA_services.get_Active_question() ;
       List<Question> arrlist2 =  new ArrayList<>()  ;
      if(questionList.size() > 5) {
          arrlist2 = questionList.subList(2, 4);
      }
      else {
           arrlist2 = questionList.subList(0, questionList.size());
      }
        return  ResponseEntity.ok(arrlist2) ;

   }

   @GetMapping("/test")
    public ResponseEntity<?> Test() {
        qandA_services.get_question() ;
        return   ResponseEntity.ok(qandA_services.get_question()) ;
   }

   }


