package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Model.Root_Folder;
import com.Search_Thesis.Search_Thesis.Services.Document_services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/api/ckt")

public class Document_rest {

    @Autowired
    Document_services document_services ;

    @Autowired
    Root_Folder root_folder ;


  @GetMapping("/load_category")
  public ResponseEntity response_category(@RequestParam("root") String Root, Root_Folder root_folder) {

      root_folder = document_services.load_Root_Folder(Root) ;
      try {
          System.out.println(      root_folder.getCategory_document().size() );
      }
      catch (Exception e) {
          System.out.println(e.getMessage());

      }


      return ResponseEntity.ok(document_services.load_category(null)) ;


  }






}
