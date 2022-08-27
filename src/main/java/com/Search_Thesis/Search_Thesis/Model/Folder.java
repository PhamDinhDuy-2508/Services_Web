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
    @Column
    private int idFolder ;

    @Expose
    @Column(name = "Title")
    private  String title ;

    @Expose
    @Column(name =  "Publish_date")
    private Date Publish_date ;

//    @ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
//    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
//    @JsonIgnore
//    @JoinColumn(name = "Category_id_key", referencedColumnName ="category_id" , nullable = true )
    @ManyToOne
    @JoinColumn(name = "Category_id_key")
    private  Category_document categorydocument ;

}

