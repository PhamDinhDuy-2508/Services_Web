package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.Document_info_redis;
import com.Search_Thesis.Search_Thesis.Services.Redis.RedisServiceImpl.Document_info_redis_Services;
import com.Search_Thesis.Search_Thesis.Services.Redis.RedisServiceImpl.Folder_info_Services;
import com.Search_Thesis.Search_Thesis.Model.Folder_model_redis;
import com.Search_Thesis.Search_Thesis.Rest.Contributor_History;
import com.Search_Thesis.Search_Thesis.repository.Document_Repository;
import com.Search_Thesis.Search_Thesis.repository.Folder_Respository;
import org.springframework.beans.factory.annotation.Autowired;
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


    public List<Document_info_redis> Get_History_Document(String ID) {
        System.out.println( "DeleteDocument " + redisTemplate.opsForHash().keys("Delete_document").stream().toList());

        List<String> key = redisTemplate.opsForHash().keys("Delete_document").stream().toList();

        List<Document_info_redis> document_info_redisList =  new ArrayList<>() ;

        for (String ignored : key) {

            if (Arrays.stream(ignored.split("_")).toList().get(0).equals(ID)) {

                Document_info_redis document_info_redis = new Document_info_redis();

                document_info_redis = document_info_redis_services.findProductById(ignored, 0);

                this.hashtable.put("document", document_info_redis.getDocument());

                document_info_redisList.add(document_info_redis) ;

            }


        }
        return  document_info_redisList ;

    }

    public List<Folder_model_redis> Get_History_Folder(String ID) {

        List<String> key = redisTemplate.opsForHash().keys("Delete_folder").stream().toList();


        List<Folder_model_redis> folderList = new ArrayList<>();

        for (String ignored : key) {

            if (Arrays.stream(ignored.split("_")).toList().get(0).equals(ID)) {

                Folder_model_redis document_info_redis = new Folder_model_redis();

                document_info_redis = folder_info_services.findProductById(ignored, 0);
                System.out.println(folder_info_services.findProductById(ignored, 0));
                folderList.add(document_info_redis);
            }
        }
        return folderList ;

    }
    public Hashtable getHashtable() {

        return hashtable;
    }
    public void Set_Contributor_History(String ID , List<Document_info_redis> History_document ,  List<Folder_model_redis> History_Folder) {
        Hashtable hashtable1 =  new Hashtable<>() ;

        hashtable1.put("document" , History_document) ;
        hashtable1.put("folder" , History_Folder) ;

        Contributor_History contributor_history =  new Contributor_History() ;
        contributor_history.setID(Integer.parseInt(ID));
        contributor_history.setHashtable(hashtable);

        redisTemplate.opsForHash().put("contributor_history" , ID ,  hashtable1 );

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
        System.out.println(user_id);
        Hashtable hashtable1 = (Hashtable) redisTemplate.opsForHash().get("contributor_history" , "1");
        if (Type_of_data.equals("document")) {
//            hashtable1 =  contributor_history.getHashtable() ;
            List<Document_info_redis>  document_info_redis = (List<Document_info_redis>) hashtable1.get("document");

            Document_info_redis document_info_redis1 =  document_info_redis.get(0) ;

            List<Document> list = document_info_redis1.getDocument() ;

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

        } else {
            Hashtable hashtable2 = (Hashtable) redisTemplate.opsForHash().get("contributor_history", user_id);
            if (hashtable2 == null) {
                return;
            }
            Stack<Folder_model_redis> stack_process = new Stack<>();
            List<Folder_model_redis> folder_model_redis = (List<Folder_model_redis>) hashtable2.get("folder");

            for (Folder_model_redis folder_model_rediss : folder_model_redis) {

                int ID_Folder = Integer.parseInt(ID);
                System.out.println(ID_Folder);

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
                List<Document> documents =  Backup.Insert_document_into_database(  folder_respository , document_repository,folder_model_redis1);
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

        public static List<Document> Insert_document_into_database(Folder_Respository folder_respository,Document_Repository document_repository ,Folder_model_redis folder_model_redis) {
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


