package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Event.Create_Category_Event;
import com.Search_Thesis.Search_Thesis.Event.Create_folder_Event;
import com.Search_Thesis.Search_Thesis.Model.*;
import com.Search_Thesis.Search_Thesis.Redis_Model.Document_Service_redis;
import com.Search_Thesis.Search_Thesis.Redis_Model.Document_redis;
import com.Search_Thesis.Search_Thesis.Services.Document_services;
import com.Search_Thesis.Search_Thesis.Services.Document_services_2;
import com.Search_Thesis.Search_Thesis.Services.Session_Service;
import com.Search_Thesis.Search_Thesis.Services.Session_Service_2;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Reids_respository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RestController

@RequestMapping("/api/ckt")

public class Document_rest {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    Document_Service_redis document_service_redis ;

    @Autowired
    Document_services document_services;

    @Autowired
    Root_Folder root_folder;

    @Autowired
    Session_Service session_service;

    @Autowired
    Document_services_2 document_services_2;
    @Autowired
    Folder_Reids_respository folder_reids_respository ;

    List<Category_document> list_category = new ArrayList<>();
    ExecutorService threadpool = Executors.newCachedThreadPool();
    private List<Document> documentList;
    Future<List<Folder>> futureTask;

    List<Folder> list_folder = new ArrayList<>();

    private int Present_folder_ID;

    @Autowired
    ResourceLoader resourceLoader;


    @GetMapping("/load_category")
    public ResponseEntity<List<Category_document>> response_category(@RequestParam("root") String Root, HttpServletRequest request) {
        root_folder = document_services.load_Root_Folder(Root);
        List<Category_document> documentList = new ArrayList<>();

        try {

            documentList = document_services.load_category(root_folder.getId());
            list_category = documentList;
            Session_Service_2<List<Document>> session_service_2 =  new Session_Service_2<>() ;


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
    public ResponseEntity folder(@RequestParam("code") String code , HttpServletRequest request) {
        this.list_folder.clear();
        Session_Service_2<List<Folder>> session_service_2 =  new Session_Service_2<>() ;
        try {
            this.list_folder = document_services.get_Folder(code);
            String name_of_Session = "List_Folder_"+this.list_folder.get(0).getTitle() ;

            session_service_2.Create_Session(request, name_of_Session ,  this.list_folder );

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok(this.list_folder);
    }

    @GetMapping("/search_folder")
    public ResponseEntity<List<Folder>> get_list(@RequestParam("name") String name , HttpServletRequest request) throws ExecutionException, InterruptedException {

        Session_Service_2<List<Folder>> session_service_2 =  new Session_Service_2<>() ;

        String name_of_Session = "List_Folder_"+name ;

        this.list_folder = session_service_2.Get_Session(request , name_of_Session) ;


        if (this.list_folder.isEmpty()) {
            this.list_folder = this.futureTask.get();
        }
        List<Folder> res = document_services.Search_folder(this.list_folder, name);


        return ResponseEntity.ok(res);
    }

    @GetMapping("/check_folder")
    public ResponseEntity check_folder(@RequestParam("folder") String folder) {
        System.out.println(folder);
        for (Folder x : this.list_folder) {
            if (x.getTitle().equals(folder)) {
                return ResponseEntity.ok(true);
            }
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping("/create_new_folder")
    public void create_folder(@RequestBody Create_folder Create_folder) {

        try {

            applicationEventPublisher.publishEvent(new Create_folder_Event(this, Create_folder));

        } catch (Exception e) {
            System.out.println(e.getMessage());
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
    public ResponseEntity<List<Folder>> display_folder(@PathVariable("code") String code) {

        try {
            return ResponseEntity.ok(document_services_2.load_folder(code));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/display_document")
    public ResponseEntity<List<Document>> display_document(
            @RequestParam("ID") String ID , HttpServletRequest request) {
        documentList = document_services_2.load_Document(ID) ;
        try {
            folder_reids_respository.save_folder_ID(ID, documentList);
        }
        catch (Exception e ) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok(documentList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "Preview_file/{ID}", produces = "application/pdf")

    public ResponseEntity<?> preview(HttpServletRequest request, @PathVariable("ID") String ID) {

        String filename = document_services_2.pdf_Path(ID);


        try {
            session_service.Create_Session(request, "folder", ID);
            return ResponseEntity.ok(resourceLoader.getResource("file:" + filename));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/Search_Document/{code}")

    public CompletableFuture<ResponseEntity> Search_Document(@RequestParam("signal") String signal , HttpServletRequest request, @PathVariable String code) {
        String Haskey = code + "_folder" ;
        System.out.println(Haskey);

        Document_redis document_redis =  document_service_redis.find(Haskey ,  code) ;

        this.documentList =  document_redis.getDocuments() ;

        return document_services_2.search_document(this.documentList,
                signal).thenApply(listdocument_list -> {
            return ResponseEntity.ok(listdocument_list);
        });
    }
}
@Data
class check{
    private String Root_id ;
    private String code ;
}



