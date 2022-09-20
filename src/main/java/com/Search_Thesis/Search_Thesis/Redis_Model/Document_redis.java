package com.Search_Thesis.Search_Thesis.Redis_Model;

import com.Search_Thesis.Search_Thesis.Model.Document;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.io.Serializable;
import java.util.List;

@Data
@RedisHash("Document_info")
@EnableRedisRepositories
@EnableAutoConfiguration
public class Document_redis  implements Serializable {

    private int Id_folder ;
    private List<Document> documents ;

    @Override
    public String toString() {
        return "Document_redis{" +
                "Id_folder=" + Id_folder +
                ", documents=" + documents +
                '}';
    }
}
