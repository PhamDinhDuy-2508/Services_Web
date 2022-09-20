package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.Root_Folder;
import com.Search_Thesis.Search_Thesis.Redis_Model.Folder_model_redis;
import com.Search_Thesis.Search_Thesis.resposity.Document_Repository;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Reids_respository;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Respository;
import com.Search_Thesis.Search_Thesis.resposity.Root_Responsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class Edit_Document_Services {
    @Autowired
    Folder folder ;
    @Autowired
    Folder_Respository folder_respository ;

    @Autowired
    Folder_Reids_respository folder_reids_respository ;

    @Autowired
    Document_Repository document_repository ;

    @Autowired
    Root_Responsitory root_responsitory ;

    private Socket socket ;


    @Async
    public CompletableFuture<Boolean> update(int ID , String name ){
        try {

            folder = folder_respository.findByIdFolder(ID);
            folder.setTitle(name);
            folder_respository.save(folder) ;
            Update_folder_in_Server(folder ,   name)  ;

            return CompletableFuture.completedFuture(true) ;

        }
        catch (Exception e){
            return CompletableFuture.completedFuture(false) ;
        }
    }
    @Async
    public CompletableFuture<Boolean> delete_folder(  int ID){
        String name ;
        List<Document> documentList =  new ArrayList<>() ;

        ExecutorService threadpool = Executors.newCachedThreadPool();


        try {
            Callable callable = ()-> {
              return  document_repository.findById_folder(ID) ;
            } ;

            Future<List<Document>> get_document_task = threadpool.submit(callable) ;

            folder = folder_respository.findByIdFolder(ID);
            name =  folder.getTitle() ;

            Root_Folder root_folder = root_responsitory.findRoot_FolderByIdById(folder.getCategorydocument() .getRoot_folder().getId()) ;

            String Category = folder.getCategorydocument() .getCode() ;
            String root_name =root_folder.getName();
            String Folder = folder.getTitle();
            String url =  "D:\\Data\\Document_data\\"+root_name+"\\" + Category+"\\" +Folder;


            List<Document> documents =  get_document_task.get() ;


            save_to_folder_redis(folder  , documents ,  url ) ;



            folder_respository.deleteByIdFolder(ID);


            return CompletableFuture.completedFuture(true) ;
        }
        catch (Exception e){
            System.out.println(e.getMessage()+"asdasd");
            return CompletableFuture.completedFuture(false) ;
        }
    }
    public void Delete_folder_in_Server(String root_name ,  String Category ,  String Folder ) {

        String url =  "D:\\Data\\Document_data\\"+root_name+"\\" + Category+"\\" +  Folder;
        File file =  new File(url) ;
        file.delete() ;

    }
    public boolean save_to_folder_redis(Folder folder ,  List<Document> documentList , String url) {
        try {


            String HASHKEY =(folder.getContributor_ID()) + "_" + String.valueOf(folder.getIdFolder());
            System.out.println(HASHKEY);

            Folder_model_redis folder_model_redis = new Folder_model_redis();

            folder_model_redis.setIdFolder(folder_model_redis.getIdFolder());
            folder_model_redis.setDocumentList(documentList);
            folder_model_redis.setCategorydocument(folder_model_redis.getCategorydocument());
            folder_model_redis.setCategorydocument(folder_model_redis.getCategorydocument());
            folder_model_redis.setPublish_date(folder.getPublish_date());

            folder_model_redis.setTitle(folder_model_redis.getTitle());

            folder_model_redis.setUrl(url);

            folder_reids_respository.save(HASHKEY, folder_model_redis);
            return  true ;
        }
        catch (Exception e) {
            return  false  ;
        }
    }
    public Boolean Update_folder_in_Server(  Folder folder , String newName) {
        String root_name = folder.getCategorydocument().getRoot_folder().getName() ;
        String Category = folder.getCategorydocument().getCode()  ;
        String Folder = folder.getTitle() ;


        String url =  "D:\\Data\\Document_data\\"+root_name+"\\" + Category+"\\" +  Folder;
        String rename = "D:\\Data\\Document_data\\"+root_name+"\\" + Category+"\\" +  newName;

        File file  =  new File(url ) ;
        File file_des =  new File(rename) ;

        if (file.renameTo(file_des)) {
            return true;
        }
        else {
            return false  ;
        }
    }
    @Async
    public void Add_Document_in_Server(String name , String signal) {
        switch (signal) {
            case "Delete"  : {

            }
            case  "Add" : {
            }
            default:{
            }
            }
        }

    public void Add_Document_in_Server(String name) {

    }
    public void Delete_Document_in_Server(String name) {

    }

    public List<Document> load_document_in_Folder(int id) {
        return document_repository.findById_folder(id) ;
    }

    public Boolean deleteDocument_inFolder(int idFolder) {
        return null ;
    }

}
