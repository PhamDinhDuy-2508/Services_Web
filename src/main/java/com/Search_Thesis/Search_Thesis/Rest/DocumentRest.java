package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.DTO.Create_category;
import com.Search_Thesis.Search_Thesis.DTO.Create_folder;
import com.Search_Thesis.Search_Thesis.DTO.FolderResponse;
import com.Search_Thesis.Search_Thesis.Event.Event.CreateCategoryEvent;
import com.Search_Thesis.Search_Thesis.Event.Event.CreateFolderEvent;
import com.Search_Thesis.Search_Thesis.Model.*;
import com.Search_Thesis.Search_Thesis.Model.SolrModels.FolderSolrSearch;
import com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService.RedisServiceImpl.Category_redis_Services;
import com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService.RedisServiceImpl.Document_Service_redis;
import com.Search_Thesis.Search_Thesis.Services.Converter.Converter;
import com.Search_Thesis.Search_Thesis.Services.DocumentService.DocumentServices2;
import com.Search_Thesis.Search_Thesis.Services.DocumentService.Document_services;
import com.Search_Thesis.Search_Thesis.Services.Drive.DriveService;
import com.Search_Thesis.Search_Thesis.Services.JwtService.JwtService;
import com.Search_Thesis.Search_Thesis.Services.SearchSortServices.Sort.SortService;
import com.Search_Thesis.Search_Thesis.Services.SessionService.SessionService;
import com.Search_Thesis.Search_Thesis.Services.SessionService.SessionServiceForGenerics;
import com.Search_Thesis.Search_Thesis.Services.UserService.UserServiceImpl.UserServiceImpl;
import com.Search_Thesis.Search_Thesis.repository.Category_document_Repository;
import com.Search_Thesis.Search_Thesis.repository.FolderRepository;
import com.Search_Thesis.Search_Thesis.repository.Folder_Reids_respository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@Scope("prototype")
@RequestMapping("/api/ckt")
public class DocumentRest {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    @Qualifier("DriveService")
    DriveService drive_service;
    @Autowired
    Document_services document_services;
    @Autowired
    @Qualifier("SessionService")
    SessionService session_serviceImpl;
    @Autowired
    @Qualifier("SessionServiceGenerics")
    SessionServiceForGenerics<List<Folder>> session_service_Generics;
    @Autowired
    FolderRepository folder_respository;
    @Autowired
    DocumentServices2 document_services_2;
    @Autowired
    Folder_Reids_respository folder_reids_respository;
    @Autowired
    Category_document_Repository category_document_responsitory;
    @Autowired
    @Qualifier("JwtServices")
    JwtService jwt_services;
    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    CacheManager cacheManager;
    private Converter<FolderSolrSearch, FolderResponse> converter;

    @Autowired
    @Qualifier("FolderEntityConvertToDto")

    private void setConverter(Converter<FolderSolrSearch, FolderResponse> converter) {
        this.converter = converter;
    }

    @Autowired
    private UserServiceImpl userService;


    private SortService<FolderSolrSearch> sortService;

    @Autowired
    public DocumentRest(@Qualifier("SortService") SortService<FolderSolrSearch> sortService) {
        this.sortService = sortService;
    }


    @Autowired
    public void setList_category(List<Category_document> list_category) {
        this.list_category = list_category;
    }


    private List<Category_document> list_category = new ArrayList<>();
    private List<Document> documentList;
    private List<Folder> list_folder = new ArrayList<>();

    @GetMapping("/load_category")
    public ResponseEntity<List<Category_document>> response_category(@RequestParam("root") String Root, HttpServletRequest request) {

        Root_Folder root_folder = document_services.load_Root_Folder(Root);
        try {
            list_category = document_services.load_category(root_folder.getId());
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            session_serviceImpl.createSession(request, "category", gson.toJson(list_category).toString());
            return ResponseEntity.ok(list_category);

        } catch (Exception e) {

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/find_category")
    public ResponseEntity<Boolean> find_cate(@RequestParam("root") String root_id, @RequestParam("code") String code) throws ExecutionException, InterruptedException {
        boolean check = false;

        Future<Boolean> search_check = document_services.find_category_code(root_id, code);

        while (!search_check.isDone()) {
        }
        check = search_check.get();
        return ResponseEntity.ok(check);
    }

    @PostMapping("/Create_new_Category")
    public ResponseEntity<Boolean> create_category(@RequestBody Create_category create_category) {
        try {
            applicationEventPublisher.publishEvent(new CreateCategoryEvent(this, create_category));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/get_folder")
    public ResponseEntity folder(@RequestParam("code") String code, HttpServletRequest request) {
        this.list_folder.clear();
        try {
            this.list_folder = document_services.get_Folder(code);
            String name_of_Session = "List_Folder_" + this.list_folder.get(0).getTitle();
            session_service_Generics.Create_Session(request, name_of_Session, this.list_folder);

        } catch (Exception e) {
        }
        return ResponseEntity.ok(this.list_folder);
    }

    @GetMapping("/check_folder")
    public ResponseEntity check_folder(@RequestParam("folder") String folder) {
        if (folder == null) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }

    }

    @PostMapping("/create_new_folder")
    public void create_folder(@RequestBody Create_folder Create_folder, @RequestParam("token") String token) {

        Create_folder.setUser_id(String.valueOf(userService.getUSerIdFromJwt(token)));

        try {
            applicationEventPublisher.publishEvent(new CreateFolderEvent(this, Create_folder));

        } catch (Exception e) {
        }
    }

    @GetMapping("/check/{Code}")

    public CompletableFuture<ResponseEntity<Boolean>> check_Category(@PathVariable("Code") String Code) {

        return document_services.check_Category_Existed(Code).thenApply(ResponseEntity::ok);

    }

    @GetMapping("/check/{Code}/{folder}")
    public CompletableFuture<ResponseEntity> check_folder_Existed(@PathVariable String folder, @PathVariable String Code) {
        return document_services.check_Folder(Code, folder).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/display_root")
    public ResponseEntity<List<List<Category_document>>> Display_Folder() {

        try {
            return ResponseEntity.ok(document_services_2.load_category());

        } catch (Exception e) {

            return ResponseEntity.ok(null);

        }
    }

    @GetMapping("/display_folder/{code}")
    public ResponseEntity display_folder(@PathVariable("code") String code) {
//        cacheManager.getCache("category_redis").clear();
        try {
            Category_document categoryDocument = category_document_responsitory.findByCode(code);

            List<Folder> list = document_services.get_Folder(code);

            JSONObject json = new JSONObject();

            json.put("amount", list.size());

            json.put("name", categoryDocument.getName());

            String message = json.toString();

            return ResponseEntity.ok(message);


        } catch (Exception e) {

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/display_document")
    public ResponseEntity<List<Document>> display_document(
            @RequestParam("ID") String ID, HttpServletRequest request) {
        documentList = document_services_2.load_Document(ID);
        try {
            folder_reids_respository.save_folder_ID(ID, documentList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok(documentList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "Preview_file/{ID}")

    public ResponseEntity<?> preview(HttpServletRequest request, @PathVariable("ID") String ID) throws GeneralSecurityException, IOException {

        String filename = document_services_2.pdf_Path(ID);
        try {
            session_serviceImpl.createSession(request, "folder", ID);
            return ResponseEntity.ok(resourceLoader.getResource("file:" + filename));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/page/{code}/{page}/{sortBy}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public String Page(@PathVariable String code, @PathVariable String sortBy, @PathVariable String page) {
        return  document_services_2.loadFolder(code, page, sortBy).toString();
    }

    /// category->Folder( lan dau tien )
    @GetMapping(value = "/pagination/{Code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String Page(@PathVariable String Code) throws ParseException {
        return document_services_2.loadFolder(Code).toString();
    }

    @GetMapping(value = "/loadFolder/{Code}")
    public String LoadFolderForFirstRedirectFormCategory(@PathVariable String Code) throws ParseException {
        return document_services_2.loadFolder(Code).toString();
    }

    @GetMapping("dowload_file/{id}")
    public ResponseEntity<?> dowload(@PathVariable String id, HttpServletResponse response) throws IOException, GeneralSecurityException {
        return ResponseEntity.ok(true);
    }

    @GetMapping("preview_file_from_Drive/{id}")
    public ResponseEntity<Object> redirectToExternalUrl(@PathVariable String id) throws URISyntaxException {

        String file_path = document_services_2.pdf_Path(id);

        URI yahoo = new URI(file_path);

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setLocation(yahoo);

        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @GetMapping("/sortBy/{Code}/{page_num}")
    public ResponseEntity sortBy(@RequestParam("signal") String signal, @PathVariable String Code, @PathVariable String page_num) {
        return ResponseEntity.ok(converter.convertFromEntityListType
                (sortService.setOption(signal).
                        sortWith(Code, Integer.parseInt(page_num))));
    }

    @PostMapping("/createRoot")
    public void createRoot() {
        try {
            System.out.println(drive_service.listEverything());
        } catch (Exception e) {
        }
    }


}




