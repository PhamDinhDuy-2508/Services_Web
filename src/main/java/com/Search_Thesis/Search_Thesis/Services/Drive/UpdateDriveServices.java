package com.Search_Thesis.Search_Thesis.Services.Drive;

import com.Search_Thesis.Search_Thesis.Model.Root_Folder;
import com.Search_Thesis.Search_Thesis.Services.Utils.DocumentUtils;
import com.Search_Thesis.Search_Thesis.repository.Category_document_Repository;
import com.Search_Thesis.Search_Thesis.repository.Root_Responsitory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service("UpdateDriveServices")
@Slf4j
public class UpdateDriveServices {

    Logger logger = LoggerFactory.getLogger(UpdateDriveServices.class) ;

    private HashMap<String, String> hashMapFolderExisted;
    private DriveService driveService;
    private Category_document_Repository categoryDocumentRepository;

    private Root_Responsitory rootRepository;


    public void UpdateRootDatabase() {
        List<Root_Folder> rootFolderList = rootRepository.findAll();
        rootFolderList.forEach(value -> {
            String hashRoot = "root:" + value.getName();
            if (this.hashMapFolderExisted.get(hashRoot) == null) {

                this.hashMapFolderExisted.put(hashRoot, encoding(value.getId()));

                try {

                    Drive drive = driveService.create_Folder_ID(DocumentUtils.getParentID(), hashRoot);

                    rootRepository.updateParentIdByRootId(drive.getBaseUrl(), String.valueOf(value.getId()));

                } catch (GeneralSecurityException e) {

                    throw new RuntimeException(e);

                } catch (IOException e) {

                    throw new RuntimeException(e);

                }

            }

        });


    }

    public void UpdateCategoryToDatabase() {

    }

    public void UpdateFolderFromDatabase() {


    }

    public HashMap<String, String> getMapFolderExisted() {
        return hashMapFolderExisted;
    }

    private HashMap<String, String> convertListFolderToHashMap(List<File> list) {
        return (HashMap<String, String>) list.stream().collect(
                Collectors.toMap(File::getName, File::getDriveId));
    }

    private void setHashMapFolderExisted(HashMap<String, String> hashMapFolderExisted) {
        this.hashMapFolderExisted = hashMapFolderExisted;
    }

    @Autowired
    private void setCategoryDocumentRepository(Category_document_Repository categoryDocumentRepository) {
        this.categoryDocumentRepository = categoryDocumentRepository;
    }

    @Autowired
    @Qualifier("DriveService")
    private void setDriveService(DriveService driveService) {
        this.driveService = driveService;
    }

    @Autowired
    public void setRootRepository(Root_Responsitory rootRepository) {
        this.rootRepository = rootRepository;
    }

    @PostConstruct
    private void initialize() throws GeneralSecurityException, IOException {
        if (hashMapFolderExisted == null) {
            hashMapFolderExisted = new HashMap<>();
        }
        try {

            final List<File> folderContentList = driveService.listFolderContent(DocumentUtils.getParentID());
            if (!folderContentList.isEmpty()) {
                setHashMapFolderExisted(convertListFolderToHashMap(folderContentList));
            }
        }
        catch (Exception e){logger.error(e.getMessage());}
    }

    private String encoding(String in) {
        byte[] bytes = in.getBytes();
        return Arrays.toString(bytes);
    }

    private String encoding(int input) {
        String in = String.valueOf(input);
        return encoding(in);
    }
}
