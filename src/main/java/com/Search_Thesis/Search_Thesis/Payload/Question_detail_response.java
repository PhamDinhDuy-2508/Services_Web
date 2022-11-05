package com.Search_Thesis.Search_Thesis.Payload;

import com.Search_Thesis.Search_Thesis.Model.Question;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class Question_detail_response {
    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

    private Question question ;

    private List<Reply_response> Reply;
}
