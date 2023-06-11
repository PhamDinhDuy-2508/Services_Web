package com.Search_Thesis.Search_Thesis.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class FolderToDropBoxModel_delete {
    private List<String> entries;

    public FolderToDropBoxModel_delete(List<String> entries) {
        this.entries = entries;
    }
}
