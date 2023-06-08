package com.Search_Thesis.Search_Thesis.Services.BackupService.BackupImpl;

import com.Search_Thesis.Search_Thesis.Model.FolderRedisModel;
import com.Search_Thesis.Search_Thesis.Services.BackupService.Backup;
import com.Search_Thesis.Search_Thesis.Services.BackupService.UpdateHistoryService;
import com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService.RedisServiceImpl.Folder_info_Services;
import com.Search_Thesis.Search_Thesis.repository.Document_Repository;
import com.Search_Thesis.Search_Thesis.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BackupFolder implements Backup {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    Folder_info_Services folder_info_services;

    @Autowired
    FolderRepository folder_respository;

    @Autowired
    Document_Repository document_repository;


    @Autowired
    UpdateHistoryService updateHistoryService;

    @Override
    public void BackUpToDatabase(String user_id, String ID) {
        Hashtable hashtable2 = (Hashtable) redisTemplate.opsForHash().get("contributor_history", user_id);
        if (hashtable2 == null) {
            return;
        }
        Stack<FolderRedisModel> stack_process = new Stack<>();
        List<FolderRedisModel> folder_model_redis = (List<FolderRedisModel>) hashtable2.get("folder");

        for (FolderRedisModel folder_model_rediss : folder_model_redis) {

            int ID_Folder = Integer.parseInt(ID);

            if (folder_model_rediss.getIdFolder() == ID_Folder) {

                stack_process.add(folder_model_rediss);
                break;
            }
        }
        FolderRedisModel folder_model_redis1 = stack_process.peek();


        updateHistoryService.Insert_Folder_into_database(folder_respository, folder_model_redis1, user_id);
        updateHistoryService.Insert_document_into_database(folder_respository, document_repository, folder_model_redis1);
//
//
        String id_folder = String.valueOf(folder_model_redis1.getIdFolder());
        Update_HistoryFolder_Redis(user_id, "folder", id_folder);
    }
    public void Update_HistoryFolder_Redis(String user_id, String type_of_file, String id_folder) {

        Hashtable hashtable1 = (Hashtable) redisTemplate.opsForHash().get("contributor_history", user_id);
        List<FolderRedisModel> list = (List<FolderRedisModel>) hashtable1.get(type_of_file);

        List<FolderRedisModel> list_old = new ArrayList<>();
        list.parallelStream().forEach(folder_model_redis -> {
            list_old.add(folder_model_redis);
        });

        for (FolderRedisModel folder_model_redis : list) {
            if (folder_model_redis.getIdFolder() == Integer.valueOf(id_folder)) {
                list.remove(folder_model_redis);
                hashtable1.remove(type_of_file, folder_model_redis);
                break;
            }
        }
        redisTemplate.opsForHash().put("contributor_history", user_id, hashtable1);
        Set<String> list1 = (Set<String>) redisTemplate.opsForHash().keys("Delete_folder");
        for (String x : list1) {

            FolderRedisModel folder_model_redis = (FolderRedisModel) redisTemplate.opsForHash().get("Delete_folder", x);
            if (folder_model_redis.getIdFolder() == Integer.valueOf(id_folder)) {
                redisTemplate.opsForHash().delete("Delete_folder", type_of_file, x);
            }

        }
    }

}
