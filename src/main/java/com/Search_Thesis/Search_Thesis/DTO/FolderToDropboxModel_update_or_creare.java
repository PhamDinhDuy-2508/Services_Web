package com.Search_Thesis.Search_Thesis.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FolderToDropboxModel_update_or_creare {

    private List<String> paths;

    public FolderToDropboxModel_update_or_creare() {
        String s = "";
    }

    public FolderToDropboxModel_update_or_creare(List<String> path) {
        this.paths = path;
    }
}
