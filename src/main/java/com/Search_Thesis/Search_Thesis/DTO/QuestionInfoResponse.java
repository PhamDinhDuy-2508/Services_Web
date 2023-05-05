package com.Search_Thesis.Search_Thesis.DTO;

import com.Search_Thesis.Search_Thesis.Model.Question;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component("Question_response")


@Data
public class QuestionInfoResponse implements Serializable {
    private Question question ;

    @Expose

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User Creator= new User() ;
}
