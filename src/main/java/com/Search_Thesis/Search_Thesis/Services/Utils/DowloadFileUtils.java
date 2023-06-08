package com.Search_Thesis.Search_Thesis.Services.Utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DowloadFileUtils {
    private static Path foundfiles ;
    public static Resource  getfileasResource(String filecode) throws IOException {

        Path dirPath = Paths.get("Files-Upload");

        Files.list(dirPath).forEach(file -> {
            if (file.getFileName().toString().startsWith(filecode)) {
                foundfiles = file;
                return;
            }
        });

        if (foundfiles != null) {
            return new UrlResource(foundfiles.toUri());
        }
        return  null ;
    }
}
