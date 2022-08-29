package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Algorithm.Search_Folder;
import com.Search_Thesis.Search_Thesis.Algorithm.Search_category;
import com.Search_Thesis.Search_Thesis.Event.Create_Category_Event;
import com.Search_Thesis.Search_Thesis.Event.Create_folder_Event;
import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.Root_Folder;
import com.Search_Thesis.Search_Thesis.resposity.Category_document_Responsitory;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Respository;
import com.Search_Thesis.Search_Thesis.resposity.Root_Responsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Future;

@Service
public class Document_services {

    @Autowired
    Folder folder ;
    @Autowired
    Root_Folder root_folder ;
    @Autowired
    Root_Responsitory root_responsitory ;

    @Autowired
    Session_Service session_service  ;
    @Autowired
    Search_category search_category ;

    @Autowired
    Search_Folder search_folder ;

    @Autowired
    Folder_Respository folder_respository ;

    @Autowired
    Category_document category_document ;


    private  Thread Search_document ;

    private HashMap<String,Folder> hashMapFolder = new HashMap<>()   ;



    @Autowired
    Category_document_Responsitory category_document_responsitory ;
    public Root_Folder load_Root_Folder(String ID) {

        return root_responsitory.findRoot_FolderByIdById(Integer.valueOf(ID));

    }
    public List<Category_document> load_category(int ID){


        List<Category_document> new_list =  new ArrayList<>() ;
        new_list = category_document_responsitory.findByID(ID) ;
        return new_list ;
    }

    public List<Category_document> Search_Category(  List<Category_document> category_documentList , String request_detail) {

        this.search_category.setList(category_documentList);
        this.search_category.Search(request_detail);

        Set<String> Key = this.search_category.getResult().keySet() ;
        Map<String , Category_document> map_cate =  this.search_category.getResult() ;
        List<Category_document> res =  new ArrayList<>() ;

        for(String x : Key) {
            res.add(map_cate.get(x)) ;
        }
        if(request_detail.isEmpty()) {
            return  category_documentList ;
        }

        return res ;
    }

    @Async

    public Future<Boolean> find_category_code(String root,String code){

       List<Category_document> category_document = category_document_responsitory.findByID(Integer.valueOf(root)) ;

       Category_document result =  new Category_document() ;
       for(Category_document x : category_document) {

           if(x.getCode().equals(code)) {
                return  new AsyncResult<Boolean>(true);
            }
       }
       return  new AsyncResult<Boolean>(false);
    }

    @EventListener
    @Async
    public  void   Create_new_Category(Create_Category_Event create_category_event) {
        try {

            root_folder.setId(Integer.valueOf(create_category_event.getCreate_category().getRoot_id()));
            root_folder.setName(create_category_event.getCreate_category().getName());

            category_document.setName(create_category_event.getCreate_category().getName());

            category_document.setCode(create_category_event.getCreate_category().getCode());

            category_document.setRoot_folder(root_folder);

            root_folder.setCategory_document(Collections.singleton(category_document));

            root_responsitory.save(root_folder) ;

        }

        catch (Exception e) {
            System.out.println("something was wrong");
        }

    }

    public List<Folder> get_Folder(String  code) {

        List<Folder> list =  folder_respository.findbyCode(code) ;
        return list ;
    }
    public List<Folder> Search_folder(List<Folder> folderList , String request) {
        if(request == "") {
            return folderList ;
        }
        search_folder.setList(folderList);
        search_folder.Search(request );
        return  search_folder.getResult() ;

    }

    @EventListener
    @Async
    public void Create_Folder(Create_folder_Event create_folder_event) {
        System.out.println(Thread.currentThread().getName());

        try {

            root_folder.setId(Integer.valueOf(create_folder_event.getCreate_folder().getRoot_id()));
            root_folder.setName(create_folder_event.getCreate_folder().getName());

            category_document.setName(create_folder_event.getCreate_folder().getName());
            category_document.setCode(create_folder_event.getCreate_folder().getCode());

            folder.setTitle(create_folder_event.getCreate_folder().getFolder_name());
            LocalDate myObj = LocalDate.now() ;
            java.sql.Date date = java.sql.Date.valueOf(myObj.toString()) ;
            folder.setPublish_date(date);
            category_document.setNewfolder(Collections.singleton(folder));

            folder.setCategorydocument(category_document);

            category_document.setRoot_folder(root_folder);




            root_folder.setCategory_document(Collections.singleton(category_document));


            root_responsitory.save(root_folder) ;





        }

        catch (Exception e) {
            System.out.println("something was wrong");
        }

    }


}
