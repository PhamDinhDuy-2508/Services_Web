package com.Search_Thesis.Search_Thesis.Rest;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component("upload_file")
public class upload_file {

    private String filname;
    private String Directory;
    private double size;

}
