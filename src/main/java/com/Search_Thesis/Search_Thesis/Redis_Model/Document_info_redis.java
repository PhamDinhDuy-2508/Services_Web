package com.Search_Thesis.Search_Thesis.Redis_Model;

import com.Search_Thesis.Search_Thesis.Model.Document;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
@Component("document_redis_info")
@Data
@RedisHash("Document_info_redis")
@EnableRedisRepositories
@EnableAutoConfiguration
public class Document_info_redis  implements Serializable {

  private List<Document> document ;
  private LocalDateTime  End_Date ;


}

