package com.Search_Thesis.Search_Thesis.Model;

import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Component("document_")
@Entity
@Data
@Table(name = "document")

@EnableAutoConfiguration
public class Document implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_document")
    private  int ID ;

    @Column(name = "Title")
    private  String Title ;


    @Column(name ="File")
    private String File ;

    @Column(name = "Author")
    private  String Author ;

    @Column(name = "Publish_date")
    private Date Publish_date ;

    @Column(name = "Id_folder")

    private int Id_folder ;


}
