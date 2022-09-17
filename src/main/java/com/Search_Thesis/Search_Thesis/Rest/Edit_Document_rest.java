package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.User;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
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

    @DeleteMapping("/delete_folder/{id_folder}")
    public ResponseEntity deleteFolder(@PathVariable String id_folder) {
        return null ;
    }
    @GetMapping("/Edit_Folder/{id_folder}")

    @Produces(MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity display(@PathVariable String id_folder) throws ExecutionException, InterruptedException {

        Edit_Get_method edit_get_method =  new Edit_Get_method() ;
        String Title ;

        CompletableFuture<String> getTitle =  document_services_2.get_name_of_Folder(Integer.parseInt(id_folder)) ;
        getTitle.thenApply(title->{
            return title;
        }) ;

        List<Document> list =  document_services_2.load_Document(id_folder) ;

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

////        CompletableFuture<Boolean> update_signal = edit_document_services.delete(Integer.parseInt(id)) ;
//        return update_signal.thenApply(signal->{
//            return ResponseEntity.ok(signal) ;
//        })  ;
        return  null ;

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
