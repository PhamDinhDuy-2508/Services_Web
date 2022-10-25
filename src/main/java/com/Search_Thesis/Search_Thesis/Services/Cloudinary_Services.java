package com.Search_Thesis.Search_Thesis.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Service
public class Cloudinary_Services {
    @Autowired
    Cloudinary cloudinary ;

    public void get_image() {

    }
    public  boolean upload_image_with_Byte(String image_path_name ,  byte[] img_byte_arr ) throws IOException {
        try {
            cloudinary.uploader()
                    .upload(img_byte_arr, ObjectUtils.asMap(
                            "resource_type" , "auto" ,
                            "public_id" , "123" ,
                            "use_filename" ,  true
                    ));
            return true ;
        }
        catch ( Exception e) {
            System.out.println(e.getMessage());
            return false ;
        }

    }
    public void download_image() {
    }
    public Map upload_image_with_path(String path , Map options) {
        try {

            return cloudinary.uploader().upload(path, options);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null  ;
        }
    }
    public synchronized Map upload (  String image_name ,  byte[] img_byte_arr) {
        Map image_src = new HashMap<>() ;
        try {
          image_src  =   cloudinary.uploader()
                    .upload(img_byte_arr, ObjectUtils.asMap(
                            "resource_type" , "auto" ,
                            "public_id" , image_name
                            ));
            return image_src ;
        }
        catch ( Exception e) {
            System.out.println(e.getMessage());
            return null ;
        }

    }

}
