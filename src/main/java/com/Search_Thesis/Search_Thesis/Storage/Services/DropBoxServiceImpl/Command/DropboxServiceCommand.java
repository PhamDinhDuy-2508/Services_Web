package com.Search_Thesis.Search_Thesis.Storage.Services.DropBoxServiceImpl.Command;

import com.Search_Thesis.Search_Thesis.DTO.FolderToDropBoxModel_delete;
import com.Search_Thesis.Search_Thesis.DTO.FolderToDropboxModel_update_or_creare;
import com.Search_Thesis.Search_Thesis.Storage.Constant.Constant;
import com.Search_Thesis.Search_Thesis.Storage.Services.Command.StorageFolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Slf4j
@Service("StorageFolderServiceCommand")
public class DropboxServiceCommand implements StorageFolderService {
    private RestTemplate restTemplate;
    public HttpHeaders httpHeadersDropBox;

    private static java.util.logging.Logger logger = Logger.getLogger(DropboxServiceCommand.log.getName());


    @PostConstruct
    private void initialize() {
        restTemplate = new RestTemplate();
        httpHeadersDropBox = new HttpHeaders();
        setHeader();
    }

    @Async
    @Override
    @Transactional
    public void createFolder(String path) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {

                HttpEntity<FolderToDropboxModel_update_or_creare> folderToDropboxModelHttpEntity = new HttpEntity<>(new FolderToDropboxModel_update_or_creare(Collections.singletonList(path)), httpHeadersDropBox);

                restTemplate.exchange(Constant.dropBoxUrl+Constant.createFolderBatch, HttpMethod.POST, folderToDropboxModelHttpEntity, FolderToDropboxModel_update_or_creare.class);

            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        });
    }

    @Override
    @Async
    @Transactional
    public void deleteFolder(String path) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {

                HttpEntity<FolderToDropBoxModel_delete> folderToDropboxModelHttpEntity = new HttpEntity<>(new FolderToDropBoxModel_delete(Collections.singletonList(path)), httpHeadersDropBox);

                restTemplate.exchange(Constant.dropBoxUrl+Constant.deleteFolderBatch, HttpMethod.POST, folderToDropboxModelHttpEntity, FolderToDropboxModel_update_or_creare.class);

            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        });
    }


    private void setHeader() {
        httpHeadersDropBox.add("Authorization", Constant.Authorization());
        httpHeadersDropBox.add("Content-Type", "application/json");
    }


}
