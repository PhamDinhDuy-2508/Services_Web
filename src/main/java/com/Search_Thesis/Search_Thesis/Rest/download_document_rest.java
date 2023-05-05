package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Model.Root_Folder;
import com.Search_Thesis.Search_Thesis.Services.*;
import com.Search_Thesis.Search_Thesis.Services.SessionService.SessionService;
import com.Search_Thesis.Search_Thesis.repository.Document_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
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
    @Qualifier("SessionService")
    SessionService session_serviceImpl;

    @Autowired
    Document_services_2 document_services_2;

    @Autowired
    Dowload_File_Utils dowload_file_utils;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ServletContext servletContext;
    @Autowired
    Drive_Service drive_service ;
    @Autowired
    Document_Repository document_repository ;
    @Autowired
    Document_services  document_services ;

    @GetMapping("/Download_file/{ID}")
    public CompletableFuture<ResponseEntity> download(@PathVariable String ID , HttpServletResponse response) throws Exception {


      return  document_services.Document_download(ID ,  response).thenApply(objectObjectMap -> {
          System.out.println("Thread_1  : " +  Thread.currentThread().getName()+ Thread.currentThread().getId());

          MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(servletContext, ID);

          byte[] data = new byte[0];
          data = (byte[]) objectObjectMap.get("byte");
          ByteArrayResource resource = new ByteArrayResource(data);

          return ResponseEntity.ok()
                  .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + objectObjectMap.get("name"))
                  .contentType(mediaType)
                  .contentLength(data.length)
                  .body(resource);
      });

    }

    @GetMapping("/download/{ID}/{filename}")
    public CompletableFuture<ResponseEntity> download_file(@PathVariable String ID, @PathVariable String filename , HttpServletResponse response) throws IOException {
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
                        System.out.println(e.getMessage());
                    }
                    ByteArrayResource resource = new ByteArrayResource(data);

                    return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName())
                    .contentType(mediaType)
                    .contentLength(data.length)
                    .body(resource);
                })  ;


    }
    @GetMapping("/download_zip/{code}")
    @Produces("application/zip")
    public  void download_zip_file(@PathVariable String code , HttpServletResponse response, HttpServletRequest request) {
        System.out.println(code);
        System.out.println("Main_Thread" + Thread.currentThread().getId());


        try {
            System.out.println("Run a task specified by a Runnable Object asynchronously.");

//
//            CompletableFuture<String> future1 = document_services_2.get_name_of_Folder(Integer.parseInt(code)).thenApplyAsync(filelname->{
//                response.setHeader("Content-Disposition", "attachment; filename="+filelname+".zip");
//                return filelname;
//            }) ;
//            CompletableFuture<Void> future2  = CompletableFuture.runAsync( document_services_2.Download_Zip(Integer.parseInt(code),  response))
//                    .thenApply(check->{
//                        String zipFileName =  "test" ;
//                        System.out.println("Thread1" + Thread.currentThread().getId());
//
//                        response.setStatus(HttpServletResponse.SC_OK);
//                        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename='" + zipFileName + "\"") ;
//                        System.out.println("Done!!!");
//
//                        return null;
//                    }) ;
//            System.out.println("It is also running... ")

            CompletableFuture<String> future1 = document_services_2.get_name_of_Folder(Integer.parseInt(code)).thenApplyAsync(filelname->{
                System.out.println("Thread_3" + Thread.currentThread().getId());

                response.setHeader("Content-Disposition", "attachment; filename="+filelname+".zip");
                return filelname;
            }) ;
            CompletableFuture<Void> future2  = CompletableFuture.runAsync( document_services_2.Download_Zip(Integer.parseInt(code),  response));

            CompletableFuture<Void> combine_future1_future2 = future1.thenCombine(future2 ,(filename,unused) ->{
                System.out.println("Thread1" + Thread.currentThread().getId());

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= test.zip") ;
                        System.out.println("Done!!!");
                        return null;
            }) ;
            System.out.println("It is also running... ");

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



}




