package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Model.Question;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Payload.QuestionCreate;
import com.Search_Thesis.Search_Thesis.Payload.Question_info_response;
import com.Search_Thesis.Search_Thesis.Payload.Reply_request;
import com.Search_Thesis.Search_Thesis.Services.QandA_Services;
import com.Search_Thesis.Search_Thesis.resposity.Question_Repository;
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

        List<Question> list =   qandA_services.load_all_with_page(page , Filter).getContent() ;

        List<Question_info_response> question_info_responses = new ArrayList<>()   ;
        try {
            for (Question question : list) {
                question_info_response = new Question_info_response();
                question_info_response.setQuestion(question);
                User user =  question.getCreator() ;
                System.out.println(user);

                question_info_response.setCreator(user);


                question_info_responses.add(question_info_response);

            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }


      return ResponseEntity.ok( question_info_responses);
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
        System.out.println(create_question);

        qandA_services.Create_Question(create_question ,  token);

    }
    @GetMapping("/load_detail_question/{id}")
    public ResponseEntity<?> Load_Question(@PathVariable String id) {

        qandA_services.Increment_view(id);
        Question question =  qandA_services.load_question_detail(Integer.parseInt(id)) ;
        return ResponseEntity.ok(question) ;
    }
    //test_Api
   @PostMapping("/upload_image")
    public ResponseEntity<?> Upload_image(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        List<String> pos = new ArrayList<>() ;
       System.out.println(multipartFile.getBytes());
        pos.add("1") ;

        try {
            qandA_services.upload(multipartFile , "123" ,pos);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
       return null ;
   }
   @GetMapping("/load_question/{id}")
    public ResponseEntity<?> load_quest(@PathVariable String id) {
        Question question1 = qandA_services.load_question(id);
        qandA_services.Increment_view(id);
      return   ResponseEntity.ok(question1) ;
   }

    @PostMapping("/reply")
    public void upload_reply( @RequestBody  Reply_request reply_request) {
        try {
            qandA_services.upload_reply(reply_request);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

   }




}


