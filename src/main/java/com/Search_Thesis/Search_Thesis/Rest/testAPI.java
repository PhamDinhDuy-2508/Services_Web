package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Storage.Services.Command.StorageFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ckt/test")
public class testAPI {
    @Autowired
    @Qualifier("StorageFolderServiceCommand")
    StorageFolderService dropboxServiceCommand;

    @PostMapping("/createFolder")
    public void createFolder() {
        dropboxServiceCommand.createFolder("/Web_Service/Pham Duy/Document/123");
    }
}
