package com.Search_Thesis.Search_Thesis.repository;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.Document_redis;
import com.Search_Thesis.Search_Thesis.Model.FolderRedisModel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Data
public  class  Folder_Reids_respository  {

    private String HASH_KEY  ;

    @Autowired
    RedisTemplate  redisTemplate ;

    @Async

    public FolderRedisModel save(String user_id  , FolderRedisModel folder)  {

        String test ="1_2508_folder" ;

        HASH_KEY = user_id+"_"+folder.getIdFolder()+"_folder"  ;

        System.out.println(HASH_KEY.length());

        redisTemplate.opsForHash().put(HASH_KEY , folder.getIdFolder() , folder);

        return folder ;
    }

    public List<Folder> find(String id_folder){
        HASH_KEY =  id_folder+"_folder"  ;

        return redisTemplate.opsForHash().values(HASH_KEY);
    }

    public Folder_Reids_respository findProductById(String ID , int id){
        HASH_KEY =  ID+"_folder"  ;

        return (Folder_Reids_respository) redisTemplate.opsForHash().get(HASH_KEY, id);
    }

    public boolean deleteProduct(String user_id , int  id){
        try {
            redisTemplate.opsForHash().delete(HASH_KEY, id);
            return true ;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return  false ;
        }
    }
    @Async
    public void save_folder_ID(String ID  , List<Document> documents)  {
        redisTemplate.setEnableTransactionSupport(true) ;

        Document_redis document_redisList =  new Document_redis() ;

        HASH_KEY = ID + "_folder"  ;

        document_redisList =  Convert_to_Document_Redis(ID ,  documents) ;


        redisTemplate.opsForHash().put(HASH_KEY , ID , document_redisList);


        Document_redis document_redis = (Document_redis) redisTemplate.opsForHash().get(HASH_KEY, ID) ;
        System.out.println(document_redis.toString());
        return  ;
    }
    public Document_redis Convert_to_Document_Redis(String ID , List<Document> document) {
        Document_redis document_redis =  new Document_redis() ;
        document_redis.setDocuments(document);
        document_redis.setId_folder(Integer.parseInt(ID));
        return  document_redis ;
    }


}
