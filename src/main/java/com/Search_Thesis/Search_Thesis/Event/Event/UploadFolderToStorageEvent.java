package com.Search_Thesis.Search_Thesis.Event.Event;

import com.Search_Thesis.Search_Thesis.DTO.FolderToDropboxModel_update_or_create;
import org.springframework.context.ApplicationEvent;

public class UploadFolderToStorageEvent extends ApplicationEvent {

    private FolderToDropboxModel_update_or_create folderToDropboxModel ;

    public UploadFolderToStorageEvent(Object source, FolderToDropboxModel_update_or_create folderModelToCloudinary) {
        super(source);
        setFolderToDropboxModel(folderModelToCloudinary);
    }


    public void setFolderToDropboxModel(FolderToDropboxModel_update_or_create folderToDropboxModel) {
        this.folderToDropboxModel = folderToDropboxModel;
    }

    public FolderToDropboxModel_update_or_create getFolderToDropboxModel() {
        return folderToDropboxModel;
    }
}
