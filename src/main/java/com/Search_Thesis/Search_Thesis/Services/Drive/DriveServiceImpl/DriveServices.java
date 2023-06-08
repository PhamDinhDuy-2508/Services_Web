package com.Search_Thesis.Search_Thesis.Services.Drive.DriveServiceImpl;

import com.Search_Thesis.Search_Thesis.Config.DriveConfig;
import com.Search_Thesis.Search_Thesis.Services.Drive.DriveService;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("DriveService")
public class DriveServices implements DriveService {
    @Autowired
    private DriveConfig googleDriveConfig;

    public List<File> listEverything() throws IOException, GeneralSecurityException {
        // Print the names and IDs for up to 10 files.
        FileList result = googleDriveConfig.getInstance().files().list()
                .setPageSize(1000)
                .setFields("nextPageToken, files(id, name, size, thumbnailLink, shared)") // get field of google drive file
                .execute();
        return result.getFiles();
    }
    public String getFolderId(String folderName) throws Exception {

        String parentId = null;

        String[] folderNames = folderName.split("/");

        Drive driveInstance = googleDriveConfig.getInstance();

        for (String name : folderNames) {
            parentId = findOrCreateFolder(parentId, name, driveInstance);
        }
        return parentId;
    }
    public String findOrCreateFolder(String parentId, String folderName, Drive driveInstance) throws Exception {

        String folderId = searchFolderId(parentId, folderName, driveInstance);

        if (folderId != null) {
            return folderId;
        }
        File fileMetadata = new File();
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setName(folderName);

        return driveInstance.files().create(fileMetadata)
                .setFields("id")
                .execute()
                .getId();
    }
    public String searchFolderId(String parentId, String folderName, Drive service) throws Exception {
        String folderId = null;
        String pageToken = null;
        FileList result  ;

        File fileMetadata = new File();
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setName(folderName);

        do {
            String query = " mimeType = 'application/vnd.google-apps.folder' ";
            if (parentId == null) {
                query = query + " and 'root' in parents";
            } else {
                query = query + " and '" + parentId + "' in parents";
            }
            result = service.files().list().setQ(query)
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name)")

                    .setPageToken(pageToken)
                    .execute();

            for (File file : result.getFiles()) {
                if (file.getName().equalsIgnoreCase(folderName)) {
                    folderId = file.getId();
                }
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null && folderId == null);

        return folderId;
    }

    public List<File> listFolderContent(String parentId) throws IOException, GeneralSecurityException {
        if (parentId == null) {
            parentId = "1Bv9HyObMnoCw3CDpCLbzi0TNcPcaN9PC";
        }
        String query = "'" + parentId + "' in parents";
        FileList result = googleDriveConfig.getInstance().files().list()
                .setQ(query)
                .setPageSize(10)
                .setFields("nextPageToken,  files(id, name ,parents,webViewLink)")
//                .setFields("webViewLink")// get field of google drive folder
                .execute();
        if(result.getFiles().isEmpty()) {
            return  Collections.emptyList() ;
        }
        System.out.println(result.getFiles());
        return result.getFiles();
    }
    public String Upload_File(MultipartFile file, String filePath) {
        try {
            String folderId = getFolderId(filePath);
            if (null != file) {
                File fileMetadata = new File();
                fileMetadata.setParents(Collections.singletonList(folderId));
                fileMetadata.setName(file.getOriginalFilename());
                File uploadFile = googleDriveConfig.getInstance()
                        .files()
                        .create(fileMetadata, new InputStreamContent(
                                file.getContentType(),
                                new ByteArrayInputStream(file.getBytes()))
                        )
                        .setFields("id").execute();

                return uploadFile.getId();

            }
        } catch (Exception e) {
            System.out.println(e.getMessage()) ;
            return  null ;
        }
        return null;
    }

    public Drive create_Folder_ID (String ParentId , String folderName) throws GeneralSecurityException, IOException {
        Drive driveInstance = DriveConfig.getInstance();
        File fileMetadata = new File();
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setName(folderName);

        if (ParentId != null) {
            fileMetadata.setParents(Collections.singletonList(ParentId));
        }
        driveInstance.files().create(fileMetadata)
                .setFields("id")
                .execute();
        return  driveInstance ;
    }
    public File uploadFile(MultipartFile file, String filePath) {
        try {
            String folderId = getFolderId(filePath);

            if (null != file) {
                File fileMetadata = new File();

                fileMetadata.setParents(Collections.singletonList(folderId));
                fileMetadata.setName(file.getOriginalFilename());
                File uploadFile = DriveConfig.getInstance()
                        .files()
                        .create(fileMetadata, new InputStreamContent(
                                file.getContentType(),
                                new ByteArrayInputStream(file.getBytes()))
                        )
                        //files(id, name ,parents,webViewLink)
                        .setFields("id, name ,parents,webViewLink").execute();

                return uploadFile;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
    public Map<Object ,Object> download_file(String id,   String id_file , OutputStream outputStream) throws GeneralSecurityException, IOException {
        String name = "" ;
        System.out.println(id);
        if(id == null) {
            return null;
        }
        else {
            List<File> fileList = listFolderContent(id) ;

            for  (File file : fileList) {
                if(file.getId().equals(id_file)) {
                    name = file.getName() ;
                    break;
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.writeTo(outputStream);
            baos.toByteArray();
            byte[] bytes = new byte[0];

            outputStream.write(bytes) ;
            Map<Object, Object> map_byte =  new HashMap<>() ;

            map_byte.put("name" , name) ;
            map_byte.put("byte" ,  bytes) ;

            return map_byte ;
        }
    }
    public void Download_Folder(String id_folder ,OutputStream outputStream  ) {

    }
    public void Download_folder_ZIP(String id_folder ,  OutputStream outputStream) throws GeneralSecurityException, IOException {
       List<File> fileList =  listFolderContent(id_folder)  ;
    }
    public boolean Delete_filde_Drive(String id_folder) throws GeneralSecurityException, IOException {
        try {
            DriveConfig.getInstance().files().delete(id_folder).execute();
            return true  ;
        }
        catch ( Exception e) {
            System.out.println(e.getMessage());
            return false ;
        }

    }





}
