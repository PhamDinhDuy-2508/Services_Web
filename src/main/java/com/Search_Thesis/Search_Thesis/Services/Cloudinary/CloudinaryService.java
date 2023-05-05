package com.Search_Thesis.Search_Thesis.Services.Cloudinary;

import java.util.Map;

public interface CloudinaryService {
     boolean upload_image_with_Byte(String image_path_name, byte[] img_byte_arr);

     Map upload_image_with_path(String path, Map options);

     Map upload(String image_name, byte[] img_byte_arr);


}
