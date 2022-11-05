package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Model.Comment_Reply_Question;
import com.Search_Thesis.Search_Thesis.Model.Question;
import com.Search_Thesis.Search_Thesis.Model.Reply;
import com.Search_Thesis.Search_Thesis.Payload.*;
import com.Search_Thesis.Search_Thesis.Services.QandA_Services;
import com.Search_Thesis.Search_Thesis.resposity.Question_Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ckt")
public class QandA_rest {
    @Autowired
    QandA_Services qandA_services ;
    @Autowired
    Question_Repository question_repository ;

    @Autowired
    Question_info_response question_info_response ;


    @GetMapping("/get_list_size")

    public ResponseEntity<?> load_number(){
        return ResponseEntity.ok( qandA_services.get_size_list() );
    }

    @GetMapping("/load_all/{page}")

    public ResponseEntity<?> load_all(@RequestParam("Filter") String Filter, @PathVariable String page){

        List<Question> list =   qandA_services.load_all_with_page(page , Filter) ;


      return ResponseEntity.ok( list);
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
        question_info_response.setQuestion(question);
        question_info_response.setCreator(question.getCreator());

        return ResponseEntity.ok(question_info_response) ;
    }
    //test_Api
   @PostMapping("/upload_image")
    public ResponseEntity<?> Upload_image(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        List<String> pos = new ArrayList<>() ;
        pos.add("1") ;

        try {
            qandA_services.upload(multipartFile , "123" ,pos);

        }catch (Exception e) {}
       return null ;
   }

   @GetMapping("/load_question/{id}")
    public ResponseEntity<?> load_quest(@PathVariable String id) {
       Question question1 = qandA_services.load_question(id);

       qandA_services.Increment_view(id);
       question_info_response.setQuestion(question1);
       question_info_response.setCreator(question1.getCreator());

      return   ResponseEntity.ok(question_info_response) ;
   }

    @PostMapping("/reply")
    public ResponseEntity<?> upload_reply( @RequestBody  Reply_request reply_request) {
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
            System.out.println(replyList);
            return  ResponseEntity.ok(replyList) ;

        }
        catch (Exception e) {
            return  ResponseEntity.ok(null) ;

        }

   }
   @PostMapping("/comment")
   public ResponseEntity<?> Comment( @Valid @RequestBody Comment_Request comment_request) {
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
   @GetMapping("/load_comment/{id}")
   public ResponseEntity<?> Comment( @PathVariable String id) {
       try {
           List<Comment_Reply_Question> lisgt =  qandA_services.load_Comment(id);
           return ResponseEntity.ok(lisgt) ;
       }
       catch (Exception e) {

           return ResponseEntity.ok(null) ;
       }

   }

   @GetMapping("/load_page_reply/{id}/{page}")
   public  ResponseEntity<?> load_reply_page(@PathVariable String page, @PathVariable String id) throws JsonProcessingException {
       Reply_response Page = qandA_services.response_question_pagination(id , Integer.parseInt(page));

      return ResponseEntity.ok(Page) ;

   }

}


