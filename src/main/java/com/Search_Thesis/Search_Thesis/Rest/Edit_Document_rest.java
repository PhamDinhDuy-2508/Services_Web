package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Redis_Model.Category_Redis;
import com.Search_Thesis.Search_Thesis.Redis_Model.Category_redis_Services;
import com.Search_Thesis.Search_Thesis.Redis_Model.Document_Service_redis;
import com.Search_Thesis.Search_Thesis.Redis_Model.Document_redis;
import com.Search_Thesis.Search_Thesis.Services.Document_services;
import com.Search_Thesis.Search_Thesis.Services.Document_services_2;
import com.Search_Thesis.Search_Thesis.Services.Edit_Document_Services;
import com.Search_Thesis.Search_Thesis.Services.JWT_Services;
import com.Search_Thesis.Search_Thesis.resposity.Document_Repository;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Respository;
import com.Search_Thesis.Search_Thesis.resposity.User_respository;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/ckt")
public class Edit_Document_rest {
    @Autowired
    Document_Repository document_repository ;
    @Autowired
    Document_services document_services ;
    @Autowired
    Document_services_2 document_services_2 ;
    @Autowired
    Folder_Respository folder_respository ;
    @Autowired
    JWT_Services jwt_services ;
    @Autowired
    User_respository user_respository ;
    @Autowired
    Edit_Document_Services edit_document_services ;
    @Autowired
    Document_Service_redis document_service_redis ;
    @Autowired
    Category_redis_Services category_redis_services ;

    @Autowired
    User user ;

    @GetMapping("/displayfolder/{code}")
    public ResponseEntity display_document(@PathVariable String code) {
        List<Folder> folderList =  document_services_2.getMap_category_Folder().get(code) ;


        return  ResponseEntity.ok(folderList) ;
    }

    @GetMapping("/display_category/{token}")
    public ResponseEntity<Set<Category_document>> display_category(@PathVariable String token) throws ExecutionException, InterruptedException {

        jwt_services.setJwt(token);

        JSONObject jsonObject = jwt_services.getPayload();
        String username = (String) jsonObject.get("sub");
        user =  user_respository.findUsersByAccount(username) ;

        String id = String.valueOf(this.user.getUser_id());
        document_services_2.get_folder_with_userId(id) ;
        Set<Category_document> list =  document_services_2.get_category_with_id_folder()  ;

        return ResponseEntity.ok(list) ;

    }


    @GetMapping("/Edit_Folder/{id_folder}")

    @Produces(MediaType.APPLICATION_JSON_VALUE)


    public ResponseEntity display(@PathVariable String id_folder) throws ExecutionException, InterruptedException {

        Edit_Get_method edit_get_method =  new Edit_Get_method() ;
        String Title ;
        String key =  id_folder+"_folder" ;
        List<Document> list  ;

        CompletableFuture<String> getTitle =  document_services_2.get_name_of_Folder(Integer.parseInt(id_folder)) ;
        getTitle.thenApply(title->{
            return title;
        }) ;


        if(document_service_redis.find(key ,  id_folder) == null) {
            list =  document_services_2.load_Document(id_folder) ;
            document_service_redis.save_folder_ID(id_folder ,  list);
        }
        else {
          Document_redis document_redis =  document_service_redis.find(key ,  id_folder);
          list =  document_redis.getDocuments() ;

        }

        Title = getTitle.get();

        edit_get_method.setDocument(list);
        edit_get_method.setName(Title);

        return ResponseEntity.ok(edit_get_method);
    }
    @PutMapping("/Edit_Folder")

    public CompletableFuture<ResponseEntity> update(@RequestParam Edit_Post_method edit_post_method) {

        CompletableFuture<Boolean> update_signal = edit_document_services.update(edit_post_method.getID() , edit_post_method.getName()) ;
        return update_signal.thenApply(signal->{
            return ResponseEntity.ok(signal) ;
        })  ;
    }



    @DeleteMapping("/Edit_Folder/{id}")
    public CompletableFuture<ResponseEntity> Delete(@PathVariable String id) {

        CompletableFuture<Boolean> update_signal = edit_document_services.delete_folder(Integer.parseInt(id)) ;
        return update_signal.thenApply(signal->{

            return ResponseEntity.ok(signal) ;

        })  ;
    }
    public String ReadCookies(HttpServletRequest request , String name) {
        Cookie[] cookies = request.getCookies();
        try {
            if (cookies != null) {

                for (Cookie cookie : cookies) {
                    System.out.println(cookie.getName()  + cookie.getValue());
                    if (cookie.getName().equals(name)) {
                        return cookie.getValue();
                    }
                }
            } else {
                return "";
            }
            return "";
        }
        catch (Exception e) {
            return "" ;
        }
    }

    @PostMapping("/deleteFolderRequest/{id_folder}/{id_document}")

    public void WriteCookies(HttpServletRequest request , HttpServletResponse response ,  @PathVariable String id_document, @PathVariable String id_folder) {
        String delete_Cookie = "";

        if(id_document.equals("all")) {
            delete_Cookie = ReadCookies(request, "deleteRequest");

            delete_Cookie = id_folder+"_all";

        }
        else {
            delete_Cookie = ReadCookies(request, "deleteRequest");
            if (delete_Cookie.isEmpty()) {
                delete_Cookie = id_document;
            } else {
                delete_Cookie += "_" + id_document;

            }
        }
        Cookie cookie1 = new Cookie("deleteRequest", delete_Cookie);
        cookie1.setDomain("localhost");
        cookie1.setPath("/");
        cookie1.setMaxAge(60 * 3600);

        response.addCookie(cookie1);
        Cookie[] allCookies = request.getCookies();
    }

    @DeleteMapping("/delete_folder/{id_folder}")
    public ResponseEntity deleteFolder(HttpServletResponse response ,  HttpServletRequest request, @PathVariable String id_folder  ) {

        String deleteList =    ReadCookies(request , "deleteRequest") ;
        deleteCookie(request , response , "deleteRequest");
        List<String> myList = new ArrayList<String>(Arrays.asList(deleteList.split("_")));

        if(deleteList.equals(id_folder+"_all")) {
            edit_document_services.Delete_Folder(id_folder);
        }
        else {

            System.out.println(myList);
            edit_document_services.Delete_Document(myList);

        }
//
        return null ;
    }
    @Cacheable(value = "pagination"  , key = "{#page_num  ,  #code ,  #filter}")
    @GetMapping("pagination/{page_num}/{code}/{filter}")
    public ResponseEntity pagination_document(@PathVariable String page_num, @PathVariable String code, @PathVariable String filter) {
        int page =  Integer.parseInt(page_num) ;
        Category_Redis category_redis  = category_redis_services.find("Category" , code) ;

        if(filter.equals("default")) {
            List<Folder> folders =  category_redis.getFolderList() ;

            return ResponseEntity.ok( document_services_2.pagination(code , page ,  folders)) ;
        }
        else  {
            List<Folder> folders  =  document_services_2.Filter(code , filter) ;
            return ResponseEntity.ok(document_services_2.pagination(code , page , folders)) ;
        }

    }

    public void deleteCookie( HttpServletRequest request  , HttpServletResponse response , String name) {
        Cookie[] cookies = request.getCookies();
        try {
            if (cookies != null) {

                for (Cookie cookie : cookies) {
                    System.out.println(cookie.getName());
                    if (cookie.getName().equals(name)) {
                        cookie.setValue("");
                        cookie.setPath("/");

                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                        return;
                    }
                }
            }
        }
        catch (Exception e) {
            return  ;
        }
    }
}
@Data
class Edit_Get_method {
    private String name ;
    private List<Document> document ;

}
@Data
class Edit_Post_method {
    private  int ID ;
    private String name ;

}
