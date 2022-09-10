package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Model.Root_Folder;
import com.Search_Thesis.Search_Thesis.Services.Document_services_2;
import com.Search_Thesis.Search_Thesis.Services.Dowload_File_Utils;
import com.Search_Thesis.Search_Thesis.Services.Session_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/ckt")
public class download_document_rest {

    @Autowired
    Root_Folder root_folder;

    @Autowired
    Session_Service session_service;

    @Autowired
    Document_services_2 document_services_2;

    @Autowired
    Dowload_File_Utils dowload_file_utils;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ServletContext servletContext;

    @GetMapping("/download/{ID}/{filename}")
    public CompletableFuture<ResponseEntity> download_file(@PathVariable String ID, @PathVariable String filename) throws IOException {
        System.out.println("Main_thread : " +  Thread.currentThread().getName() + Thread.currentThread().getId());
       return document_services_2.Download(ID, filename, this.servletContext).

                thenApply(file_path->{
                    System.out.println("Thread_1  : " +  Thread.currentThread().getName()+ Thread.currentThread().getId());

                    MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(servletContext, ID);
                    Path path = Paths.get(file_path);

                    byte[] data = new byte[0];
                    try {
                        data = Files.readAllBytes(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ByteArrayResource resource = new ByteArrayResource(data);

                    return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName())
                    .contentType(mediaType)
                    .contentLength(data.length)
                    .body(resource);
                })  ;


//        catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//        String file_path = document_services_2.pdf_Path(ID) ;
//        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, ID);
//        Path path = Paths.get(file_path);
//
//        byte[] data = Files.readAllBytes(path);
//        ByteArrayResource resource = new ByteArrayResource(data);
//
//        return ResponseEntity.ok()
//
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName())
//                .contentType(mediaType) //
//                .contentLength(data.length) //
//                .body(resource);
    }


}




