package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Document_info_redis;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.FolderRedisModel;
import com.Search_Thesis.Search_Thesis.Services.HistoryService.History_Services;
import com.Search_Thesis.Search_Thesis.Services.JwtService.JwtService;
import com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService.RedisServiceImpl.Document_Service_redis;
import com.Search_Thesis.Search_Thesis.repository.Document_Repository;
import com.Search_Thesis.Search_Thesis.repository.FolderRepository;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    FolderRepository folder_respository ;
    @Autowired
    RedisTemplate redisTemplate ;
    @Autowired
    @Qualifier("JwtServices")
    JwtService jwt_services ;

    @GetMapping("/load_history/{token}")
    public ResponseEntity Get_Edit_History(@PathVariable String token) {

        jwt_services.setJwt(token);
        JSONObject jsonObject = jwt_services.getPayload();

        int ID = (int) jsonObject.get("id");
        String user_id = String.valueOf(ID)  ;

        try {
            List<Document> list =  new ArrayList<>() ;

             List<FolderRedisModel> folder_model_redis =  history_services.Get_History_Folder(user_id);
             List<Document_info_redis> document_info_redis = history_services.Get_History_Document(user_id);

            history_services.Set_Contributor_History(String.valueOf(ID), document_info_redis , folder_model_redis );

            History_response history_response =  new History_response() ;
            history_response.setFolder_histories(folder_model_redis);
            for (Document_info_redis document_info_redis1 : document_info_redis ) {
                list.add(document_info_redis1.getDocument().get(0)) ;
            }
            history_response.setDocument_histories(list);

            return ResponseEntity.ok(history_response) ;

        }
        catch (Exception e) {
              return  null  ;
        }
    }
    @GetMapping("/Backup_Document/{type_of_file}/{IdOfFile}")
    public void Backup(@PathVariable String type_of_file  , @RequestParam("token") String token, @PathVariable String IdOfFile) throws InterruptedException {
        System.out.println(token);

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
@Data
class History_response{
    private List<Document> document_histories ;
    private  List<FolderRedisModel> folder_histories ;

    @Override
    public String toString() {
        return "History_response{" +
                "document_histories=" + document_histories +
                ", folder_histories=" + folder_histories +
                '}';
    }
}
