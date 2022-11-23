package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Algorithm.Sort_Alphabet;
import com.Search_Thesis.Search_Thesis.Algorithm.Sort_Day;
import com.Search_Thesis.Search_Thesis.Event.Create_Category_Event;
import com.Search_Thesis.Search_Thesis.Event.Create_folder_Event;
import com.Search_Thesis.Search_Thesis.Model.*;
import com.Search_Thesis.Search_Thesis.Redis_Model.Category_Redis;
import com.Search_Thesis.Search_Thesis.Redis_Model.Category_redis_Services;
import com.Search_Thesis.Search_Thesis.Redis_Model.Document_Service_redis;
import com.Search_Thesis.Search_Thesis.Redis_Model.Document_redis;
import com.Search_Thesis.Search_Thesis.Services.*;
import com.Search_Thesis.Search_Thesis.resposity.Category_document_Responsitory;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Reids_respository;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Respository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import java.util.concurrent.*;

@RestController
@Scope("prototype")


@RequestMapping("/api/ckt")

public class Document_rest {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    Document_Service_redis document_service_redis;

    @Autowired
    Drive_Service drive_service ;

    @Autowired
    Document_services document_services;

    @Autowired
    Root_Folder root_folder;

    @Autowired
    Session_Service session_service;

    @Autowired
    Folder_Respository folder_respository;

    @Autowired
    Document_services_2 document_services_2;
    @Autowired
    Folder_Reids_respository folder_reids_respository;
    @Autowired
    Category_document_Responsitory category_document_responsitory ;

    List<Category_document> list_category = new ArrayList<>();
    ExecutorService threadpool = Executors.newCachedThreadPool();
    private List<Document> documentList;
    Future<List<Folder>> futureTask;

    @Autowired
    JWT_Services jwt_services ;

    List<Folder> list_folder = new ArrayList<>();

    private int Present_folder_ID;

    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    CacheManager cacheManager  ;

    @Autowired
    Category_redis_Services category_redis_services ;


    @GetMapping("/load_category")
    public ResponseEntity<List<Category_document>> response_category(@RequestParam("root") String Root, HttpServletRequest request) {
        root_folder = document_services.load_Root_Folder(Root);
        List<Category_document> documentList = new ArrayList<>();

        try {

            documentList = document_services.load_category(root_folder.getId());
            list_category = documentList;
            Session_Service_2<List<Document>> session_service_2 = new Session_Service_2<>();

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            session_service.Create_Session(request, "category", gson.toJson(documentList).toString());


            return ResponseEntity.ok(documentList);

        } catch (Exception e) {

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search_document")

    public ResponseEntity<List<Category_document>> response_category_search(HttpServletRequest request,
                                                                            @RequestParam("value") String value) {
        List<Category_document> res = new ArrayList<>();
        res = document_services.Search_Category(this.list_category, value);


        return ResponseEntity.ok(res);
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
            applicationEventPublisher.publishEvent(new Create_Category_Event(this, create_category));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/get_folder")
    public ResponseEntity folder(@RequestParam("code") String code, HttpServletRequest request) {
        this.list_folder.clear();
        Session_Service_2<List<Folder>> session_service_2 = new Session_Service_2<>();
        try {
            this.list_folder = document_services.get_Folder(code);
            String name_of_Session = "List_Folder_" + this.list_folder.get(0).getTitle();

            session_service_2.Create_Session(request, name_of_Session, this.list_folder);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok(this.list_folder);
    }

    @GetMapping("/search_folder")
    public ResponseEntity<List<Folder>> get_list(@RequestParam("name") String name, HttpServletRequest request) throws ExecutionException, InterruptedException {
//
//        Session_Service_2<List<Folder>> session_service_2 =  new Session_Service_2<>() ;
//
//        String name_of_Session = "List_Folder_"+name ;
//
//        this.list_folder = session_service_2.Get_Session(request , name_of_Session) ;
//
//
//        if (this.list_folder == null) {
//            this.list_folder = this.futureTask.get();
//        }
//        List<Folder> res = document_services.Search_folder(this.list_folder, name);


        return ResponseEntity.ok(null);
    }

    @GetMapping("/check_folder")
    public ResponseEntity check_folder(@RequestParam("folder") String folder) {
        System.out.println(folder);
        List<Folder> folder1 = folder_respository.findByTitle(folder);
        if (folder == null) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }

    }

    @PostMapping("/create_new_folder")
    public void create_folder(@RequestBody Create_folder Create_folder , @RequestParam("token") String token) {
        jwt_services.setJwt(token);
        JSONObject jsonObject = jwt_services.getPayload();
        int user_id = (int) jsonObject.get("id");
        Create_folder.setUser_id(String.valueOf(user_id));
        System.out.println(Create_folder.getCode());

        Category_document categoryDocument =  category_document_responsitory.findByCode(Create_folder.getCode()) ;
        Create_folder.setName(categoryDocument.getName());
        try {
            applicationEventPublisher.publishEvent(new Create_folder_Event(this, Create_folder));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @GetMapping("/check/{Code}")

    public CompletableFuture<ResponseEntity<Boolean>> check_Category(@PathVariable("Code") String Code) {
        System.out.println(Code);

        return document_services.check_Category_Existed(Code).thenApply(ResponseEntity::ok);

    }

    @GetMapping("/check/{Code}/{folder}")
    public CompletableFuture<ResponseEntity> check_folder_Existed(@PathVariable String folder, @PathVariable String Code) {

        System.out.println(folder + Code);
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
        System.out.println(code);
        try {
            Category_document categoryDocument =   category_document_responsitory.findByCode(code) ;
            System.out.println(categoryDocument);


//           Category_Redis  category_redis = category_redis_services.find("Category", code);
            Category_Redis  category_redis =null;
            List<Folder> list = document_services.get_Folder(code);
            JSONObject json = new JSONObject();
            json.put("amount", list.size());
            json.put("name" , categoryDocument.getName()) ;

            String message = json.toString();


            return ResponseEntity.ok(message);



//            if (category_redis == null) {
//                List<Folder> list = document_services_2.load_folder(code);
//                JSONObject json = new JSONObject();
//                json.put("amount", list.size());
//                json.put("name" , categoryDocument.getName()) ;
//                category_redis_services.save_folder_ID(code, list);
//                String message = json.toString();
//
//
//                return ResponseEntity.ok(message);
//            } else {
//
//                List<Folder> list = document_services_2.load_folder(code);
//                JSONObject json = new JSONObject();
//                json.put("amount", list.size());
//                json.put("name" , categoryDocument.getName()) ;
//
//                String message = json.toString();
//
//
//                return ResponseEntity.ok(message);
//            }

        } catch (Exception e) {
            System.out.println(e.getMessage());

            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/display_document")
    public ResponseEntity<List<Document>> display_document(
            @RequestParam("ID") String ID, HttpServletRequest request) {
        documentList = document_services_2.load_Document(ID);
        System.out.println(documentList.size());
        try {
            folder_reids_respository.save_folder_ID(ID, documentList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok(documentList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "Preview_file/{ID}")

    public ResponseEntity<?> preview(HttpServletRequest request, @PathVariable("ID") String ID) throws GeneralSecurityException, IOException {

        String filename = document_services_2.pdf_Path(ID) ;
        System.out.println(filename);
        System.out.println( drive_service.listFolderContent("1kaUe173DrRNbIQ0IJVJJxZ3znpQ33C-V") );



        try {
            session_service.Create_Session(request, "folder", ID);
            return ResponseEntity.ok(resourceLoader.getResource("file:" + filename));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
//        return  null ;
    }

    @GetMapping("/Search_Document/{code}")

    public CompletableFuture<ResponseEntity> Search_Document(@RequestParam("signal") String signal, HttpServletRequest request, @PathVariable String code) {
        String Haskey = code + "_folder";
        System.out.println(Haskey);

        Document_redis document_redis = document_service_redis.find(Haskey, code);

        this.documentList = document_redis.getDocuments();

        return document_services_2.search_document(this.documentList,
                signal).thenApply(listdocument_list -> {
            return ResponseEntity.ok(listdocument_list);
        });
    }

    @GetMapping("/pagination/{page_num}")
    public ResponseEntity Page(@PathVariable String page_num , @RequestParam("Code") String code , @RequestParam("Filter") String filter) {

        int page =  Integer.parseInt(page_num) ;
//        Category_Redis category_redis  = category_redis_services.find("Category" , code) ;
        List<Folder> folderList =document_services_2.load_folder(code) ;

        if(filter.equals("default")) {
            ArrayList<Folder> folder = new ArrayList<Folder>(folderList.subList(0, folderList.size() ));

            List<Folder> folders =  folder.stream().toList() ;


            return ResponseEntity.ok( document_services_2.pagination( code , page ,  folders)) ;
        }
        else  {
            List<Folder> folders  =  document_services_2.Filter(code , filter) ;
            return ResponseEntity.ok(document_services_2.pagination(code ,page , folders)) ;
        }
    }
    @GetMapping("dowload_file/{id}")
    public  ResponseEntity<?> dowload(@PathVariable String id , HttpServletResponse response) throws IOException, GeneralSecurityException {
//        drive_service.dowload_file(id , response.getOutputStream()) ;
        return ResponseEntity.ok(true) ;
    }
    @GetMapping("preview_file_from_Drive/{id}")
    public ResponseEntity<Object> redirectToExternalUrl(@PathVariable String id) throws URISyntaxException {

        String file_path = document_services_2.pdf_Path(id);

        URI yahoo = new URI(file_path);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(yahoo);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
    @GetMapping("/filter/{CatrgoryCode}/{page_num}")
    public ResponseEntity Filter(@RequestParam("signal") String signal, @PathVariable String CatrgoryCode, @PathVariable String page_num) {
        List<Folder> folderList = new ArrayList<>();

        Category_Redis category_redis = category_redis_services.find("Category", CatrgoryCode);

        folderList = category_redis.getFolderList();
        try {
                switch (signal) {
                    case "AZ": {
                        System.out.println("AZ");
                        List<Folder> folderList1 = new ArrayList<>();

                        Sort_Alphabet sort_alphabet = new Sort_Alphabet();

                        sort_alphabet.Filter(folderList);

                        return ResponseEntity.ok(sort_alphabet.get_Result());
                    }
                    case "ZA" :{
                        List<Folder> folderList1 = new ArrayList<>();

                        Sort_Alphabet sort_alphabet = new Sort_Alphabet();

                        sort_alphabet.Reverse(folderList);

                        return ResponseEntity.ok(sort_alphabet.get_Result());
                    }
                    case ("Date"): {
                        System.out.println("Date");
                        List<Folder> folderList1 = new ArrayList<>();

                        Sort_Day sort_alphabet = new Sort_Day();

                        sort_alphabet.Filter(folderList);

                        return ResponseEntity.ok(sort_alphabet.get_Result());
                    }
                    default: {
                        return ResponseEntity.ok(null);
                    }
                }
        }
        catch (Exception e) {
            return ResponseEntity.ok(folderList);
        }
    }

}

@Data
class check{
    private String Root_id ;
    private String code ;
}
@Data
class Pagination{
    private  int number_of_page ;
    private List<Folder> list ;

}



