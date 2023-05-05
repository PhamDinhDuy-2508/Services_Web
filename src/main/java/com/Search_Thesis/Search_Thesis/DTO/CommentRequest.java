package com.Search_Thesis.Search_Thesis.DTO;

import lombok.Data;

@Data
public class CommentRequest {
    private String content ;
    private String token ;
    private String id ;
    private String reply_id ;
}
