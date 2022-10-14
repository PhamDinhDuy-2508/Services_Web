package com.Search_Thesis.Search_Thesis.Redis_Model;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Respository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
@Service
public class Folder_info_Services implements  Services_Redis<Folder_model_redis ,List<Document>  > {

    private final String Hash_Key =  "Delete_folder" ;
    private Folder_model_redis folder ;

    @Autowired
    RedisTemplate redisTemplate ;

    @Autowired
    Folder_Respository folder_respository ;
    @Override
    public Folder_model_redis find(String haskey, String ID) {
        return null;
    }

    @Override
    public Folder_model_redis findProductById(String userid, int ID) {
        Folder_model_redis folder_model_redis =  new Folder_model_redis() ;
        folder_model_redis =  (Folder_model_redis) redisTemplate.opsForHash().get(Hash_Key ,  userid);

        return folder_model_redis ;

    }

    @Override
    public Boolean deleteProduct(String user_id, int id) {
        return null;
    }

    @Override
    public void save_folder_ID(String ID, List<Document> elemment) {

        Folder folder1 = folder_respository.findByIdFolder(Integer.parseInt(ID)) ;
        System.out.println( folder_respository.findByIdFolder(Integer.parseInt(ID)) );

        String root_name = "Chuyen Nganh" ;
        String Category =  folder1.getCategorydocument().getCode() ;
        String folder_name =  folder1.getTitle() ;
        int Category_ID = folder1.getCategorydocument().getCategory_id();

        String url =  "D:\\Data\\Document_data\\"+root_name+"\\" + Category+"\\" +  folder_name;
        Folder_model_redis folder_model_redis  =  Convert_to_Document_Redis(folder1    ,  elemment ,  url) ;
        Expire(folder1 ,  elemment ,  url);

        LocalDateTime localDateTime =  LocalDateTime.now() ;
        String id =  folder1.getContributor_ID() + "_"+localDateTime.toString() ;

        redisTemplate.opsForHash().put(this.Hash_Key , id  , folder_model_redis);
        redisTemplate.expire(ID , 7 , TimeUnit.DAYS) ;

    }
    public Folder_model_redis Convert_to_Document_Redis(Folder folder ,  List<Document> documents , String url) {

        Folder_model_redis folder_model_redis =  new Folder_model_redis() ;
        folder_model_redis.setIdFolder(folder.getIdFolder());
        folder_model_redis.setDocumentList(documents);
        folder_model_redis.setTitle(folder.getTitle());
        folder_model_redis.setUrl(url);
        folder_model_redis.setCategorydocument(folder.getCategorydocument());


        return   folder_model_redis ;
    }
    public void Expire(Folder folder , List<Document> documents , String url){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        Folder_model_redis  document_redis =  new Folder_model_redis() ;

        document_redis =  Convert_to_Document_Redis( folder ,  documents , url) ;


        redisTemplate.opsForHash().put("1_Expire" , now, document_redis); ;

        redisTemplate.opsForHash().values("1_Expire");
    }
    public Set<?> getHashKey(String hashkey) {

        return redisTemplate.opsForHash().keys(hashkey);

    }

    public Folder_model_redis findByTime(LocalDateTime localDateTime) {
        Folder_model_redis folder_model_redis = ( Folder_model_redis) redisTemplate.opsForHash().get("1_Expire", localDateTime);


        return folder_model_redis ;
    }
    public void Delete_Expired_Data(LocalDateTime localDateTime){
        redisTemplate.opsForHash().delete("1_Expire" ,  localDateTime) ;
    }




}
