package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Respository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
@Service
public class Edit_Document_Services {
    @Autowired
    Folder folder ;
    @Autowired
    Folder_Respository folder_respository ;

    @Async
    public CompletableFuture<Boolean> update(int ID , String name ){
        try {

            folder = folder_respository.findByIdFolder(ID);
            folder.setTitle(name);
            folder_respository.save(folder) ;
            return CompletableFuture.completedFuture(true) ;

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return CompletableFuture.completedFuture(false) ;
        }
    }
    @Async
    public CompletableFuture<Boolean> delete(int ID){
        String name ;
        try {
            folder = folder_respository.findByIdFolder(ID);
            name =  folder.getTitle() ;
            Delete_Document_in_Server(name);
            folder_respository.delete(folder);
            return CompletableFuture.completedFuture(true) ;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return CompletableFuture.completedFuture(false) ;
        }
    }
    public void Delete_folder_in_Server(String name ) {}
    public void Update_folder_in_Server(String name) {}
    public void Update_Document_in_Server(String name) {}
    public void Add_Document_in_Server(String name) {}
    public void Delete_Document_in_Server(String name) {}





}
