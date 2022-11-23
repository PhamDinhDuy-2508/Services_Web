package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Algorithm.Search_Document;
import com.Search_Thesis.Search_Thesis.Algorithm.Sort_Alphabet;
import com.Search_Thesis.Search_Thesis.Algorithm.Sort_Day;
import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.Root_Folder;
import com.Search_Thesis.Search_Thesis.Redis_Model.Category_Redis;
import com.Search_Thesis.Search_Thesis.Redis_Model.Category_redis_Services;
import com.Search_Thesis.Search_Thesis.resposity.Category_document_Responsitory;
import com.Search_Thesis.Search_Thesis.resposity.Document_Repository;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Respository;
import com.Search_Thesis.Search_Thesis.resposity.Root_Responsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
@Scope("prototype")

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

    @Autowired
    Search_Document search_document ;

    @Autowired
    Category_redis_Services category_redis_services ;
    private Future<Set<Category_document>> get_Category_task ;
    private ExecutorService threadpool = Executors.newCachedThreadPool();

    private  HashMap<String ,  List<Folder>> Map_category_Folder ;


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
    @Cacheable(value = "category_redis" , key = "#code")
    public List<Folder> load_folder(String code) {

        List<Folder> folder = folder_respository.findbyCode(code) ;

        return folder ;
    }
//    @Cacheable(value = "display_document" ,  key = "#ID")
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

        return document.getPath() ;
    }
    @Async
    public CompletableFuture<String> Download(String ID, String file_name , ServletContext servletContext) throws IOException {

        String file_path = pdf_Path(ID) ;
        return  CompletableFuture.completedFuture(file_path)  ;
    }
    @Async
    public CompletableFuture<String> get_name_of_Folder(int Code){

        Folder folder =  folder_respository.findByIdFolder(Code) ;
        return CompletableFuture.completedFuture(folder.getTitle()) ;

    }

    public Runnable Download_Zip(int Code , HttpServletResponse response ){

        Folder folder =  folder_respository.findByIdFolder(Code) ;
        List<Document> documents =  document_repository.findById_folder(Code) ;
        if(documents.isEmpty()){
            return null;
        }

        if(documents.isEmpty()){
            return null;
        }
        List<String> listOfFileNames  =  new ArrayList<>() ;

        for(int i = 0 ;i < documents.size() ;i++) {
            listOfFileNames.add(documents.get(i).getFile()) ;
        }
        try {
            ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());


            for (String fileName : listOfFileNames) {
                FileSystemResource resource = new FileSystemResource(fileName);

                ZipEntry zipEntry = new ZipEntry(resource.getFilename());

                zipEntry.setSize(resource.contentLength());
                zipOut.putNextEntry(zipEntry);
                StreamUtils.copy(resource.getInputStream(), zipOut);
                zipOut.closeEntry();
            }
            zipOut.finish();

            zipOut.close();
        }

        catch (Exception e) {
            System.out.println(e.getMessage());
        }



        return null;
    }
    @Async
    public CompletableFuture<List<Document> > search_document(List<Document> base ,String signal) {

        List<Document> result=new ArrayList<>() ;
        if(signal.isBlank()) {
            return  CompletableFuture.completedFuture(base) ;
        }
        search_document.setList(base);

        search_document.Search(signal);

        result = search_document.getResult() ;

        return  CompletableFuture.completedFuture(result) ;


    }

    public Set<Category_document> get_category_with_id_folder() throws ExecutionException, InterruptedException {

            Set<Category_document> category_documentList = this.get_Category_task.get();

            return category_documentList ;
    }

    public void get_folder_with_userId(String ID){
        List<Folder> folders =  folder_respository.findByContributor_ID(Integer.parseInt(ID)) ;
        this.Map_category_Folder =  new HashMap<>() ;
        Callable callable = ()->{
            Set<Category_document> list =  new HashSet<>() ;
            for(int i = 0; i < folders.size() ; i++) {
                list.add(folders.get(i).getCategorydocument()) ;
            }
            return  list ;
        } ;
        for(Folder i  : folders) {
            if(this.Map_category_Folder.get(i.getCategorydocument().getCode()) == null) {
                List<Folder> folderList =  new ArrayList<>() ;
                folderList.add(i) ;
                this.Map_category_Folder.put(i.getCategorydocument().getCode() , folderList ) ;
            }
            else {
                this.Map_category_Folder.get(i.getCategorydocument().getCode()).add(i) ;
            }
        }
        this.get_Category_task = threadpool.submit(callable) ;

    }

    public HashMap<String, List<Folder>> getMap_category_Folder() {
        return Map_category_Folder;
    }

    public void setMap_category_Folder(HashMap<String, List<Folder>> map_category_Folder) {
        Map_category_Folder = map_category_Folder;
    }
//    @Cacheable(value = "pagination" ,key = "{#code ,  #page} ")
    public List<Folder> pagination(String code , int page  , List<Folder> folderList) {
        List<Folder> result = new ArrayList<>() ;
        if(page*5 > folderList.size()) {
            if(page == 1) {

                for(int i = 0; i < folderList.size() ; i++) {
                    result.add(folderList.get(i)) ;
                }
            }
            else {

                for(int i = (page-1)*5; i <  folderList.size()  ; i++) {
                    System.out.println(folderList.get(i).getTitle());
                    result.add(folderList.get(i)) ;
                }
            }
        }
        else {
            for(int i = (page-1)*5; i <(page)*5 ; i++) {
                System.out.println(i);
                result.add(folderList.get(i)) ;
            }
        }
        return  result ;
    }
    @Cacheable(value = "Filter" , key = "{#signal  , #code}")
    public List<Folder> Filter(String code ,  String signal) {
        List<Folder> folderList = new ArrayList<>();

        Category_Redis category_redis = category_redis_services.find("Category", code);

        folderList = category_redis.getFolderList();
        try {

            switch (signal) {
                case "AZ": {
                    System.out.println("AZ");

                    Sort_Alphabet sort_alphabet = new Sort_Alphabet();

                    sort_alphabet.Filter(folderList);

                    return(sort_alphabet.get_Result());
                }
                case "ZA" :{
                    System.out.println("ZA");
                    List<Folder> folderList1 = new ArrayList<>();

                    Sort_Alphabet sort_alphabet = new Sort_Alphabet();

                    sort_alphabet.Reverse(folderList);

                    return (sort_alphabet.get_Result());
                }
                case ("Date"): {
                    System.out.println("Date");
                    List<Folder> folderList1 = new ArrayList<>();

                    Sort_Day sort_alphabet = new Sort_Day();

                    sort_alphabet.Filter(folderList);

                    return (sort_alphabet.get_Result());
                }
                default: {
                    return (null);
                }
            }

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return (folderList);
        }
    }

}
