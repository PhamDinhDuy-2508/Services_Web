package com.Search_Thesis.Search_Thesis.Payload;

import lombok.Data;

@Data
public class Comment_Request {
    private String content ;
    private String token ;
    private String id ;
    private String reply_id ;

}
