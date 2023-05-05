package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Algorithm.Search_Folder;
import com.Search_Thesis.Search_Thesis.Algorithm.Search_category;
import com.Search_Thesis.Search_Thesis.Config.Drive_Config;
import com.Search_Thesis.Search_Thesis.Event.Create_Category_Event;
import com.Search_Thesis.Search_Thesis.Event.Create_folder_Event;
import com.Search_Thesis.Search_Thesis.Event.Upload_document_Event;
import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.Root_Folder;
import com.Search_Thesis.Search_Thesis.Services.SessionService.SessionService;
import com.Search_Thesis.Search_Thesis.repository.Category_document_Responsitory;
import com.Search_Thesis.Search_Thesis.repository.Document_Repository;
import com.Search_Thesis.Search_Thesis.repository.Folder_Respository;
import com.Search_Thesis.Search_Thesis.repository.Root_Responsitory;
import com.google.api.services.drive.Drive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;

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
    @Qualifier("SessionService")
    SessionService session_serviceImpl;
    @Autowired
    Search_category search_category ;

    @Autowired
   Search_Folder search_folder ;
    @Autowired
    RedisTemplate redisTemplate ;

    @Autowired
    Folder_Respository folder_respository ;

    @Autowired
    Category_document category_document ;

    @Autowired
    Document_Repository document_repository ;

    @Autowired
    Drive_Service drive_service ;

    @Value("${drive.root_folder.id}")
    private String parent_Id ;

    ExecutorService threadpool = Executors.newCachedThreadPool();



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

    @Autowired
    CacheManager cacheManager ;
    public Root_Folder load_Root_Folder(String ID) {

        return root_responsitory.findRoot_FolderByIdById(Integer.valueOf(ID));

    }
    public List<Category_document> load_category(int ID){


        List<Category_document> new_list =  new ArrayList<>() ;
        new_list = category_document_responsitory.findByID(ID) ;
        System.out.println(new_list);
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
        System.out.println(this.parent_Id);
        try {

            root_folder.setId(Integer.valueOf(create_category_event.getCreate_category().getRoot_id()));
            root_folder.setName(create_category_event.getCreate_category().getName());

            category_document.setName(create_category_event.getCreate_category().getName());

            category_document.setCode(create_category_event.getCreate_category().getCode());

            category_document.setRoot_folder(root_folder);

            root_folder.setCategory_document(Collections.singleton(category_document));

            root_responsitory.save(root_folder) ;
            String id = "" ;
            for(com.google.api.services.drive.model.File file :drive_service.listFolderContent(parent_Id)) {
                System.out.println(file.getName());
                if(file.getName().equals(create_category_event.getCreate_category().getRoot_name())) {
                    id =  file.getId() ;
                    break;
                }
            }
            drive_service.create_Folder_ID(id ,create_category_event.getCreate_category().getCode()  ) ;
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


        try {
            String root_name= create_folder_event.getCreate_folder().getRoot_name() ;
            String category_name =  create_folder_event.getCreate_folder().getCode() ;
            String folder_name =  create_folder_event.getCreate_folder().getFolder_name() ;
            Category_document categoryDocument =  category_document_responsitory.findByCode(category_name) ;
            System.out.println(categoryDocument);


            folder.setTitle(create_folder_event.getCreate_folder().getFolder_name());
            LocalDate myObj = LocalDate.now() ;
            java.sql.Date date = java.sql.Date.valueOf(myObj.toString()) ;
            folder.setPublish_date(date);
            category_document.setNewfolder(Collections.singleton(folder));
            folder.setCategorydocument(categoryDocument);

//            folder.setCategorydocument(category_document);
            folder.setContributor_ID(Integer.valueOf( create_folder_event.getCreate_folder().getUser_id()));

//
            folder_respository.save(folder) ;

            String filePath = "Data_Document/"+root_name+"/"+category_name;

            String id =  drive_service.getFolderId(filePath) ;
            Drive driveInstance = Drive_Config.getInstance();

            drive_service.findOrCreateFolder(id ,  folder_name , driveInstance);
            cacheManager.getCache("category_redis").evict(category_name);

        }

        catch (Exception e) {
            System.out.println(e.getMessage());
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
            Category_document categoryDocument = category_document_responsitory.findByCode(code)  ;

            if (categoryDocument == null) {
                return CompletableFuture.completedFuture(false);
            }
            this.category_document = categoryDocument;
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
            Folder folder1 =  folder_respository.findByTitleAndCode(Code , Title);
            System.out.println(folder1.getIdFolder());

            if(folder1 == null) {
                return  CompletableFuture.completedFuture(false) ;
            }
            else {
                return  CompletableFuture.completedFuture(true) ;
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());

            return  CompletableFuture.completedFuture(false)  ;
        }
    }
    public void Delete_Document_Cache(String ID){
        redisTemplate.delete("display_document::"+ID) ;
    }



    public synchronized String Create_File(String root , String category , String folder , String filename , MultipartFile multipartFile) throws IOException {
        String Folder_Path = "D:\\Data\\Document_data\\" + root+"\\"+ category+"\\"+folder+"\\"+filename ;


//        Create_Folder_Directory(root , category ,  folder) ;
        if(multipartFile.getSize()==0) {
            System.out.println(Folder_Path);
            System.out.println(multipartFile.getSize());
            return null ;
        }



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
            System.out.println("Folder " + Folder_Path);
            System.out.println("Error" + e.getMessage());
            return Folder_Path;
        }
        System.out.println(Folder_Path);

        return Folder_Path ;
    }

    @CacheEvict(value = "category_redis" , key = "{#Code}")
    public void Delete_Cachce_Category(String Code) {

    }
    @EventListener
    @Async
    public  void Create_Document(Upload_document_Event upload_document_event) throws Exception {


        String root_name = upload_document_event.getCreate_folder().getRoot_name() ;

        String category_name = upload_document_event.getCreate_folder().getCode() ;

        String Folder_name =  upload_document_event.getCreate_folder().getFolder_name() ;

        Folder folder1 = folder_respository.findByTitleAndCode(category_name , Folder_name) ;

        List<MultipartFile> multipartFiles =  new ArrayList<>() ;

        List<Document> documentList =  new ArrayList<>() ;

        String file_PATH = "Data_Document/"+root_name+"/"+category_name+"/"+Folder_name ;

        String id = drive_service.getFolderId(file_PATH);


//        System.out.println(Arrays.toString(upload_document_event.getMultipartFile()[0].getBytes()));

///https://drive.google.com/file/d/107wEnfCHBcu2BT57-twdmI-r_H9FAArT/view?usp=share_link
        for(MultipartFile multipartFile  : upload_document_event.getMultipartFile()) {
           com.google.api.services.drive.model.File file =  drive_service.uploadFile(multipartFile , file_PATH) ;
            System.out.println(file.getParents());

//            String id_Folder = drive_service.getFolderId(file_PATH) ;
            document = create_Document_info(multipartFile.getOriginalFilename(), file.getParents().get(0), folder1 , file.getWebViewLink(),file.getId());
//            document_repository.save(document);
            documentList.add(document);
            document_repository.save(document) ;

//
//
//            Thread thread =  new Thread(()->{
//                try {
//
//                    String  file_path = Create_File(root_name ,  category_name , Folder_name , multipartFile.getOriginalFilename() , multipartFile);
//                    if(file_path != null) {
//                        document = create_Document_info(multipartFile.getOriginalFilename(), file_path, folder1);
//                        document_repository.save(document);
//                        documentList.add(document);
//                        System.out.println(file_path);
//                    }
//
//
//                } catch (IOException e) {
//                    System.out.println("Error" + e.getMessage());
//                }
//
//            }) ;
//            thread.start();
//            Future<?> futureTask = threadpool.submit(() -> {
//                String file_path = null;
//                try {
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//                System.out.println("Thread_Cjild" + Thread.currentThread().getName());
//
//                document = create_Document_info(multipartFile.getOriginalFilename() ,  file_path , folder1) ;
//                document_repository.save(document) ;
//                documentList.add(document) ;
//                return  ;
//            });
//            while(!futureTask.isDone()) {
//                wait();
//            }
//
//            continue;
//
//                    synchronized (thread){
//                        System.out.println("Thread_child" + Thread.currentThread().getName());
//                        document = create_Document_info(multipartFile.getOriginalFilename() ,  file_path , folder1) ;
//                        document_repository.save(document) ;
//                        documentList.add(document) ;
//                    }thread.start();
////            / be nha lam


        }
        Delete_Cachce_Category(category_name);
//        cacheManager.getCache("category_redis").get(category_name).get() ;
//        cacheManager.getCache("category_redis").put(category_name);


        Update_Cache_Document(documentList , String.valueOf( folder1.getIdFolder()) ) ;

    }

    public Document create_Document_info(String file_name , String file_path , Folder folder , String share,String id_drive) throws GeneralSecurityException, IOException {

        Document document1 =  new Document() ;
        document1.setFile(file_path);
        document1.setTitle(file_name);
        document1.setPath(share);
        document1.setId_document_drive(id_drive);
        try {


            String id = drive_service.getFolderId(file_name);
            System.out.println(drive_service.listFolderContent(id));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        LocalDate myObj = LocalDate.now() ;

        java.sql.Date date = java.sql.Date.valueOf(myObj.toString()) ;

        document1.setPublish_date(date);
        document1.setId_folder(folder.getIdFolder());

        return document1 ;
    }
    @CachePut("load_folder")
    public  Folder Update_Cache_Folder(Folder newest_Document){

        return newest_Document ;
    }

    @CachePut("display_document")
    @CacheEvict(value = "display_document" , key = "{#ID}")
    public  List<Document> Update_Cache_Document( List<Document> newest_Document ,String ID){

        return newest_Document ;
    }
    @Async
    public void create_Category_Drive(String root_name , String foldername) throws GeneralSecurityException, IOException {
        String root_Drive_id = "" ;
        List<com.google.api.services.drive.model.File> fileList =  drive_service.listFolderContent("1Zq8yhHNPbcNV3pH7Zz8MV28cKxZc3SVm") ;
        for (com.google.api.services.drive.model.File file : fileList) {

            if(file.getName().equals(root_name)) {
                root_Drive_id = file.getId(); ;
                break;
            }
        }
        drive_service.create_Folder_ID(root_Drive_id ,  foldername) ;
    }
    public String Find_By_Folder_ID(String category_id  ,  String folder_name) throws Exception {
        Drive driveInstance = Drive_Config.getInstance();
        String id = drive_service.findOrCreateFolder(category_id ,  folder_name ,  driveInstance) ;

        return id ;
    }
//    @Async
    public String Create_Path(MultipartFile [] multipartFiles  , String root_name  , String foldername) throws Exception {

        String file_path = "Data_Document/"+root_name+"/" ;
        String root_Drive_id = "" ;
        List<com.google.api.services.drive.model.File> fileList =  drive_service.listFolderContent("1Zq8yhHNPbcNV3pH7Zz8MV28cKxZc3SVm") ;
        for (com.google.api.services.drive.model.File file : fileList) {

            if(file.getName().equals(root_name)) {
                root_Drive_id = file.getId(); ;
                break;
            }
        }
        String id_folder = Find_By_Folder_ID(root_Drive_id ,  foldername) ;
        file_path += foldername ;
        drive_service.Upload_File(multipartFiles[0], file_path ) ;
        return file_path;

    }


@Async

    public CompletableFuture<Map<Object , Object>> Document_download(String ID , HttpServletResponse response) throws Exception {
        Document document = document_repository.findByID(Integer.parseInt(ID)) ;
        String path =  document.getPath() ;
        String id = document.getFile();
        String id_file =  document.getId_document_drive() ;
        Map<Object , Object> objectObjectMap =  drive_service.download_file(id , id_file , response.getOutputStream()) ;
        return  CompletableFuture.completedFuture(objectObjectMap)  ;

    }

}
// producer
class Send_File implements   Runnable {
    private  final BlockingDeque<MultipartFile>  multipartFileBlockingDeque ;
    private List<MultipartFile> multipartFile ;
    @Autowired
    Upload_document_Event upload_document_event ;
    @Autowired
    Folder_Respository folder_respository ;

    public Send_File(BlockingDeque<MultipartFile> multipartFileBlockingDeque , List<MultipartFile> multipartFile) {
        this.multipartFileBlockingDeque = multipartFileBlockingDeque;
        this.multipartFile =  multipartFile ;
    }
    @Override
    public void run() {

        List<MultipartFile> multipartFiles =  new ArrayList<>() ;
        List<Document> documentList =  new ArrayList<>() ;
        try {
            while(true) {
                if(multipartFile.size() == 0) {
                    break;
                }
                MultipartFile multipartFile1 = multipartFile.get(0) ;
                send_File(multipartFile1);

                multipartFile.remove(0) ;
                wait();

            }

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void send_File(MultipartFile multipartFile) {
        this.multipartFileBlockingDeque.add(multipartFile);
    }


}
class processing_FILE implements  Runnable {
    private  final  BlockingDeque<MultipartFile> multipartFiles ;
    @Autowired
    Document_Repository document_repository ;
    @Autowired
    Folder_Respository folder_respository ;
    @Autowired
    Document_services document_services ;
    @Autowired
    Upload_document_Event upload_document_event ;
    private  String path   ;
    private  Folder folder ;
    public processing_FILE(BlockingDeque<MultipartFile> multipartFiles , String path , Folder folder) {

        this.multipartFiles = multipartFiles;

        this.path =  path ;
        this.folder =  folder ;
    }

    @Override
    public void run() {
        try {

            Processing_File(multipartFiles.take());

        } catch (InterruptedException e) {

            notifyAll();
            throw new RuntimeException(e);

        }
        notifyAll();

    }
    public void Processing_File(MultipartFile multipartFile)  {
        String root_name = upload_document_event.getCreate_folder().getRoot_name() ;

        String category_name = upload_document_event.getCreate_folder().getCode() ;

        String Folder_name =  upload_document_event.getCreate_folder().getFolder_name() ;

        Folder folder1 = folder_respository.findByTitleAndCode(category_name , Folder_name) ;

        if(this.path != null) {
//                String file_path = document_services.Create_File(root_name ,  category_name , Folder_name , multipartFile.getOriginalFilename() , multipartFile);
//
//                Document document = document_services.create_Document_info(multipartFile.getOriginalFilename() ,  file_path , folder1 , "") ;
//
//                document_repository.save(document) ;

        }

    }



}

