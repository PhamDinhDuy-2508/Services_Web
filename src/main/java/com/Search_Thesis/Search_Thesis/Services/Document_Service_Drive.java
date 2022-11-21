package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.resposity.Document_Repository;
import com.google.api.services.drive.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class Document_Service_Drive {

    @Autowired
    Drive_Service drive_service ;

    @Autowired
    Document_Repository document_repository ;

    @Value("${drive.root_folder.id}")
    private String root_id ;


    public List<Category_document> get_all_Category(String root_name) throws GeneralSecurityException, IOException {
        List<File> fileList =  drive_service.listFolderContent(root_id)  ;
        String id = "" ;
        for(File file : fileList) {
             if(file.getName().equals(root_name)) {
                id =  file.getId() ;
                break;
             }
        }
        List<File> Category_LIst =  drive_service.listFolderContent(id)  ;

        return  null ;
    }



}
