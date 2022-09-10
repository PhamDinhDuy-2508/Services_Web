package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Algorithm.Search_Folder;
import com.Search_Thesis.Search_Thesis.Algorithm.Search_category;
import com.Search_Thesis.Search_Thesis.Event.Create_Category_Event;
import com.Search_Thesis.Search_Thesis.Event.Create_folder_Event;
import com.Search_Thesis.Search_Thesis.Event.Upload_document_Event;
import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.Root_Folder;
import com.Search_Thesis.Search_Thesis.resposity.Category_document_Responsitory;
import com.Search_Thesis.Search_Thesis.resposity.Document_Repository;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Respository;
import com.Search_Thesis.Search_Thesis.resposity.Root_Responsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class Document_services {

    @Autowired
    Document document ;
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

    @Autowired
    Document_Repository document_repository ;


    private  Thread Search_document ;

    private HashMap<String,Folder> hashMapFolder = new HashMap<>()   ;

    private Boolean signal ;

    private  Category_document present_Category ;

    public Boolean getSignal() {
        return signal;
    }

    public void setSignal(Boolean signal) {
        this.signal = signal;
    }

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

            Create_Category_Folder(create_category_event.getCreate_category().getRoot_name() ,
                                    create_category_event.getCreate_category().getCode() );

        }

        catch (Exception e) {
            System.out.println("something was wrong");
        }

    }

    public List<Folder> get_Folder(String  code) {

        List<Folder> list =  folder_respository.findbyCode(code) ;
        return list ;
    }
    public  Folder findByCodeAndTitle(String category , String title) {
        return folder_respository.findByTitleAndCode(category ,  title);
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

            Create_Folder_Directory(create_folder_event.getCreate_folder().getRoot_name()  ,
                                     create_folder_event.getCreate_folder().getCode() ,
                                    create_folder_event.getCreate_folder().getFolder_name());

        }

        catch (Exception e) {
            System.out.println("something was wrong");
        }

    }
    public void Create_Category_Folder(String root , String category) throws IOException {

        String category_path = "D:\\Data\\Document_data\\" + root+"\\"+ category ;

        File category_root_directory = new File(category_path) ;

        if(!category_root_directory.exists()) {
            category_root_directory.mkdir() ;

            Path path = Paths.get(category_path);

            Files.createDirectories(path);
        }
    }
    public void Create_Folder_Directory(String root , String category , String folder) throws IOException {
        Create_Category_Folder(root , category);

        String Folder_Path = "D:\\Data\\Document_data\\" + root+"\\"+ category+"\\"+folder ;
        System.out.println(Folder_Path);

        File folder_category_root_directory = new File(Folder_Path) ;

        if(!folder_category_root_directory.exists()) {

            folder_category_root_directory.mkdir() ;

        }

    }

    @Async
    public CompletableFuture<Boolean> check_Category_Existed(String code) {
        try {
            List<Category_document> categoryDocument = category_document_responsitory.findByCode(code)  ;

            if (categoryDocument == null) {
                return CompletableFuture.completedFuture(false);
            }
            this.category_document = categoryDocument.get(0);
            return CompletableFuture.completedFuture(true) ;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return CompletableFuture.completedFuture(false) ;

        }
    }
    @Async
    public CompletableFuture<Boolean> check_Folder(String Code , String Title) {
        try {
            Folder folder1 = folder_respository.findByTitleAndCode(Code , Title);
            if(folder1 == null) {
                return  CompletableFuture.completedFuture(false) ;
            }
            else {
                return  CompletableFuture.completedFuture(true) ;
            }
        }
        catch (Exception e) {
            return  CompletableFuture.completedFuture(false)  ;
        }
    }



    public String Create_File(String root , String category , String folder , String filename , MultipartFile multipartFile) throws IOException {

//        Create_Folder_Directory(root , category ,  folder) ;
        if(multipartFile.getSize()==0) {
            return null ;
        }

        String Folder_Path = "D:\\Data\\Document_data\\" + root+"\\"+ category+"\\"+folder+"\\"+filename ;


        File document_root_directory = new File(Folder_Path) ;
        if(!document_root_directory.exists()) {
            document_root_directory.getParentFile().mkdirs();

        }

        Path document_path = Paths.get(Folder_Path);
        Path fileToSavePath = Files.createFile(document_path);

        try {
            byte[] bytes = multipartFile.getBytes();

            // Creating the directory to store file
            File dir = new File(Folder_Path);
            if (!dir.exists())
                dir.mkdirs();
            else {
                dir.delete() ;
            }

            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(document_root_directory));
            stream.write(multipartFile.getBytes());
            stream.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return Folder_Path ;
    }
    @EventListener
    @Async
    public void Create_Document(Upload_document_Event upload_document_event) throws IOException {


        String root_name = upload_document_event.getCreate_folder().getRoot_name() ;

        String category_name = upload_document_event.getCreate_folder().getCode() ;

        String Folder_name =  upload_document_event.getCreate_folder().getFolder_name() ;

        Folder folder1 = findByCodeAndTitle(category_name , Folder_name) ;

        List<MultipartFile> multipartFiles =  new ArrayList<>() ;

        for(MultipartFile multipartFile  : upload_document_event.getMultipartFile()) {

            try {
                String file_path = Create_File(root_name ,  category_name , Folder_name , multipartFile.getOriginalFilename() , multipartFile);
                if(file_path != null) {
                    document = create_Document_info(multipartFile.getOriginalFilename() ,  file_path , folder1) ;
                    document_repository.save(document) ;
                }

            } catch (IOException e) {

                System.out.println(e.getMessage());

            }
        }
    }

    public Document create_Document_info(String file_name , String file_path , Folder folder) {

        Document document1 =  new Document() ;
        document1.setFile(file_path);
        document1.setTitle(file_name);

        LocalDate myObj = LocalDate.now() ;

        java.sql.Date date = java.sql.Date.valueOf(myObj.toString()) ;

        document1.setPublish_date(date);
        document1.setId_folder(folder.getIdFolder());

        return document1 ;
    }



}
