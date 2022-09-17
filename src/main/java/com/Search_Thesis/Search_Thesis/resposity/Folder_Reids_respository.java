package com.Search_Thesis.Search_Thesis.resposity;

import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Redis_Model.Folder_model_redis;
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

    public Folder_model_redis save(String user_id  ,  Folder_model_redis folder)  {

        HASH_KEY =  user_id+"_folder"  ;
        redisTemplate.opsForHash().put(HASH_KEY , folder.getIdFolder() , folder);
        return folder ;
    }

    public List<Folder> find(String userid){
        HASH_KEY =  userid+"_folder"  ;
        return redisTemplate.opsForHash().values(userid);
    }

    public Folder_Reids_respository findProductById(String user_id , int id){
        HASH_KEY =  user_id+"_folder"  ;

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

}
