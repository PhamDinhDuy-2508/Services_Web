package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Redis_Model.Document_info_redis;
import com.Search_Thesis.Search_Thesis.Redis_Model.Document_info_redis_Services;
import com.Search_Thesis.Search_Thesis.Redis_Model.Folder_info_Services;
import com.Search_Thesis.Search_Thesis.Redis_Model.Folder_model_redis;
import com.Search_Thesis.Search_Thesis.Rest.Contributor_History;
import com.Search_Thesis.Search_Thesis.resposity.Document_Repository;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Respository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

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


    private Hashtable hashtable = new Hashtable<>();


    public void Get_History_Document(String ID) {
        List<String> key = redisTemplate.opsForHash().keys("Delete_document").stream().toList();
        System.out.println(key);

        List<Document_info_redis> documentList = new ArrayList<>();

        List<String> name_of_document = new ArrayList<>();


        for (String ignored : key) {

            if (Arrays.stream(ignored.split("_")).toList().get(0).equals(ID)) {

                Document_info_redis document_info_redis = new Document_info_redis();

                document_info_redis = document_info_redis_services.findProductById(ignored, 0);

                this.hashtable.put("document", document_info_redis.getDocument());


            }
        }
        System.out.println(this.hashtable);
        ;
    }

    public void Get_History_Folder(String ID) {

        List<String> key = redisTemplate.opsForHash().keys("Delete_folder").stream().toList();


        List<Folder_model_redis> folderList = new ArrayList<>();

        for (String ignored : key) {

            if (Arrays.stream(ignored.split("_")).toList().get(0).equals(ID)) {

                Folder_model_redis document_info_redis = new Folder_model_redis();

                document_info_redis = folder_info_services.findProductById(ignored, 0);
                System.out.println(folder_info_services.findProductById(ignored, 0));
                folderList.add(document_info_redis);

                this.hashtable.put("folder", folderList);

            }
        }
    }

    @Cacheable(value = "contributor_history", key = "#User_Id")
    public Contributor_History contributor_history(String User_Id, Hashtable hashtable) {
        Contributor_History contributor_history = new Contributor_History();

        if (!check_Data_Existed_In_redis(User_Id)) {

            redisTemplate.opsForHash().put("contributor_history", User_Id, hashtable);
        } else {
            redisTemplate.opsForHash().put("contributor_history", User_Id, hashtable);
            Hashtable hashtable1 = (Hashtable) redisTemplate.opsForHash().get("contributor_history", User_Id);
            contributor_history.setID(Integer.parseInt(User_Id));
            contributor_history.setHashtable(hashtable1);
        }

        return contributor_history;
    }
    public Hashtable getHashtable() {

        return hashtable;
    }

    public boolean check_Data_Existed_In_redis(String userID) {
        if (redisTemplate.opsForHash().get("contributor_history", userID) == null) {
            System.out.println("None");
            return false;
        } else {
            return true;
        }
    }

    public void setHashtable(Hashtable hashtable) {
        this.hashtable = hashtable;
    }

    @Async
    public void Backup_to_Databases(String user_id, String Type_of_data, String ID) throws InterruptedException {
        Hashtable hashtable1 = (Hashtable) redisTemplate.opsForHash().get("contributor_history", user_id);
        System.out.println(redisTemplate.opsForHash().get("contributor_history", user_id));
        if (Type_of_data.equals("document")) {
            List<Document> list = (List<Document>) hashtable1.get(Type_of_data);
            Thread thread =  new Thread(()->{
                Update_DocumentHistory_Redis(user_id, "document", ID);
            }) ;
            thread.start();

            for (Document document : list) {
                if (document.getID() == Integer.valueOf(ID)) {
                    System.out.println("True");
                    document_repository.save(document);
                }
            }
            thread.join();

//           Update_History_Redis(user_id ,  "document" , ID);
        } else {
            Hashtable hashtable2 = (Hashtable) redisTemplate.opsForHash().get("contributor_history", user_id);
            if (hashtable2 == null) {
                return;
            }
            Stack<Folder_model_redis> stack_process = new Stack<>();
            List<Folder_model_redis> folder_model_redis = (List<Folder_model_redis>) hashtable2.get("folder");

            for (Folder_model_redis folder_model_rediss : folder_model_redis) {

                int ID_Folder = Integer.parseInt(ID);

                if (folder_model_rediss.getIdFolder() == ID_Folder) {

                    stack_process.add(folder_model_rediss);
                    break;
                }
            }
            Folder_model_redis folder_model_redis1 = stack_process.peek();

            List<Document> documentList = new ArrayList<>();

            Thread thread2 = new Thread();
            Thread thread = new Thread();

            synchronized (thread) {
                Backup.Insert_Folder_into_database(folder_respository, folder_model_redis1, user_id);
            }
            synchronized (thread2) {
                int id_folder = folder_model_redis1.getIdFolder();
                Backup.Insert_document_into_database(document_repository, String.valueOf(id_folder));
            }
            thread.start();
            thread2.start();
            String id_folder = String.valueOf(folder_model_redis1.getIdFolder());
            Update_HistoryFolder_Redis(user_id, "folder", id_folder);

        }

    }

    public void Update_HistoryFolder_Redis(String user_id, String type_of_file, String id_folder) {

        Hashtable hashtable1 = (Hashtable) redisTemplate.opsForHash().get("contributor_history", user_id);
        List<Folder_model_redis> list = (List<Folder_model_redis>) hashtable1.get(type_of_file);

        List<Folder_model_redis> list_old = new ArrayList<>();
        list.parallelStream().forEach(folder_model_redis -> {
            list_old.add(folder_model_redis);
        });

        for (Folder_model_redis folder_model_redis : list) {
            if (folder_model_redis.getIdFolder() == Integer.valueOf(id_folder)) {
                list.remove(folder_model_redis);
                hashtable1.remove(type_of_file, folder_model_redis);
                break;
            }
        }
        redisTemplate.opsForHash().put("contributor_history", user_id, hashtable1);
        Set<String> list1 = (Set<String>) redisTemplate.opsForHash().keys("Delete_folder");
        for (String x : list1) {

            Folder_model_redis folder_model_redis = (Folder_model_redis) redisTemplate.opsForHash().get("Delete_folder", x);
            if (folder_model_redis.getIdFolder() == Integer.valueOf(id_folder)) {
                redisTemplate.opsForHash().delete("Delete_folder", type_of_file, x);
            }

        }
    }

    public void Update_DocumentHistory_Redis(String user_id, String type_of_file, String ID) {

        Hashtable hashtable1 = (Hashtable) redisTemplate.opsForHash().get("contributor_history", user_id);
        List<Document> list = (List<Document>) hashtable1.get(type_of_file);

        List<Document> list_old = new ArrayList<>();
        list.parallelStream().forEach(folder_model_redis -> {
            list_old.add(folder_model_redis);
        });
        for (Document folder_model_redis : list) {
            if (folder_model_redis.getID() == Integer.valueOf(ID)) {
                list.remove(folder_model_redis);
                hashtable1.remove(type_of_file, folder_model_redis);
                break;
            }
        }
        redisTemplate.opsForHash().put("contributor_history", user_id, hashtable1);
        Set<String> list1 = (Set<String>) redisTemplate.opsForHash().keys("Delete_document");
        for (String x : list1) {
            Document_info_redis folder_model_redis = (Document_info_redis) redisTemplate.opsForHash().get("Delete_document", x);
//            System.out.println(folder_model_redis.getDocument().get(0) + ID);
            if (folder_model_redis.getDocument().get(0).getID() == Integer.valueOf( ID) ) {
                redisTemplate.opsForHash().delete("Delete_document", x);
            }
        }

    }
}


    class Backup {

        public static void Insert_Folder_into_database(Folder_Respository folder_respository, Folder_model_redis folder_model_redis, String user_id) {

            Folder folder = new Folder();

            folder.setTitle(folder_model_redis.getTitle());
            folder.setIdFolder(folder_model_redis.getIdFolder());
            folder.setPublish_date(folder_model_redis.getPublish_date());
            folder.setContributor_ID(Integer.parseInt(user_id));
            folder.setCategorydocument(folder_model_redis.getCategorydocument());

            try {
                folder_respository.save(folder);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        public static void Insert_document_into_database(Document_Repository document_repository, String id_folder) {

            List<Document> documents = document_repository.findById_folder(Integer.parseInt(id_folder));
            try {
                for (Document document : documents) {
                    document_repository.save(document);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

    }


