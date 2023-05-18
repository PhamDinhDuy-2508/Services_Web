package com.Search_Thesis.Search_Thesis.Services.BackupService;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.Folder_model_redis;
import com.Search_Thesis.Search_Thesis.Services.RedisService.RedisServiceImpl.Document_info_redis_Services;
import com.Search_Thesis.Search_Thesis.Services.RedisService.RedisServiceImpl.Folder_info_Services;
import com.Search_Thesis.Search_Thesis.repository.Document_Repository;
import com.Search_Thesis.Search_Thesis.repository.Folder_Respository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateHistoryService {


    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    Document_info_redis_Services document_info_redis_services;

    @Autowired
    Folder_info_Services folder_info_services;

    @Autowired
    Folder_Respository folder_respository;

    @Autowired
    Document_Repository document_repository;
    String userId ;
    String Id ;

    public UpdateHistoryService() {
    }

    public UpdateHistoryService(String userId, String id) {
        this.userId = userId;
        Id = id;
    }
    public  void Insert_Folder_into_database(Folder_Respository folder_respository, Folder_model_redis folder_model_redis, String user_id) {
            Folder folder = new Folder();

            folder.setTitle(folder_model_redis.getTitle());
            folder.setIdFolder(folder_model_redis.getIdFolder());
            folder.setPublish_date(folder_model_redis.getPublish_date());
            folder.setContributor_ID(user_id);
            folder.setCategorydocument(folder_model_redis.getCategorydocument());

            try {
                folder_respository.save(folder);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

    }

    public  List<Document> Insert_document_into_database(Folder_Respository folder_respository, Document_Repository document_repository , Folder_model_redis folder_model_redis) {
        String Title = folder_model_redis.getTitle() ;
        String Code =  folder_model_redis.getCategorydocument().getCode() ;

        Folder folder =  folder_respository.findByTitleAndCode(  Code , Title) ;
        int id =  folder.getIdFolder() ;
        List<Document> documentList = folder_model_redis.getDocumentList();

        if(!documentList.isEmpty()) {
            try {
                for (Document document : documentList) {
                    document.setId_folder(id);
                    document.setID(0);
                    document_repository.save(document);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return documentList ;
    }

}


