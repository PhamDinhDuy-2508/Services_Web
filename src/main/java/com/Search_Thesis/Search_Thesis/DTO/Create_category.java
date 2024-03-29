package com.Search_Thesis.Search_Thesis.DTO;

import lombok.Data;
import org.springframework.stereotype.Component;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
//        include = JsonTypeInfo.As.PROPERTY, property = "profileType" ,visible = true) @JsonSubTypes({
//
//        @JsonSubTypes.Type(value = Create_folder.class, name = "Create_folder")
//})
@Data
@Component("create_cate")

public class Create_category {
    private String name;
    private String code;
    private String root_id;
    private String root_name;

    @Override

    public String toString() {
        return "Create_category{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", root_id='" + root_id + '\'' +
                ", root_name='" + root_name + '\'' +
                '}';
    }
}

