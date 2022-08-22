package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Model.Root_Folder;
import com.Search_Thesis.Search_Thesis.resposity.Root_Responsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Document_services {

    @Autowired
    Root_Folder root_folder ;

    @Autowired
    Root_Responsitory root_responsitory ;


    public Root_Folder load_Root_Folder(String ID) {


        Root_Folder root_folder1 = (Root_Folder) root_responsitory.findRoot_FolderByIdById(Integer.valueOf(ID));

        return root_folder1 ;

    }
    public List<Category_document> load_category(Root_Folder root_folder){


        List<Category_document> new_list =  new ArrayList<>() ;

//        new_list =  categoryDocument_responsitory.findAllByRoot_folder(root_folder) ;

        return new_list ;

    }



}
