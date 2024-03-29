package com.Search_Thesis.Search_Thesis.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringExclude;
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

    @JsonIgnore
    @Column(name = "PATH")
    private  String path ;

    @JsonIgnore
    @Column(name = "id_document_drive")
    private  String id_document_drive ;

    @Column(name = "Publish_date")
    private Date Publish_date ;

    @Column(name = "Id_folder")

    private int Id_folder ;
    @ManyToOne
    @JoinColumn(name = "FolderId")
    @EqualsAndHashCode.Exclude
    @ToStringExclude
    private  Folder folder ;


    @Override
    public String toString() {
        return "Document{" +
                "ID=" + ID +
                ", Title='" + Title + '\'' +
                ", File='" + File + '\'' +
                ", Author='" + Author + '\'' +
                ", Publish_date=" + Publish_date +
                ", Id_folder=" + Id_folder +
                '}';
    }
}
