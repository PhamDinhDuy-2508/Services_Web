package com.Search_Thesis.Search_Thesis.Rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/document_upload/ckt/upload")
public class upload_document
{
    @GetMapping("/load_root")
    public void load_root(){

    }
    @GetMapping ("/load_all")
    public void load_all(){

    }

    @GetMapping("/load_category")
    public void load() {

    }
}
