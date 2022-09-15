package com.Search_Thesis.Search_Thesis.Model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Data
@Component("folder")
@EnableAutoConfiguration
@Table(name = "folder")
public class Folder {

    @Id
    @Expose
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_folder")
    private int idFolder  =0  ;

    @Expose
    @Column(name = "Title")
    private  String title ;

    @Expose
    @Column(name =  "Publish_date")
    private Date Publish_date ;

    @ManyToOne
    @JoinColumn(name = "Category_id_key" , referencedColumnName = "category_id" ,columnDefinition = "json"
            , nullable = true)
    private  Category_document categorydocument ;

    @Column(name = "Contributor_id")
    private  int Contributor_ID  ;

}

