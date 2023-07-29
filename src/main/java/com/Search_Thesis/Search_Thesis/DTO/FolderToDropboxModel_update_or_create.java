package com.Search_Thesis.Search_Thesis.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FolderToDropboxModel_update_or_create {

    private List<String> paths;

    public FolderToDropboxModel_update_or_create(List<String> path) {
        this.paths = path;
    }
}
