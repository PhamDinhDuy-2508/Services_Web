package com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService.RedisServiceImpl;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Document_redis;
import com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService.Services_Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Document_Service_redis  implements Services_Redis<Document_redis, List<Document>> {
    private    String HASH_KEY = "_folder" ;
    @Autowired
    RedisTemplate  redisTemplate ;
    @Override
    public Document_redis find(String hashkey ,  String id) {
        return (Document_redis) redisTemplate.opsForHash().get(hashkey ,  id);
    }

    @Override
    public Document_redis findProductById(String userid, int ID) {
        return (Document_redis) redisTemplate.opsForHash().get(HASH_KEY ,  ID) ;
    }


    @Override
    public void save_folder_ID(String ID, List<Document> documents) {
        redisTemplate.setEnableTransactionSupport(true) ;

        Document_redis document_redisList =  new Document_redis() ;

        HASH_KEY = ID + "_folder"  ;

        document_redisList =  Convert_to_Document_Redis(ID ,  documents) ;

        redisTemplate.opsForHash().put(HASH_KEY , ID , document_redisList);


    }
    public Document_redis Convert_to_Document_Redis(String ID , List<Document> document) {
        Document_redis document_redis =  new Document_redis() ;
        document_redis.setDocuments(document);
        document_redis.setId_folder(Integer.parseInt(ID));
        return  document_redis ;
    }
}

