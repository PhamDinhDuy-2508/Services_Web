package com.Search_Thesis.Search_Thesis.Redis_Model;

import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Model.Document;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
@Data
@RedisHash("Folder_info")
@EnableRedisRepositories
@EnableAutoConfiguration

public class Folder_model_redis implements Serializable {

    private int idFolder  ;


    private  String title ;

    private Date Publish_date ;


    private Category_document categorydocument ;


    private  int Contributor_ID  ;

    private List<Document> documentList ;

    private  String url ;

}
