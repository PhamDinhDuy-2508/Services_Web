package com.Search_Thesis.Search_Thesis.Model;

import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
@Data
@RedisHash("Folder_info")
@EnableRedisRepositories
@EnableAutoConfiguration
@Component("Folder_info")
public class FolderRedisModel implements Serializable {

    private int idFolder  ;

    private  String title ;

    private Date Publish_date ;


    private Category_document categorydocument ;


    private  int Contributor_ID  ;

    private List<Document> documentList ;

    private  String url ;



}
