package com.Search_Thesis.Search_Thesis.Redis_Model;

import com.Search_Thesis.Search_Thesis.Model.Folder;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
@Data
@Component("redis_category")
@RedisHash("Category_info_redis")
@EnableRedisRepositories
@EnableAutoConfiguration
public class Category_Redis implements Serializable {

    private String Code ;
    private List<Folder> folderList ;
}

