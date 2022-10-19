package com.Search_Thesis.Search_Thesis.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@EnableAutoConfiguration
@Component("category_document")
@Entity
@Table(name = "category_document")

public class Category_document implements Serializable {


    @Id
    @Expose
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name ="category_id" )

    private  int category_id =0 ;

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
    private Set<Folder> newfolder ;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Root_Folder getRoot_folder() {
        return root_folder;
    }

    public void setRoot_folder(Root_Folder root_folder) {
        this.root_folder = root_folder;
    }

    public Set<Folder> getNewfolder() {
        return newfolder;
    }

    public void setNewfolder(Set<Folder> newfolder) {
        this.newfolder = newfolder;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
