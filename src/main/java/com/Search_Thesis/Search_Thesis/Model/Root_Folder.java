package com.Search_Thesis.Search_Thesis.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name= "root_folder")
@Component("root")
@EnableAutoConfiguration
public class Root_Folder {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "idroot_folder")
    private  int id ;

    @Column
    private  String name ;

    @Column(name = "parent_id")
    @JsonIgnore
    private  String parentId ;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "root_folder")

    private Set< Category_document> category_document ;



    @Override
    public String toString() {
        return "Root_Folder{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category_document=" + category_document.size()  +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Category_document> getCategory_document() {
        return category_document;
    }

    public void setCategory_document(Set<Category_document> category_document) {
        this.category_document = category_document;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
