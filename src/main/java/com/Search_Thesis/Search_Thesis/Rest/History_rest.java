package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Redis_Model.Document_Service_redis;
import com.Search_Thesis.Search_Thesis.Services.History_Services;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/ckt")
public class History_rest {
    @Autowired
    Document_Service_redis document_service_redis ;
    @Autowired
    History_Services history_services ;

    @GetMapping("/load_history/{user_id}")
    public ResponseEntity Get_Edit_History(@PathVariable String user_id) {

        history_services.Get_All_Edit_Service(user_id);

        return null ;
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