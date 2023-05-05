package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.DTO.Create_folder;
import com.Search_Thesis.Search_Thesis.Event.Upload_document_Event;
import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Model.Root_Folder;
import com.Search_Thesis.Search_Thesis.Services.Document_services;
import com.Search_Thesis.Search_Thesis.Services.Drive_Service;
import com.Search_Thesis.Search_Thesis.Services.JwtService.JwtService;
import com.Search_Thesis.Search_Thesis.repository.Category_document_Responsitory;
import com.Search_Thesis.Search_Thesis.repository.Root_Responsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/ckt")
public class upload_document
{

    @Autowired
    Document_services document_services ;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher ;

    @Autowired
    Drive_Service drive_service ;

    @Autowired
    Create_folder create_folder ;
    @Autowired
    @Qualifier("JwtServices")
    JwtService jwt_services ;

    @Autowired
    Root_Responsitory root_responsitory ;

    @Autowired
    Category_document_Responsitory category_document_responsitory ;

    @PostMapping ("/create_folder")
    public void create_folder_DIRECTORY(@RequestBody Create_folder create_folder ) throws IOException {

        this.create_folder =  create_folder ;


        document_services.Create_Folder_Directory(create_folder.getRoot_name() ,
                create_folder.getCode() ,  create_folder.getFolder_name());

        return;
    }

    @PostMapping("/upload_document/{id_root}/{code_category}/{name_folder}")
    public ResponseEntity<?> upload_document(@RequestParam("file") MultipartFile[] multipartFile , @PathVariable String code_category, @PathVariable String name_folder, @PathVariable String id_root) throws IOException {

        String message = "";
        Root_Folder root_folder = root_responsitory.findRoot_FolderByIdById(Integer.parseInt(id_root)) ;
        Category_document category_document = category_document_responsitory.findByCode(code_category);


        create_folder.setFolder_name(name_folder);
        create_folder.setName(category_document.getName());
        create_folder.setCode(code_category);
        create_folder.setRoot_id(id_root);
        create_folder.setRoot_name(root_folder.getName());


        try {
            List<String> fileNames = new ArrayList<>();

            Arrays.asList(multipartFile).stream().forEach(file -> {
                fileNames.add(file.getOriginalFilename());
            });
            applicationEventPublisher.publishEvent(new Upload_document_Event(this , multipartFile ,  create_folder));
            message = "Uploaded the files successfully: " + fileNames;

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            message = "Fail to upload files!";
        }

        return ResponseEntity.ok("check") ;
    }
    @PostMapping("/create_Category/{id}/{name}")
    public  ResponseEntity<?> Create_Category(@PathVariable String id, @PathVariable String name) {
        try {
            switch (id) {
                case "1" :
                    document_services.create_Category_Drive("Khác" , name);
                    break;
                case "2" :
                    document_services.create_Category_Drive("Chuyên Ngành" , name);
                    break;
                case  "3" :
                    document_services.create_Category_Drive("Đại Cương" , name);
                default: {
                    return  null ;
                }
            }
        }
        catch (Exception e ) {
            System.out.println(e.getMessage());
        }
        return null ;
    }
//    @PostMapping("/upload_document_to_drive/{id}/{name}")

//    @Async
    @PostMapping(value = "/upload_document_to_drive/{id}/{name}"
        )
    public  ResponseEntity<?> upload_document_to_Drive(@RequestParam("file")  MultipartFile []  multipartFiles, @PathVariable String id, @PathVariable String name) throws Exception {
        String path = "" ;

        try {
                switch (id) {
                    case "1" :
                        path +=   document_services.Create_Path(multipartFiles ,  "Khác"  , name );
                        System.out.println(path);
                        drive_service.Upload_File( multipartFiles[0],  path);
                        break;
                    case "2" :
                        System.out.println(Thread.currentThread().getName());
//                        document_services.Create_Path(multipartFiles ,  "Chuyên Ngành"  , name );
//
                        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(()->{
                            try {
                                document_services.Create_Path(multipartFiles ,  "Chuyên Ngành"  , name );
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                            return null;
                        }) ;
                        break;
                    case  "3" :
                         path += document_services.Create_Path(multipartFiles ,  "Đại Cương"  , name );
                        System.out.println(path);
                        drive_service.Upload_File( multipartFiles[0],  path) ;
                    default: {
                        path = "" ;
                    }
                }
            }
            catch (Exception e ) {
                System.out.println(e.getMessage());
            }
            return   ResponseEntity.ok( path) ;

    }


}
