package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Event.Create_Category_Event;
import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Model.Create_category;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.Root_Folder;
import com.Search_Thesis.Search_Thesis.Services.Document_services;
import com.Search_Thesis.Search_Thesis.Services.Session_Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController

@RequestMapping("/api/ckt")

public class Document_rest {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher ;

    @Autowired
    Document_services document_services ;

    @Autowired
    Root_Folder root_folder ;

    @Autowired
    Session_Service session_service ;


    List<Category_document> list_category =  new ArrayList<>() ;
    ExecutorService threadpool = Executors.newCachedThreadPool();
    Future<List<Folder>> futureTask  ;




    @GetMapping("/load_category")
    public ResponseEntity<List<Category_document>> response_category(@RequestParam("root") String Root , HttpServletRequest request) {
        System.out.println(Root);
        root_folder = document_services.load_Root_Folder(Root);
        List<Category_document> documentList = new ArrayList<>();

        try {

            documentList = document_services.load_category(root_folder.getId());
            list_category = documentList;

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            session_service.Create_Session(request, "category", gson.toJson(documentList).toString());


            return ResponseEntity.ok(documentList);

        } catch (Exception e) {

            return ResponseEntity.notFound().build();
        }
    }
  @GetMapping("/search_document")

  public  ResponseEntity<List<Category_document>> response_category_search( HttpServletRequest request ,
                                                                      @RequestParam("value") String value ) {
        List<Category_document> res =  new ArrayList<>() ;
        res = document_services.Search_Category(this.list_category ,  value) ;


     return ResponseEntity.ok(res) ;
  }
  @GetMapping("/find_category")
  public  ResponseEntity<Boolean> find_cate( @RequestParam("root") String root_id ,   @RequestParam("code")  String code) throws ExecutionException, InterruptedException {

        boolean check = false ;

        Future<Boolean> search_check =  document_services.find_category_code(root_id, code) ;

        while ( !search_check.isDone()) {}
        check =  search_check.get() ;
        return ResponseEntity.ok(check) ;
  }

  @PostMapping("/Create_new_Category")
  public ResponseEntity<Boolean> create_category(@RequestBody Create_category create_category ) {
        try{
            applicationEventPublisher.publishEvent(new Create_Category_Event(this , create_category));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null   ;
  }



  @GetMapping("/get_folder")
  public void folder( @RequestParam("code")  String code , @RequestParam("request_code") String request) {

      try {
          this.futureTask = threadpool.submit(() -> document_services.get_Folder(code));
      } catch (Exception e) {
          System.out.println(e.getMessage());
      }
  }

  @GetMapping("/search_folder")
    public ResponseEntity<List<Folder>> get_list( @RequestParam("name")  String name) throws ExecutionException, InterruptedException {

        List<Folder> list_Folder =  this.futureTask.get() ;

        return  ResponseEntity.ok(document_services.Search_folder(list_Folder ,  name)) ;
  }
}
@Data
class document_find{
    private String Root_id ;
    private String code ;
}
