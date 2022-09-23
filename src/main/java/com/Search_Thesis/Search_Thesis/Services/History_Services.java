package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Redis_Model.Document_info_redis;
import com.Search_Thesis.Search_Thesis.Redis_Model.Document_info_redis_Services;
import com.Search_Thesis.Search_Thesis.Redis_Model.Folder_info_Services;
import com.Search_Thesis.Search_Thesis.Redis_Model.Folder_model_redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class History_Services {

    @Autowired
    RedisTemplate redisTemplate ;
    @Autowired
    Document_info_redis_Services document_info_redis_services ;

    @Autowired
    Folder_info_Services folder_info_services ;


    public void  Get_All_Edit_Service(String ID) {
        List<String> key = redisTemplate.opsForHash().keys("Delete_document").stream().toList();
        ;
        List<Document_info_redis> documentList = new ArrayList<>();

        for (String ignored : key) {

            if (Arrays.stream(ignored.split("_")).toList().get(0).equals(ID)) {

                Document_info_redis document_info_redis = new Document_info_redis();
                document_info_redis = document_info_redis_services.findProductById(ignored , 0) ;
                documentList.add(document_info_redis) ;
            }
        }
        return  ;
    }
    public   List<Document_info_redis>Get_History_Document(String ID) {
        List<String> key = redisTemplate.opsForHash().keys("Delete_document").stream().toList();

        List<Document_info_redis> documentList = new ArrayList<>();

        for (String ignored : key) {

            if (Arrays.stream(ignored.split("_")).toList().get(0).equals(ID)) {

                Document_info_redis document_info_redis = new Document_info_redis();

                document_info_redis = document_info_redis_services.findProductById(ignored , 0) ;

                documentList.add(document_info_redis) ;

            }
        }
        return documentList ;
    }
    public   List<Folder_model_redis>Get_History_Folder(String ID) {
        List<String> key = redisTemplate.opsForHash().keys("Delete_folder").stream().toList();

        List<Folder_model_redis> folderList = new ArrayList<>();

        for (String ignored : key) {

            if (Arrays.stream(ignored.split("_")).toList().get(0).equals(ID)) {

                Folder_model_redis document_info_redis = new Folder_model_redis();

                document_info_redis =folder_info_services .findProductById(ignored , 0) ;

                folderList.add(document_info_redis) ;

            }
        }
        return folderList ;
    }

}