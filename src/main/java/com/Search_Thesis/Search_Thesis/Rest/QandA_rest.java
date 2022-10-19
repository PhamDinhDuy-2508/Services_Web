package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Model.Question;
import com.Search_Thesis.Search_Thesis.Payload.QuestionCreate;
import com.Search_Thesis.Search_Thesis.Services.QandA_Services;
import com.Search_Thesis.Search_Thesis.resposity.Question_Repository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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

    @GetMapping("/load_all/{page}")

    public ResponseEntity<?> load_all(@RequestParam("Filter") String Filter, @PathVariable String page){
        List<Question> list =  question_repository.findAll() ;

      return ResponseEntity.ok( qandA_services.load_all(page , Filter) );
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
        return ResponseEntity.ok(question) ;

    }
}
@Data
class Create_Question {
    @NotBlank
    private  String Title ;
    @NotBlank
    private  String Content ;
    @NotBlank
    private  String Category ;

    private  String name_display ;
    private  String email_send ;
}
