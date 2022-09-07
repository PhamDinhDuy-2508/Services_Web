package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.Root_Folder;
import com.Search_Thesis.Search_Thesis.resposity.Category_document_Responsitory;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Respository;
import com.Search_Thesis.Search_Thesis.resposity.Root_Responsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
}
