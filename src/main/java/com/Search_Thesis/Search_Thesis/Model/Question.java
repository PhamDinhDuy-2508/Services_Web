package com.Search_Thesis.Search_Thesis.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component("question")
@Entity
@Table(name = "question")
@EnableAutoConfiguration
public class Question {
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

    @ManyToOne(cascade = CascadeType.MERGE , fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @JsonIgnore
    @JoinColumn(name = "Creator_id"  , referencedColumnName = "user_id" ,columnDefinition = "json"
            , nullable = true)
    private User creator;
    @ManyToMany(mappedBy = "questionList")

    private List<Category_Question> category_questions ;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude

    @ToString.Exclude
    @JoinTable(name = "reply_question", //Tạo ra một join Table tên là "address_person"
            joinColumns = @JoinColumn(name = "id_ques"),  // TRong đó, khóa ngoại chính là address_id trỏ tới class hiện tại (Address)
            inverseJoinColumns = @JoinColumn(name = "id_reply") )//Khóa ngoại thứ 2 trỏ tới thuộc tính ở dưới (Person)
    private Collection< Reply  > reply ;

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

    @Override
    public String toString() {
        return "Question{" +
                "Question_id=" + Question_id +
                ", Date_Create=" + Date_Create +
                ", content='" + content + '\'' +
                ", Title='" + Title + '\'' +
                ", View=" + View +
                ", Vote=" + Vote +
                ", creator=" + creator +
                ", category_questions=" + category_questions +
                '}';
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
