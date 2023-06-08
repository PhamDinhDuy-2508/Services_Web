package com.Search_Thesis.Search_Thesis.Services.Cloudinary.CloudinaryServiceImpl;

import com.Search_Thesis.Search_Thesis.Services.Cloudinary.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("CloudinaryService")
public class CloudinaryServiceImpl implements CloudinaryService {
    @Autowired
    Cloudinary cloudinary;
    public boolean upload_image_with_Byte(String image_path_name, byte[] img_byte_arr) {
        try {
            cloudinary.uploader()
                    .upload(img_byte_arr, ObjectUtils.asMap(
                            "resource_type", "auto",
                            "public_id", "123",
                            "use_filename", true
                    ));
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public Map upload_image_with_path(String path, Map options) {
        try {
            return cloudinary.uploader().upload(path, options);
        } catch (Exception e) {
            return null;
        }
    }

    public Map upload(String name, byte[] byte_arr) {
        synchronized (this) {
            Map image_src = new HashMap<>();
            try {
                image_src = cloudinary.uploader()
                        .upload(byte_arr, ObjectUtils.asMap(
                                "resource_type", "auto",
                                "public_id", name
                        ));
                return image_src;
            } catch (Exception e) {
                return null;
            }
        }
    }

}
