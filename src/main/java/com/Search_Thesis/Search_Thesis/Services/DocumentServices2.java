package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Algorithm.Search_Document;
import com.Search_Thesis.Search_Thesis.DTO.FolderResponse;
import com.Search_Thesis.Search_Thesis.Model.*;
import com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService.RedisServiceImpl.Category_redis_Services;
import com.Search_Thesis.Search_Thesis.Services.Converter.Converter;
import com.Search_Thesis.Search_Thesis.Services.SearchSortServices.Sort.SortService;
import com.Search_Thesis.Search_Thesis.Services.SessionService.SessionService;
import com.Search_Thesis.Search_Thesis.Services.Utils.DocumentUtils;
import com.Search_Thesis.Search_Thesis.Services.Utils.DowloadFileUtils;
import com.Search_Thesis.Search_Thesis.repository.Category_document_Repository;
import com.Search_Thesis.Search_Thesis.repository.Document_Repository;
import com.Search_Thesis.Search_Thesis.repository.FolderRepository;
import com.Search_Thesis.Search_Thesis.repository.Root_Responsitory;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrCommandDAO;
import com.google.gson.Gson;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Scope("prototype")
public class DocumentServices2 {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    Document_services document_services;

    @Autowired
    Root_Folder root_folder;

    @Autowired
    @Qualifier("SessionService")
    SessionService session_serviceImpl;
    @Autowired
    Root_Responsitory root_responsitory;

    @Autowired
    Category_document_Repository category_document_responsitory;

    @Autowired

    FolderRepository folder_respository;

    @Autowired
    Document_Repository document_repository;

    @Autowired
    DowloadFileUtils dowload_file_utils;

    @Autowired
    Search_Document search_document;

    private Converter<FolderSolrSearch, FolderResponse> converter;

    @Autowired
    @Qualifier("FolderEntityConvertToDto")

    public void setConverter(Converter<FolderSolrSearch, FolderResponse> converter) {
        this.converter = converter;
    }

    @Autowired
    Category_redis_Services category_redis_services;
    private SolrCommandDAO<FolderSolrSearch> solrSearchSolrCommandDAO;

    @Autowired
    @Qualifier("DaoImplSolrFolder")
    public void setDao(SolrCommandDAO<FolderSolrSearch> dao) {
        this.solrSearchSolrCommandDAO = dao;
    }

    private SortService<FolderSolrSearch> sortService;

    @Autowired
    public DocumentServices2(@Qualifier("SortService") SortService<FolderSolrSearch> sortService) {
        this.sortService = sortService;
    }



    private Future<Set<Category_document>> get_Category_task;
    private ExecutorService threadpool = Executors.newCachedThreadPool();

    private HashMap<String, List<Folder>> Map_category_Folder;


    HashMap<String, List<Category_document>> Hash_category_documentList = new HashMap<>();
    private Gson gson ;

    @PostConstruct
    public void initialize() {
        gson =  new Gson() ;
    }
    public List<Root_Folder> load_root() {
        return root_responsitory.findAll();
    }

    public List<List<Category_document>> load_category() {

        List<Root_Folder> root_folders = load_root();
        Hash_category_documentList = new HashMap<>();
        List<List<Category_document>> category_documentList = new ArrayList<>();

        for (int i = 0; i < root_folders.size(); i++) {

            category_documentList.add(category_document_responsitory.findByID(root_folders.get(i).getId()));

        }
        return category_documentList;
    }

    @Cacheable(value = "category_redis", key = "#code")
    public List<Folder> load_folder(String code) {

        return folder_respository.findbyCode(code);
    }

    //    @Cacheable(value = "display_document" ,  key = "#ID")
    public List<Document> load_Document(String ID) {
        try {
            System.out.println(Integer.valueOf(ID));
            return document_repository.findById_folder(Integer.parseInt(ID));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String pdf_Path(String ID_document) {

        Document document = document_repository.findByID(Integer.parseInt(ID_document));

        return document.getPath();
    }

    @Async
    public CompletableFuture<String> Download(String ID, String file_name, ServletContext servletContext) throws IOException {

        String file_path = pdf_Path(ID);
        return CompletableFuture.completedFuture(file_path);

    }

    @Async
    public CompletableFuture<String> get_name_of_Folder(int Code) {

        Folder folder = folder_respository.findByIdFolder(Code);
        return CompletableFuture.completedFuture(folder.getTitle());

    }

    public Runnable Download_Zip(int Code, HttpServletResponse response) {

        List<Document> documents = document_repository.findById_folder(Code);
        if (documents.isEmpty()) {
            return null;
        }

        if (documents.isEmpty()) {
            return null;
        }
        List<String> listOfFileNames = new ArrayList<>();

        for (int i = 0; i < documents.size(); i++) {
            listOfFileNames.add(documents.get(i).getFile());
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Async
    public CompletableFuture<List<Document>> search_document(List<Document> base, String signal) {

        List<Document> result = new ArrayList<>();
        if (signal.isBlank()) {
            return CompletableFuture.completedFuture(base);
        }
        search_document.setList(base);

        search_document.Search(signal);

        result = search_document.getResult();

        return CompletableFuture.completedFuture(result);

    }

    public Set<Category_document> get_category_with_id_folder() throws ExecutionException, InterruptedException {

        Set<Category_document> category_documentList = this.get_Category_task.get();
        return category_documentList;
    }

    public void get_folder_with_userId(String ID) {
        List<Folder> folders = folder_respository.findByContributor_ID(Integer.parseInt(ID));
        this.Map_category_Folder = new HashMap<>();
        Callable callable = () -> {
            Set<Category_document> list = new HashSet<>();
            for (int i = 0; i < folders.size(); i++) {
                list.add(folders.get(i).getCategorydocument());
            }
            return list;
        };
        for (Folder i : folders) {
            if (this.Map_category_Folder.get(i.getCategorydocument().getCode()) == null) {
                List<Folder> folderList = new ArrayList<>();
                folderList.add(i);
                this.Map_category_Folder.put(i.getCategorydocument().getCode(), folderList);
            } else {
                this.Map_category_Folder.get(i.getCategorydocument().getCode()).add(i);
            }
        }
        this.get_Category_task = threadpool.submit(callable);
    }

    public HashMap<String, List<Folder>> getMap_category_Folder() {
        return Map_category_Folder;
    }


    //    @Cacheable(value = "pagination" ,key = "{#code ,  #page} ")
    public Object loadFolder(String code) throws ParseException {
        List<FolderResponse> folderResponseList = converter.convertFromEntityListType
                (sortService.setOption(DocumentUtils.getDefaultSortType()).sortWith(code, 1));
        int totalPage = (solrSearchSolrCommandDAO.getByCode(code).size())/DocumentUtils.getPageNumberSize();

        JSONObject jsonObject = new JSONObject() ;
        jsonObject.put("pageNumber" ,  totalPage + 1) ;
        jsonObject.put("folderName" ,    category_document_responsitory.findByCode(code).getName()) ;
        jsonObject.put("folderList"  ,  folderResponseList) ;
        return jsonObject ;

    }



}
