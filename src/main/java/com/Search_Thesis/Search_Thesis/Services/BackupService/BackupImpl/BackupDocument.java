package com.Search_Thesis.Search_Thesis.Services.BackupService.BackupImpl;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Document_info_redis;
import com.Search_Thesis.Search_Thesis.Services.BackupService.Backup;
import com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService.RedisServiceImpl.Document_info_redis_Services;
import com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService.RedisServiceImpl.Folder_info_Services;
import com.Search_Thesis.Search_Thesis.repository.Document_Repository;
import com.Search_Thesis.Search_Thesis.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

@Component
public class BackupDocument implements Backup {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    Document_info_redis_Services document_info_redis_services;

    @Autowired
    Folder_info_Services folder_info_services;

    @Autowired
    FolderRepository folder_respository;

    @Autowired
    Document_Repository document_repository;


    @Override
    public void BackUpToDatabase(String user_id, String ID) {
        Hashtable hashtable1 = (Hashtable) redisTemplate.opsForHash().get("contributor_history", user_id);

        List<Document_info_redis> document_info_redis = (List<Document_info_redis>) hashtable1.get("document");

        Document_info_redis document_info_redis1 = document_info_redis.get(0);

        List<Document> list = document_info_redis1.getDocument();

        Thread thread = new Thread(() -> {
            Update_DocumentHistory_Redis(user_id, "document", ID);
        });

        thread.start();

        for (Document document : list) {

            if (document.getID() == Integer.valueOf(ID)) {
                document_repository.save(document);
            }

        }
    }
    public void Update_DocumentHistory_Redis(String user_id, String type_of_file, String ID) {

        Hashtable hashtable1 = (Hashtable) redisTemplate.opsForHash().get("contributor_history", user_id);
        List<Document_info_redis> list = (List<Document_info_redis>) hashtable1.get(type_of_file);

        List<Document> list_old = new ArrayList<>();
        list_old =   list.get(0).getDocument() ;

        for (Document folder_model_redis : list_old) {
            if (folder_model_redis.getID() == Integer.valueOf(ID)) {
                hashtable1.remove("document", list);

                list_old.remove(folder_model_redis);
                list.get(0).setDocument(list_old);
                hashtable1.remove("document", list);
                break;
            }
        }
        redisTemplate.opsForHash().put("contributor_history", user_id, hashtable1);
        Set<String> list1 = (Set<String>) redisTemplate.opsForHash().keys("Delete_document");
        for (String x : list1) {
            Document_info_redis folder_model_redis = (Document_info_redis) redisTemplate.opsForHash().get("Delete_document", x);
            if (folder_model_redis.getDocument().get(0).getID() == Integer.valueOf( ID) ) {
                redisTemplate.opsForHash().delete("Delete_document", x);
            }
        }

    }
}
