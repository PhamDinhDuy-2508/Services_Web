package com.Search_Thesis.Search_Thesis.Model;

import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@EnableAutoConfiguration
@Component("category_document")
@Entity
@Table(name = "category_document")
public class Category_document {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name ="category_id" )
    private  int category_id = 0 ;

    @Column
    private  String name ;

    @Column
    private  String code ;

    @ManyToOne

    @JoinColumn(name = "root_id"  , referencedColumnName = "idroot_folder"
            , nullable = true)
    private  Root_Folder root_folder ;



}
