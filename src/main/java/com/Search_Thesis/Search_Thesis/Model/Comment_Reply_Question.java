package com.Search_Thesis.Search_Thesis.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "comment_reply_question")
@Entity
public class Comment_Reply_Question implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;

    @Id
    @Column(name = "idcomment_reply_question")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int ID ;

    @Column(name = "content")
    private  String content ;

    @Column(name = "Date")
    private Date date ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "user_id"  , referencedColumnName = "user_id" ,columnDefinition = "json"
            , nullable = true)
    private User user_comment ;


    @JsonIgnore
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id"  , referencedColumnName = "idreply" ,columnDefinition = "json"
            , nullable = true)
    private Reply reply ;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser_comment() {
        return user_comment;
    }

    public void setUser_comment(User user_comment) {
        this.user_comment = user_comment;
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }
}
