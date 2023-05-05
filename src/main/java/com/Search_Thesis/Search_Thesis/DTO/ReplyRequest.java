package com.Search_Thesis.Search_Thesis.DTO;

import lombok.Data;

@Data
public class ReplyRequest {
    private String content;

    private String question_id;

    private String token;
}

