package com.Search_Thesis.Search_Thesis.Services.DocumentService;

import com.Search_Thesis.Search_Thesis.Config.DriveConfig;
import com.Search_Thesis.Search_Thesis.Event.Event.CreateCategoryEvent;
import com.Search_Thesis.Search_Thesis.Event.Event.UploadDocumentEvent;
import com.Search_Thesis.Search_Thesis.Model.*;
import com.Search_Thesis.Search_Thesis.Model.SolrModels.CategoryDocumentSolrSearch;
import com.Search_Thesis.Search_Thesis.Services.Drive.DriveServiceImpl.DriveServices;
import com.Search_Thesis.Search_Thesis.Services.Drive.UpdateDriveServices;
import com.Search_Thesis.Search_Thesis.Services.SessionService.SessionService;
import com.Search_Thesis.Search_Thesis.Services.Utils.DocumentUtils;
import com.Search_Thesis.Search_Thesis.repository.Category_document_Repository;
import com.Search_Thesis.Search_Thesis.repository.Document_Repository;
import com.Search_Thesis.Search_Thesis.repository.FolderRepository;
import com.Search_Thesis.Search_Thesis.repository.Root_Responsitory;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrCategoryDocumentRepository;
import com.google.api.services.drive.Drive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.io.File;
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
    Document document;
    @Autowired
    Folder folder;
    @Autowired
    Root_Folder root_folder;
    @Autowired
    Root_Responsitory root_responsitory;

    @Autowired
    @Qualifier("SessionService")
    SessionService session_serviceImpl;


    @Autowired
    SolrCategoryDocumentRepository solrCategoryDocumentRepository;
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    FolderRepository folder_respository;

    @Autowired
    Category_document category_document;

    @Autowired
    Document_Repository document_repository;

    @Autowired
    DriveServices drive_service;

    private Boolean signal;

    private UpdateDriveServices updateDriveServices ;
    @Autowired
    @Qualifier("UpdateDriveServices")
    public void setUpdateDriveServices(UpdateDriveServices updateDriveServices) {
        this.updateDriveServices = updateDriveServices;
    }


    public Boolean getSignal() {
        return signal;
    }

    public void setSignal(Boolean signal) {
        this.signal = signal;
    }

    @Autowired
    Category_document_Repository category_document_responsitory;

    @Autowired
    CacheManager cacheManager;

    public Root_Folder load_Root_Folder(String ID) {

        return root_responsitory.findRoot_FolderByIdById(Integer.valueOf(ID));

    }

    public List<Category_document> load_category(int ID) {


        List<Category_document> new_list = new ArrayList<>();
        new_list = category_document_responsitory.findByID(ID);
        System.out.println(new_list);
        return new_list;
    }

    public List<Category_document> Search_Category(List<Category_document> category_documentList, String request_detail) {

        return Collections.emptyList();
    }

    @Async
    public Future<Boolean> find_category_code(String root, String code) {

        List<Category_document> category_document = category_document_responsitory.findByID(Integer.valueOf(root));

        Category_document result = new Category_document();
        for (Category_document x : category_document) {

            if (x.getCode().equals(code)) {
                return new AsyncResult<Boolean>(true);
            }
        }
        return new AsyncResult<Boolean>(false);
    }

    @EventListener
    @Async
    public void Create_new_Category(CreateCategoryEvent create_category_event) {
        try {

            root_folder.setId(Integer.valueOf(create_category_event.getCreate_category().getRoot_id()));
            root_folder.setName(create_category_event.getCreate_category().getName());

            category_document.setName(create_category_event.getCreate_category().getName());

            category_document.setCode(create_category_event.getCreate_category().getCode());

            category_document.setRoot_folder(root_folder);

            root_folder.setCategory_document(Collections.singleton(category_document));

            root_responsitory.save(root_folder);
            String id = "";
            for (com.google.api.services.drive.model.File file : drive_service.listFolderContent(DocumentUtils.getParentID())) {

                if (file.getName().equals(create_category_event.getCreate_category().getRoot_name())) {
                    id = file.getId();
                    break;
                }
            }
            drive_service.create_Folder_ID(id, create_category_event.getCreate_category().getCode());
        } catch (Exception e) {
            System.out.println("something was wrong");
        }

    }

    public List<Folder> get_Folder(String code) {
        List<Folder> list = folder_respository.findbyCode(code);
        return list;
    }


    public void Create_Category_Folder(String root, String category) throws IOException {

        String category_path = "D:\\Data\\Document_data\\" + root + "\\" + category;

        File category_root_directory = new File(category_path);

        if (!category_root_directory.exists()) {
            category_root_directory.mkdir();

            Path path = Paths.get(category_path);

            Files.createDirectories(path);
        }
    }

    public void Create_Folder_Directory(String root, String category, String folder) throws IOException {
        Create_Category_Folder(root, category);

        String Folder_Path = "D:\\Data\\Document_data\\" + root + "\\" + category + "\\" + folder;
        System.out.println(Folder_Path);

        File folder_category_root_directory = new File(Folder_Path);

        if (!folder_category_root_directory.exists()) {

            folder_category_root_directory.mkdir();

        }

    }

    @Async
    public CompletableFuture<Boolean> check_Category_Existed(String code) {
        try {

            CategoryDocumentSolrSearch categoryDocumentSolrSearch = solrCategoryDocumentRepository.findCategoryDocumentSolrSearchByCode(code);
            if (categoryDocumentSolrSearch != null) {
            } else {

            }

            Category_document categoryDocument = category_document_responsitory.findByCode(code);

            if (categoryDocument == null) {
                return CompletableFuture.completedFuture(false);
            }
            this.category_document = categoryDocument;
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);

        }
    }

    @Async
    public CompletableFuture<Boolean> check_Folder(String Code, String Title) {
        try {
            Folder folder1 = folder_respository.findByTitleAndCode(Code, Title);
            System.out.println(folder1.getIdFolder());

            if (folder1 == null) {
                return CompletableFuture.completedFuture(false);
            } else {
                return CompletableFuture.completedFuture(true);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return CompletableFuture.completedFuture(false);
        }
    }

    public void Delete_Document_Cache(String ID) {
        redisTemplate.delete("display_document::" + ID);
    }




    @CacheEvict(value = "category_redis", key = "{#Code}")
    public void Delete_Cachce_Category(String Code) {

    }

    @EventListener
    @Async
    public void Create_Document(UploadDocumentEvent upload_document_event) throws Exception {


        String root_name = upload_document_event.getCreate_folder().getRoot_name();

        String category_name = upload_document_event.getCreate_folder().getCode();

        String Folder_name = upload_document_event.getCreate_folder().getFolder_name();

        Folder folder1 = folder_respository.findByTitleAndCode(category_name, Folder_name);

        List<MultipartFile> multipartFiles = new ArrayList<>();

        List<Document> documentList = new ArrayList<>();

        String file_PATH = "Data_Document/" + root_name + "/" + category_name + "/" + Folder_name;

        String id = drive_service.getFolderId(file_PATH);



        for (MultipartFile multipartFile : upload_document_event.getMultipartFile()) {
            multipartFiles.add(multipartFile) ;
            com.google.api.services.drive.model.File file = drive_service.uploadFile(multipartFile, file_PATH);
            System.out.println(file.getParents());

            document = create_Document_info(multipartFile.getOriginalFilename(), file.getParents().get(0), folder1, file.getWebViewLink(), file.getId());
            documentList.add(document);
            document_repository.save(document);


        }
        Delete_Cachce_Category(category_name);

        Update_Cache_Document(documentList, String.valueOf(folder1.getIdFolder()));

    }

    public Document create_Document_info(String file_name, String file_path, Folder folder, String share, String id_drive) throws GeneralSecurityException, IOException {

        Document document1 = new Document();
        document1.setFile(file_path);
        document1.setTitle(file_name);
        document1.setPath(share);
        document1.setId_document_drive(id_drive);
        try {


            String id = drive_service.getFolderId(file_name);
            System.out.println(drive_service.listFolderContent(id));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        LocalDate myObj = LocalDate.now();

        java.sql.Date date = java.sql.Date.valueOf(myObj.toString());

        document1.setPublish_date(date);
        document1.setId_folder(folder.getIdFolder());

        return document1;
    }

    @CachePut("load_folder")
    public Folder Update_Cache_Folder(Folder newest_Document) {

        return newest_Document;
    }

    @CachePut("display_document")
    @CacheEvict(value = "display_document", key = "{#ID}")
    public List<Document> Update_Cache_Document(List<Document> newest_Document, String ID) {
        return newest_Document;
    }

    @Async
    public void createAllRootFolderOnDriveFromDatabase() {
        updateDriveServices.UpdateRootDatabase();
    }



    @Async
    public void create_Category_Drive(String root_name, String foldername) throws GeneralSecurityException, IOException {
        String root_Drive_id = "";
        List<com.google.api.services.drive.model.File> fileList = drive_service.listFolderContent("1Zq8yhHNPbcNV3pH7Zz8MV28cKxZc3SVm");
        for (com.google.api.services.drive.model.File file : fileList) {

            if (file.getName().equals(root_name)) {
                root_Drive_id = file.getId();
                ;
                break;
            }
        }
        drive_service.create_Folder_ID(root_Drive_id, foldername);
    }

    public String Find_By_Folder_ID(String category_id, String folder_name) throws Exception {
        Drive driveInstance = DriveConfig.getInstance();
        String id = drive_service.findOrCreateFolder(category_id, folder_name, driveInstance);

        return id;
    }

    //    @Async
    public String Create_Path(MultipartFile[] multipartFiles, String root_name, String foldername) throws Exception {

        String file_path = "Data_Document/" + root_name + "/";
        String root_Drive_id = "";
        List<com.google.api.services.drive.model.File> fileList = drive_service.listFolderContent("1Zq8yhHNPbcNV3pH7Zz8MV28cKxZc3SVm");
        for (com.google.api.services.drive.model.File file : fileList) {

            if (file.getName().equals(root_name)) {
                root_Drive_id = file.getId();
                ;
                break;
            }
        }
        String id_folder = Find_By_Folder_ID(root_Drive_id, foldername);
        file_path += foldername;
        drive_service.Upload_File(multipartFiles[0], file_path);
        return file_path;

    }


    @Async

    public CompletableFuture<Map<Object, Object>> Document_download(String ID, HttpServletResponse response) throws Exception {
        Document document = document_repository.findByID(Integer.parseInt(ID));
        String path = document.getPath();
        String id = document.getFile();
        String id_file = document.getId_document_drive();
        Map<Object, Object> objectObjectMap = drive_service.download_file(id, id_file, response.getOutputStream());
        return CompletableFuture.completedFuture(objectObjectMap);

    }

}

// producer
//class Send_File implements Runnable {
//    private final BlockingDeque<MultipartFile> multipartFileBlockingDeque;
//    private List<MultipartFile> multipartFile;
//    @Autowired
//    UploadDocumentEvent upload_document_event;
//    @Autowired
//    FolderRepository folder_respository;
//
//    public Send_File(BlockingDeque<MultipartFile> multipartFileBlockingDeque, List<MultipartFile> multipartFile) {
//        this.multipartFileBlockingDeque = multipartFileBlockingDeque;
//        this.multipartFile = multipartFile;
//    }
//
//    @Override
//    public void run() {
//
//        List<MultipartFile> multipartFiles = new ArrayList<>();
//        List<Document> documentList = new ArrayList<>();
//        try {
//            while (true) {
//                if (multipartFile.size() == 0) {
//                    break;
//                }
//                MultipartFile multipartFile1 = multipartFile.get(0);
//                send_File(multipartFile1);
//
//                multipartFile.remove(0);
//                wait();
//
//            }
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public void send_File(MultipartFile multipartFile) {
//        this.multipartFileBlockingDeque.add(multipartFile);
//    }
//
//
//}
//
//class processing_FILE implements Runnable {
//    private final BlockingDeque<MultipartFile> multipartFiles;
//    @Autowired
//    Document_Repository document_repository;
//    @Autowired
//    FolderRepository folder_respository;
//    @Autowired
//    Document_services document_services;
//    @Autowired
//    UploadDocumentEvent upload_document_event;
//    private String path;
//    private Folder folder;
//
//    public processing_FILE(BlockingDeque<MultipartFile> multipartFiles, String path, Folder folder) {
//
//        this.multipartFiles = multipartFiles;
//
//        this.path = path;
//        this.folder = folder;
//    }
//
//    @Override
//    public void run() {
//        try {
//
//            Processing_File(multipartFiles.take());
//
//        } catch (InterruptedException e) {
//
//            notifyAll();
//            throw new RuntimeException(e);
//
//        }
//        notifyAll();
//
//    }
//
//    public void Processing_File(MultipartFile multipartFile) {
//        String root_name = upload_document_event.getCreate_folder().getRoot_name();
//
//        String category_name = upload_document_event.getCreate_folder().getCode();
//
//        String Folder_name = upload_document_event.getCreate_folder().getFolder_name();
//
//        Folder folder1 = folder_respository.findByTitleAndCode(category_name, Folder_name);
//
//        if (this.path != null) {
//        }
//
//    }
//
//
//}
//
