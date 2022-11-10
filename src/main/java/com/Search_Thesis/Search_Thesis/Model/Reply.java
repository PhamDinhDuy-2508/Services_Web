package com.Search_Thesis.Search_Thesis.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Component
@Entity
@Table(name = "reply")
@EnableAutoConfiguration
public class Reply  implements Serializable {
    private static final long  serialVersionUID = -297553281792804395L;

    public int getReply_id() {
        return Reply_id;
    }

    public void setReply_id(int reply_id) {
        Reply_id = reply_id;
    }

    public Date getDate_create() {
        return Date_create;
    }

    public void setDate_create(Date date_create) {
        Date_create = date_create;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<Comment_Reply_Question> getComment_reply_questionList() {
        return comment_reply_questionList;
    }

    public void setComment_reply_questionList(Collection<Comment_Reply_Question> comment_reply_questionList) {
        this.comment_reply_questionList = comment_reply_questionList;
    }


    @Id
    @Column( name = "idreply")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int Reply_id ;

    @Column(name = "Date_create")
    private Date Date_create ;

    @Column(name =  "Content")
    private String content ;

    @ManyToOne(fetch = FetchType.EAGER)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @JsonIgnore

    @JoinColumn(name = "Question_id"  , referencedColumnName = "id_question" ,columnDefinition = "json"
            , nullable = true)
    // LAZY để tránh việc truy xuất dữ liệu không cần thiết. Lúc nào cần thì mới query
    private Question question ;



    @ManyToOne( fetch = FetchType.LAZY)
//    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @JoinColumn(name = "user_user_id"  , referencedColumnName = "user_id" ,columnDefinition = "json"
            , nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @Expose
    private User user ;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "reply")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Collection<Comment_Reply_Question> comment_reply_questionList ;




}
