package com.Search_Thesis.Search_Thesis.Model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component("create_folder")
public class Create_folder  {
    private String name;
    private String code;
    private String root_id;
    private String root_name;
    private String folder_name;

    @Override
    public String toString() {
        return "Create_folder{" +
                "folder_name='" + folder_name + '\'' +
                '}';
    }
}
