package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Redis_Model.Document_Service_redis;
import com.Search_Thesis.Search_Thesis.Services.History_Services;
import com.Search_Thesis.Search_Thesis.Services.JWT_Services;
import com.Search_Thesis.Search_Thesis.resposity.Document_Repository;
import com.Search_Thesis.Search_Thesis.resposity.Folder_Respository;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Scope("prototype")
@RequestMapping("api/ckt")
public class History_rest {
    @Autowired
    Document_Service_redis document_service_redis ;
    @Autowired
    History_Services history_services ;

    @Autowired
    Document_Repository document_repository ;
    @Autowired
    Folder_Respository folder_respository ;
    @Autowired
    RedisTemplate redisTemplate ;
    @Autowired
    JWT_Services jwt_services ;

    @GetMapping("/load_history/{token}")
    public ResponseEntity Get_Edit_History(@PathVariable String token) {
        jwt_services.setJwt(token);
        JSONObject jsonObject = jwt_services.getPayload();

        int ID = (int) jsonObject.get("id");
        String user_id = String.valueOf(ID) ;

        try {
            history_services.Get_History_Folder(user_id);
            history_services.Get_History_Document(user_id);

            Contributor_History contributor_history =   history_services.contributor_history(user_id , history_services.getHashtable() ) ;

            contributor_history.setID(Integer.parseInt(user_id));
            contributor_history.setHashtable(history_services.getHashtable());

            String message;
            JSONObject json = new JSONObject();
            json.put("Folder", contributor_history.getHashtable().get("folder"));
            List<Object> t =  new ArrayList<>() ;
            t.add(contributor_history.getHashtable().get("folder")) ;
            t.add(contributor_history.getHashtable().get("document")) ;

            return ResponseEntity.ok(t) ;

        }

        catch (Exception e) {
            System.out.println(e.getMessage());
            return  null  ;
        }

    }
    @GetMapping("/Backup_Document/{type_of_file}/{IdOfFile}")
    public void Backup(@PathVariable String type_of_file  , @RequestParam("token") String token, @PathVariable String IdOfFile) throws InterruptedException {

        jwt_services.setJwt(token);

        JSONObject jsonObject = jwt_services.getPayload();
        int id = (int) jsonObject.get("id");

        if(type_of_file.equals("document")) {
            history_services.Backup_to_Databases(String.valueOf(id),  "document"  , IdOfFile);
        }
        else {
            history_services.Backup_to_Databases(String.valueOf(id),  "Folder" , IdOfFile );
        }
    }
}
@Data
class Folder_History{
    private  int amount ;
    private List<Folder> list ;
}
@Data
class Document_Histoty{
    private  int amount ;
    private  List<Document> documentList ;
}
