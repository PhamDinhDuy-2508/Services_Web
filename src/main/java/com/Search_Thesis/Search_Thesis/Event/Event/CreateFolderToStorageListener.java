package com.Search_Thesis.Search_Thesis.Event.Event;

import com.Search_Thesis.Search_Thesis.Storage.Services.DropBoxServiceImpl.Command.DropboxServiceCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.time.Clock;

public class CreateFolderToStorageListener extends ApplicationEvent {

    private DropboxServiceCommand dropboxServiceCommand  ;

    public CreateFolderToStorageListener(Object source) {
        super(source);
    }

    public CreateFolderToStorageListener(Object source, Clock clock, DropboxServiceCommand dropboxServiceCommand) {
        super(source, clock);
        this.dropboxServiceCommand = dropboxServiceCommand;
    }

    @Autowired
    @Qualifier("StorageFolderServiceCommand")
    public void setDropboxServiceCommand(DropboxServiceCommand dropboxServiceCommand) {
        this.dropboxServiceCommand = dropboxServiceCommand;
    }

    @EventListener
    @Async
    public void createFolderOnStorage(UploadFolderToStorageEvent uploadFolderToStorageEvent) {
        String Paths  = uploadFolderToStorageEvent.getFolderToDropboxModel().getPaths().get(0) ;
        dropboxServiceCommand.createFolder(Paths);
    }

}
