package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.Document_services;
import com.Search_Thesis.Search_Thesis.Services.Document_services_2;
import com.Search_Thesis.Search_Thesis.Services.JWT_Services;
import com.Search_Thesis.Search_Thesis.resposity.Document_Repository;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Respository;
import com.Search_Thesis.Search_Thesis.resposity.User_respository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
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
    User user ;

    @GetMapping("/displayfolder/{code}")
    public ResponseEntity display_document(@PathVariable String code) {
        List<Folder> folderList =  document_services_2.getMap_category_Folder().get(code) ;


        return  ResponseEntity.ok(folderList) ;
    }

    @GetMapping("/display_category/{token}")
    public ResponseEntity<Set<Category_document>> display_category(@PathVariable String token) throws ExecutionException, InterruptedException {

        jwt_services.setJwt(token);

        System.out.println(token);

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
    @PutMapping("/Edit_Folder/{id_folder}")
    public ResponseEntity updateFolder(@PathVariable String id_folder) {
        return null ;
    }

    @GetMapping("/display_Folder")
    public ResponseEntity displayFolder() {
        return null ;
    }


}
