package com.Search_Thesis.Search_Thesis.Services.HistoryService;

import com.Search_Thesis.Search_Thesis.Model.Document_info_redis;
import com.Search_Thesis.Search_Thesis.Model.Folder_model_redis;
import com.Search_Thesis.Search_Thesis.Services.BackupService.BackupImpl.BackupDocument;
import com.Search_Thesis.Search_Thesis.Services.BackupService.BackupImpl.BackupFolder;
import com.Search_Thesis.Search_Thesis.Services.BackupService.BackupService;
import com.Search_Thesis.Search_Thesis.Services.RedisService.RedisServiceImpl.Document_info_redis_Services;
import com.Search_Thesis.Search_Thesis.Services.RedisService.RedisServiceImpl.Folder_info_Services;
import com.Search_Thesis.Search_Thesis.repository.Document_Repository;
import com.Search_Thesis.Search_Thesis.repository.Folder_Respository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

@Service
@Scope("prototype")

public class History_Services {
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

    @Autowired
    BackupService backupService;

    public History_Services() {}

    private Hashtable hashtable = new Hashtable<>();


    public List<Document_info_redis> Get_History_Document(String ID) {

        List<String> key = redisTemplate.opsForHash().keys("Delete_document").stream().toList();

        List<Document_info_redis> document_info_redisList = new ArrayList<>();

        for (String ignored : key) {

            if (Arrays.stream(ignored.split("_")).toList().get(0).equals(ID)) {

                Document_info_redis document_info_redis = document_info_redis_services.findProductById(ignored, 0);

                this.hashtable.put("document", document_info_redis.getDocument());

                document_info_redisList.add(document_info_redis);
            }
        }
        return document_info_redisList;

    }

    public List<Folder_model_redis> Get_History_Folder(String ID) {

        List<String> key = redisTemplate.opsForHash().keys("Delete_folder").stream().toList();


        List<Folder_model_redis> folderList = new ArrayList<>();

        for (String ignored : key) {

            String Id_fromKey = Arrays.stream(ignored.split("_")).toList().get(0);

            if (Id_fromKey.equals(ID)) {

                Folder_model_redis document_info_redis = folder_info_services.findProductById(ignored, 0);

                folderList.add(document_info_redis);
            }
        }
        return folderList;
    }

    public void Set_Contributor_History(String ID, List<Document_info_redis> History_document, List<Folder_model_redis> History_Folder) {
        Hashtable hashtable1 = new Hashtable<>();
        hashtable1.put("document", History_document);
        hashtable1.put("folder", History_Folder);
        redisTemplate.opsForHash().put("contributor_history", ID, hashtable1);
    }


    @Async
    public void Backup_to_Databases(String user_id, String Type_of_data, String ID) throws InterruptedException {
        if (Type_of_data.equals("document")) {
            backupService.setBackupProcess(new BackupDocument());
        } else {
            backupService.setBackupProcess(new BackupFolder());
        }
        backupService.Backup();

//        Hashtable hashtable1 = (Hashtable) redisTemplate.opsForHash().get("contributor_history" , "1");
//        if (Type_of_data.equals("document")) {
//
//            List<Document_info_redis>  document_info_redis = (List<Document_info_redis>) hashtable1.get("document");
//
//            Document_info_redis document_info_redis1 =  document_info_redis.get(0) ;
//
//            List<Document> list = document_info_redis1.getDocument() ;
//
//            Thread thread =  new Thread(()->{
//                Update_DocumentHistory_Redis(user_id, "document", ID);
//            }) ;
//
//            thread.start();
//
//            for (Document document : list) {
//
//                if (document.getID() == Integer.valueOf(ID)) {
//                    document_repository.save(document);
//                }
//
//            }
//
//        } else {
//            Hashtable hashtable2 = (Hashtable) redisTemplate.opsForHash().get("contributor_history", user_id);
//            if (hashtable2 == null) {
//                return;
//            }
//            Stack<Folder_model_redis> stack_process = new Stack<>();
//            List<Folder_model_redis> folder_model_redis = (List<Folder_model_redis>) hashtable2.get("folder");
//
//            for (Folder_model_redis folder_model_rediss : folder_model_redis) {
//
//                int ID_Folder = Integer.parseInt(ID);
//
//                if (folder_model_rediss.getIdFolder() == ID_Folder) {
//
//                    stack_process.add(folder_model_rediss);
//                    break;
//                }
//            }
//            Folder_model_redis folder_model_redis1 = stack_process.peek();
//
//            Backup.Insert_Folder_into_database(folder_respository, folder_model_redis1, user_id);
//            Backup.Insert_document_into_database(  folder_respository , document_repository,folder_model_redis1);
//
//
//            String id_folder = String.valueOf(folder_model_redis1.getIdFolder());
//            Update_HistoryFolder_Redis(user_id, "folder", id_folder);
//        }

    }
}



