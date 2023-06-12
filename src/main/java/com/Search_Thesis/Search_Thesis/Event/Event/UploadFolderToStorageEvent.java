package com.Search_Thesis.Search_Thesis.Event.Event;

import com.Search_Thesis.Search_Thesis.DTO.FolderToDropboxModel_update_or_creare;
import org.springframework.context.ApplicationEvent;

public class UploadFolderToStorageEvent extends ApplicationEvent {

    private FolderToDropboxModel_update_or_creare folderToDropboxModel ;

    public UploadFolderToStorageEvent(Object source, FolderToDropboxModel_update_or_creare folderModelToCloudinary) {
        super(source);
        setFolderToDropboxModel(folderModelToCloudinary);
    }


    public void setFolderToDropboxModel(FolderToDropboxModel_update_or_creare folderToDropboxModel) {
        this.folderToDropboxModel = folderToDropboxModel;
    }

    public FolderToDropboxModel_update_or_creare getFolderToDropboxModel() {
        return folderToDropboxModel;
    }
}
