package com.Search_Thesis.Search_Thesis.Services.Drive;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

public interface DriveService {
    List<File> listEverything() throws IOException, GeneralSecurityException;
    String getFolderId(String folderName) throws IOException, GeneralSecurityException, Exception;
    String findOrCreateFolder(String parentId, String folderName, Drive driveInstance) throws Exception;
    String searchFolderId(String parentId, String folderName, Drive service) throws Exception;

    List<File> listFolderContent(String parentId) throws IOException, GeneralSecurityException;

     String Upload_File(MultipartFile file, String filePath) ;
     Drive create_Folder_ID (String ParentId , String folderName) throws GeneralSecurityException, IOException;

     File uploadFile(MultipartFile file, String filePath) ;
     Map<Object ,Object> download_file(String id, String id_file , OutputStream outputStream) throws GeneralSecurityException, IOException;




    }
