package com.Search_Thesis.Search_Thesis.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
@Entity
@Table(name = "question")
@EnableAutoConfiguration
public class Question    implements Serializable {
    private static final long  serialVersionUID = 297553281792804396L;

    @Id
    @Expose
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_question")

    private  int Question_id = 0 ;



    @Column(name = "Date_create")

    private Date Date_Create ;

    @Column(name = "Content" )

    private String content ;

    @Column(name = "Title")
    private String Title ;
    @Column(name = "View")
    private  int View ;
    @Column(name = "Vote")
    private  int Vote ;
    @Column(name = "Creator_Email")
    private  String Email ;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }




    @ManyToOne(  fetch = FetchType.EAGER)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

    @JoinColumn(name = "Creator_id"  , referencedColumnName = "user_id" ,columnDefinition = "json"
            , nullable = true)
    private User creator;
//    @JsonIgnore
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    @ManyToMany(    fetch = FetchType.LAZY )

    @JoinTable(name =  "category_question_join" ,  joinColumns = @JoinColumn(name ="id_question" ) ,
            inverseJoinColumns =  @JoinColumn(name = "id_category" ) )

    private List<Category_Question> category_questions ;



    @JsonIgnore
    @OneToMany( fetch = FetchType.LAZY, mappedBy = "question")
//    @JsonIgnore
    private Collection< Reply  > reply ;

    public Collection<Reply> getReply() {
        return reply;
    }

    public void setReply(Collection<Reply> reply) {
        this.reply = reply;
    }



    public int getQuestion_id() {
        return Question_id;
    }

    public void setQuestion_id(int question_id) {
        Question_id = question_id;
    }

    public Date getDate_Create() {
        return Date_Create;
    }

    public void setDate_Create(Date date_Create) {
        Date_Create = date_Create;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getView() {
        return View;
    }

    public void setView(int view) {
        View = view;
    }

    public int getVote() {
        return Vote;
    }

    public void setVote(int vote) {
        Vote = vote;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }


    //    public Collection<Reply> getReply() {
//        return reply;
//    }
//
//    public void setReply(Collection<Reply> reply) {
//        this.reply = reply;
//    }

    public List<Category_Question> getCategory_questions() {
        return category_questions;
    }

    public void setCategory_questions(List<Category_Question> category_questions) {
        this.category_questions = category_questions;
    }


}
