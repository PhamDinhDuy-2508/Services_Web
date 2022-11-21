package com.Search_Thesis.Search_Thesis.Model;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@Valid
@Component("create_folder")
public class Create_folder  {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @NotBlank
    private String root_id;

    @NotBlank
    private String root_name;
    @NotBlank
    private String folder_name;
    @NotBlank
    private  String user_id ;

    @Override
    public String toString() {
        return "Create_folder{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", root_id='" + root_id + '\'' +
                ", root_name='" + root_name + '\'' +
                ", folder_name='" + folder_name + '\'' +
                '}';
    }
}
