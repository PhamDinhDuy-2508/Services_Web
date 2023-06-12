package com.Search_Thesis.Search_Thesis.Event.Event;

import com.Search_Thesis.Search_Thesis.DTO.Create_folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;

public class CreateFolderEvent extends ApplicationEvent {
    @Autowired
    private Create_folder create_folder  ;

    public CreateFolderEvent(Object source, Create_folder create_folder) {

        super(source);
        this.create_folder = create_folder ;
    }


    public Create_folder getCreate_folder() {
        return create_folder;
    }

    public void setCreate_folder(Create_folder create_folder) {
        this.create_folder = create_folder;
    }

}
