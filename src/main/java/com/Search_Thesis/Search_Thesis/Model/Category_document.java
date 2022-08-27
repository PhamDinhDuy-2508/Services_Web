package com.Search_Thesis.Search_Thesis.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Data
@EnableAutoConfiguration
@Component("category_document")
@Entity
@Table(name = "category_document")

public class Category_document {


    @Id
    @Expose
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name ="category_id" )

    private  int category_id = 0 ;

    @Column
    @Expose
    private  String name ;

    @Column
    @Expose


    private  String code ;

    @ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @JsonIgnore
    @JoinColumn(name = "root_id"  , referencedColumnName = "idroot_folder" ,columnDefinition = "json"
            , nullable = true)
    private  Root_Folder root_folder ;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "categorydocument")
    @JsonIgnore
    private List<Folder> newfolder ;

    @Override

    public String toString() {
        return "Category_document{" +
                "category_id=" + category_id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

}
