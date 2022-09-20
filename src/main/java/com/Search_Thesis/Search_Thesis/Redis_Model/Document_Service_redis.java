package com.Search_Thesis.Search_Thesis.Redis_Model;

import com.Search_Thesis.Search_Thesis.Model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service


public class Document_Service_redis  implements  Services_Redis<Document_redis , List<Document>>{
    private    String HASH_KEY = "_folder" ;
    @Autowired
    RedisTemplate  redisTemplate ;
    @Override
    public Document_redis find(String hashkey ,  String id) {
        return (Document_redis) redisTemplate.opsForHash().get(hashkey ,  id);
    }

    @Override
    public Document_redis findProductById(String userid, int ID) {
        String key = userid+this.HASH_KEY ;
        return (Document_redis) redisTemplate.opsForHash().get(HASH_KEY ,  ID) ;
    }

    @Override
    public Boolean deleteProduct(String user_id, int id) {
        try {
            redisTemplate.opsForHash().delete(HASH_KEY, id);
            return true ;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return  false ;
        }
    }

    @Override
    public void save_folder_ID(String ID, List<Document> documents) {
        redisTemplate.setEnableTransactionSupport(true) ;

        Document_redis document_redisList =  new Document_redis() ;

        HASH_KEY = ID + "_folder"  ;

        document_redisList =  Convert_to_Document_Redis(ID ,  documents) ;


        redisTemplate.opsForHash().put(HASH_KEY , ID , document_redisList);

        Document_redis document_redis = (Document_redis) redisTemplate.opsForHash().get(HASH_KEY, ID) ;

        return  ;

    }
    public Document_redis Convert_to_Document_Redis(String ID , List<Document> document) {
        Document_redis document_redis =  new Document_redis() ;
        document_redis.setDocuments(document);
        document_redis.setId_folder(Integer.parseInt(ID));
        return  document_redis ;
    }
}

