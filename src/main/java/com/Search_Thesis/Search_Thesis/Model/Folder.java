package com.Search_Thesis.Search_Thesis.Model;

import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;


@Entity
@Component("folder")
@EnableAutoConfiguration
@Table(name = "folder")
public class Folder implements Serializable {
    private static final long serialVersionUID = -2879936628319211082L;

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

    @OneToMany(mappedBy = "folder" , cascade = CascadeType.ALL)
    @ToStringExclude
    @HashCodeExclude
    private Collection<Document> documents ;

    public Collection<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Collection<Document> documents) {
        this.documents = documents;
    }

    public int getIdFolder() {
        return idFolder;
    }

    public void setIdFolder(int idFolder) {
        this.idFolder = idFolder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublish_date() {
        return Publish_date;
    }

    public void setPublish_date(Date publish_date) {
        Publish_date = publish_date;
    }

    public Category_document getCategorydocument() {
        return categorydocument;
    }

    public void setCategorydocument(Category_document categorydocument) {
        this.categorydocument = categorydocument;
    }

    public int getContributor_ID() {
        return Contributor_ID;
    }

    public void setContributor_ID(int contributor_ID) {
        Contributor_ID = contributor_ID;
    }
    public void setContributor_ID(String contributor_ID) {
        Contributor_ID = Integer.parseInt( contributor_ID );
    }




}

