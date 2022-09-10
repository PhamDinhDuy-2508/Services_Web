package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.Root_Folder;
import com.Search_Thesis.Search_Thesis.resposity.Category_document_Responsitory;
import com.Search_Thesis.Search_Thesis.resposity.Document_Repository;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Respository;
import com.Search_Thesis.Search_Thesis.resposity.Root_Responsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class Document_services_2 {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher ;

    @Autowired
    Document_services document_services ;

    @Autowired
    Root_Folder root_folder ;

    @Autowired
    Session_Service session_service ;
    @Autowired
    Root_Responsitory root_responsitory ;

    @Autowired
    Category_document_Responsitory category_document_responsitory ;

    @Autowired

    Folder_Respository folder_respository ;

    @Autowired
    Document_Repository document_repository ;

    @Autowired

    Dowload_File_Utils dowload_file_utils ;


    HashMap<String ,  List<Category_document>> Hash_category_documentList =  new HashMap<>() ;


    public List<Root_Folder> load_root(){
        return root_responsitory.findAll() ;
    }

    public  List<List<Category_document>  > load_category() {

        List<Root_Folder> root_folders =  load_root() ;
        Hash_category_documentList =  new HashMap<>() ;
        List<List<Category_document> >category_documentList =  new ArrayList<>() ;

        for( int i = 0  ; i < root_folders.size() ;  i++ ) {

           category_documentList .add(  category_document_responsitory.findByID(root_folders.get(i).getId())) ;

        }
        return   category_documentList ;
    }
    public List<Folder> load_folder(String code) {

        List<Folder> folder = folder_respository.findbyCode(code) ;

        return folder ;
    }
    public List<Document> load_Document(String ID) {
        try {
            System.out.println(Integer.valueOf(ID));
           return document_repository.findById_folder(Integer.valueOf(ID)) ;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return  null;
        }
    }
    public String pdf_Path( String ID_document){

        Document document =  document_repository.findByID(Integer.valueOf( ID_document) )  ;

        return document.getFile() ;
    }
    @Async
    public CompletableFuture<String> Download(String ID, String file_name , ServletContext servletContext) throws IOException {

        String file_path = pdf_Path(ID) ;


        return  CompletableFuture.completedFuture(file_path) ;

    }
}
