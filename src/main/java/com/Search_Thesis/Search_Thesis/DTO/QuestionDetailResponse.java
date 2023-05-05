package com.Search_Thesis.Search_Thesis.DTO;

import com.Search_Thesis.Search_Thesis.Model.Question;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionDetailResponse implements Serializable{
    private static final long  serialVersionUID = -297553281792804395L;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Question question ;
    private List<Category_question_id_name> category_questionList ;
    private  int reply_size = 0 ;
}
