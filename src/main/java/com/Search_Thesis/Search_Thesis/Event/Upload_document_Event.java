package com.Search_Thesis.Search_Thesis.Event;

import com.Search_Thesis.Search_Thesis.Model.Create_folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.multipart.MultipartFile;

public class Upload_document_Event extends ApplicationEvent {

    @Autowired
    MultipartFile[] multipartFile ;
    @Autowired
    Create_folder create_folder ;

    public Upload_document_Event(Object source , MultipartFile[] multipartFile , Create_folder create_folder) {
        super(source);
        this.multipartFile =  multipartFile ;
        this.create_folder =  create_folder ;
    }

    public MultipartFile[] getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile[] multipartFile) {
        this.multipartFile = multipartFile;
    }

    public Create_folder getCreate_folder() {
        return create_folder;
    }

    public void setCreate_folder(Create_folder create_folder) {
        this.create_folder = create_folder;
    }


}


