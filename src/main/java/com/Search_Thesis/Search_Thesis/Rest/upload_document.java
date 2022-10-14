package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Event.Upload_document_Event;
import com.Search_Thesis.Search_Thesis.Model.Create_folder;
import com.Search_Thesis.Search_Thesis.Services.Document_services;
import com.Search_Thesis.Search_Thesis.Services.JWT_Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/ckt")
public class upload_document
{

    @Autowired
    Document_services document_services ;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher ;

    @Autowired
    Create_folder create_folder ;
    @Autowired
    JWT_Services jwt_services ;

    @PostMapping ("/create_folder")
    public void create_folder_DIRECTORY(@RequestBody Create_folder create_folder ) throws IOException {

        this.create_folder =  create_folder ;


        document_services.Create_Folder_Directory(create_folder.getRoot_name() ,
                create_folder.getCode() ,  create_folder.getFolder_name());


        return;
    }

    @PostMapping("/upload_document")
    public ResponseEntity upload_document(@RequestParam("file") MultipartFile[] multipartFile) throws IOException {

        String message = "";
        try {
            List<String> fileNames = new ArrayList<>();

            Arrays.asList(multipartFile).stream().forEach(file -> {
                fileNames.add(file.getOriginalFilename());
            });
            applicationEventPublisher.publishEvent(new Upload_document_Event(this , multipartFile ,  this.create_folder));
            message = "Uploaded the files successfully: " + fileNames;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());

            message = "Fail to upload files!";

        }

        return ResponseEntity.ok("check") ;
    }

}
