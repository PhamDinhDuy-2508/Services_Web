package com.Search_Thesis.Search_Thesis.Model;

import com.google.gson.annotations.Expose;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
@Entity
@Table(name =  "catergory_question")
@Component

public class Category_Question implements Serializable {
    private static final long  serialVersionUID = -297553281792804396L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Expose
    @Column( name =   "idcatergory_question")

    private int Category_id ;

    @Column(name = "category_name")
    private  String category_name ;

//    @JoinTable(name =  "category_question_join" ,  joinColumns = @JoinColumn(name ="id_category" ) ,
//            inverseJoinColumns =  @JoinColumn(name = "id_question" ) )
////    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY )

    @ManyToMany(mappedBy = "category_questions")

    private List<Question> questionList ;

    public int getCategory_id() {
        return Category_id;
    }

    public void setCategory_id(int category_id) {
        Category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
}
