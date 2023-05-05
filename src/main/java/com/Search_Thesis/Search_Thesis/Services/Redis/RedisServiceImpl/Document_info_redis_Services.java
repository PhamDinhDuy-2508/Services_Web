package com.Search_Thesis.Search_Thesis.Services.Redis.RedisServiceImpl;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Document_info_redis;
import com.Search_Thesis.Search_Thesis.Services.Redis.Services_Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
@Service
public class Document_info_redis_Services implements Services_Redis<Document_info_redis, List<Document>> {

    private  String Haskey =  "Delete_document" ;

    @Autowired
    RedisTemplate redisTemplate  ;





    Document_info_redis document_info_redis =  new Document_info_redis();

    @Override
    public Document_info_redis find(String haskey, String ID) {
        return (Document_info_redis) redisTemplate.opsForHash().values(Haskey);
    }

    @Override
    public Document_info_redis findProductById(String userid, int ID) {

        return (Document_info_redis) redisTemplate.opsForHash().get(Haskey ,  userid);

    }

    public Boolean deleteProduct(String ID , int id) {
        try {
            redisTemplate.opsForHash().delete(this.Haskey, ID);
            return true ;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void save_folder_ID(String ID, List<Document> documents) {
        Document_info_redis document_redis =  new Document_info_redis() ;

        document_redis =  Convert_to_Document_Redis( documents , ID) ;
        LocalDateTime now = LocalDateTime.now();

        ID += "_"+now.toString() ;

        System.out.println(redisTemplate.opsForHash().keys(this.Haskey));


        redisTemplate.opsForHash().put(this.Haskey , ID , document_redis);
        redisTemplate.expire(ID , 7 , TimeUnit.DAYS) ;

    }
    public Document_info_redis Convert_to_Document_Redis(List<Document> document , String ID ) {

        try{

            List<String> key = redisTemplate.opsForHash().keys("Delete_document").stream().toList();
            for( String keys  : key) {
                System.out.println(redisTemplate.opsForHash().get(this.Haskey ,  keys));
            }

            List<Document_info_redis> document_info_redisList =  new ArrayList<>() ;
            Document_info_redis document_info_redis1 =  new Document_info_redis() ;

            document_info_redis1.setDocument(document);
            document_info_redis1.setID(ID);

            return   document_info_redis1 ;
        }
        catch (Exception e) {
            Document_info_redis document_info_redis = new Document_info_redis() ;
            document_info_redis.setDocument(document);
            document_info_redis.setID(ID);

            return   document_info_redis ;
        }
    }
    public void Expire( List<Document> documents, String id_folder){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        Document_info_redis  document_redis =  new Document_info_redis() ;

        document_redis =  Convert_to_Document_Redis(  documents , id_folder) ;

        document_redis.setID(now.toString());

        redisTemplate.opsForHash().put("1_Expire" , now, document_redis); ;
        redisTemplate.opsForHash().values("1_Expire");
    }
    public Set<?> getHashKey(String hashkey) {

        return redisTemplate.opsForHash().keys(hashkey);

    }

    public Document_info_redis  findByTime(LocalDateTime localDateTime) {
        Document_info_redis document_info_redis1  = (Document_info_redis) redisTemplate.opsForHash().get("1_Expire" ,  localDateTime);
        System.out.println(document_info_redis1);
      return document_info_redis1;
    }
    public void Delete_Expired_Data(LocalDateTime localDateTime){
        redisTemplate.opsForHash().delete("1_Expire" ,  localDateTime) ;
    }

}
