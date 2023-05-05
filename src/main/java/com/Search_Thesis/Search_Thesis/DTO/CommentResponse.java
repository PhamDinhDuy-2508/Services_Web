package com.Search_Thesis.Search_Thesis.DTO;

import com.Search_Thesis.Search_Thesis.Model.Comment_Reply_Question;
import lombok.Data;

import java.io.Serializable;
@Data
public class CommentResponse implements Serializable {
    private Comment_Reply_Question comment_reply_question;
    private String username ;
    private int userID;
}
