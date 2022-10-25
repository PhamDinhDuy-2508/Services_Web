package com.Search_Thesis.Search_Thesis.Model;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Data
public class Content {


    @Autowired
    private Question question;
    private  String content ;
    private HashMap<Integer , String> map_src_image = new HashMap<>() ;

    private  StringBuffer content_result ;
    @Autowired
    Cloudinary cloudinary ;



    public Content(Question question) {
        this.content = question.getContent();
    }
    public void find_src() {
        List<Integer> pos = native_Search(this.content , "src='") ;
        System.out.println(this.content);

        for (Integer i: pos) {
            int pos_ = i ;
            int count = 0  ;
            String imgsrc = "" ;
            while (!String.valueOf(content.charAt(pos_)).equals("'") ) {
                imgsrc +=  String.valueOf(content.charAt(pos_)) ;
                count++ ;
                pos_++ ;

            }
            String tem[] =  imgsrc.split(",") ;

            StringBuffer stringBuilder =  new StringBuffer(this.content) ;

            this.content_result =   stringBuilder.replace(i ,  pos_ , "") ;


            String res =  this.content.substring(i , pos_) ;
            this.map_src_image.put(i ,   tem[tem.length-1]) ;
        }

    }
    public void update_content() {
        List<Integer> jkeys =  this.map_src_image.keySet().stream().toList() ;
        for(Integer i : jkeys) {
            this.content_result.insert(i ,  this.map_src_image.get(i)) ;
        }
        System.out.println(this.content_result);


    }
    public List<Integer> native_Search(String pat , String txt){
        List<Integer> result = new ArrayList<>() ;
        int l1 = pat.length();
        int l2 = txt.length();
        int i = 0, j = l2 - 1;

        for (i = 0, j = l2 - 1; j < l1;) {

            if (txt.equals(pat.substring(i, j + 1))) {
                result.add(i+5) ;
            }
            i++;
            j++;
        }
        return result ;

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

    public void upload_to_Cloudiary() throws IOException {
        List<Integer> keyset = map_src_image.keySet().stream().toList(); ;


        for (Integer i : keyset ) {
            byte[] byte_array =  map_src_image.get(i).getBytes() ;
            upload_image_with_Byte("" , byte_array) ;

        }

    }



}
