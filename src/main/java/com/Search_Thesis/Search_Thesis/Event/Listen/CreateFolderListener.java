package com.Search_Thesis.Search_Thesis.Event.Listen;

import com.Search_Thesis.Search_Thesis.DTO.FolderToDropboxModel_update_or_creare;
import com.Search_Thesis.Search_Thesis.Event.Event.CreateFolderEvent;
import com.Search_Thesis.Search_Thesis.Event.Event.UploadFolderToStorageEvent;
import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Services.Drive.DriveService;
import com.Search_Thesis.Search_Thesis.repository.Category_document_Repository;
import com.Search_Thesis.Search_Thesis.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class CreateFolderListener {
    private Logger logger =  Logger.getLogger(CreateFolderEvent.class.getName()) ;
    private Category_document_Repository categoryDocumentRepository;

    @Autowired
    public void setCategoryDocumentRepository(Category_document_Repository categoryDocumentRepository) {
        this.categoryDocumentRepository = categoryDocumentRepository;
    }

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public void setApplicationEvent(ApplicationEventPublisher applicationEvent) {
        this.applicationEventPublisher = applicationEvent;
    }

    private FolderRepository folderRepository;

    @Autowired
    public void setFolderRepository(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @Autowired
    DriveService driveService;

    @Autowired
    CacheManager cacheManager;
    private Category_document category_document;
    private Folder folder;

    @PostConstruct
    private void initialize() {
        folder = new Folder();
        category_document = new Category_document();
    }


    @EventListener
    @Async
    public void createFolderListener(CreateFolderEvent create_folder_event) throws Exception {

        Category_document categoryDocument = categoryDocumentRepository.findByCode(create_folder_event.getCreate_folder().getCode());
        create_folder_event.getCreate_folder().setName(categoryDocument.getName());
        try {
            String root_name = create_folder_event.getCreate_folder().getRoot_name();
            String category_name = create_folder_event.getCreate_folder().getCode();
            String folder_name = create_folder_event.getCreate_folder().getFolder_name();

            folder.setTitle(create_folder_event.getCreate_folder().getFolder_name());
            LocalDate myObj = LocalDate.now();

            java.sql.Date date = java.sql.Date.valueOf(myObj.toString());

            folder.setPublish_date(date);

            category_document.setNewfolder(Collections.singleton(folder));
            folder.setCategorydocument(categoryDocument);

            folder.setContributor_ID(Integer.parseInt(create_folder_event.getCreate_folder().getUser_id()));


            folderRepository.save(folder);

            String filePath = "/Web_Service/Pham Duy/" + root_name + "/" + category_name + "/" + folder_name;

            try {
                applicationEventPublisher.publishEvent(new UploadFolderToStorageEvent(this , new FolderToDropboxModel_update_or_creare(List.of(filePath))));

            } catch (Exception e) {
                logger.info(e.getMessage());
            }

            Objects.requireNonNull(cacheManager.getCache("category_redis")).evict(category_name);
        } catch (Exception e) {
            throw new Exception("Cannot Create Folder");
        }

        reInitialize();

    }

    private void reInitialize() {
        folder = new Folder();
        category_document = new Category_document();
    }

}
