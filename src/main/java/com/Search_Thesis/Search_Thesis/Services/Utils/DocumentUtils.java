package com.Search_Thesis.Search_Thesis.Services.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class DocumentUtils {
    @Value("${page.folder.size}")
    private int pageNumberSize;
    @Value("${default.sort.type}")
    private String defaultSortType;
    private static int Page_size;
    private static String defaultSortTypeStatic;

    private  static  String parentID ;



    @Value("${drive.root_folder.id}")
    private String parent_ID ;
    @Value("${drive.root_folder.id}")
    public void setParent_ID(String parent_ID) {
        DocumentUtils.parentID = parent_ID;
    }

    public static String getParentID() {
        return parentID;
    }

    @Value("${default.sort.type}")
    public void setDefaultSortType(String defaultSortType) {
        DocumentUtils.defaultSortTypeStatic = defaultSortType;
    }

    public static String getDefaultSortType() {
        return defaultSortTypeStatic;
    }

    @Value("${page.folder.size}")
    public void setPageNumberSize(int pageNumberSize) {
        DocumentUtils.Page_size = pageNumberSize;
    }

    public static int getPageNumberSize() {
        return Page_size;
    }

    public static int start(int pageNumber) {
        return DocumentUtils.getPageNumberSize() * (pageNumber - 1);
    }

    public static int rows(int pageNumber) {
        return DocumentUtils.getPageNumberSize() * pageNumber;
    }

    public static String convertToFormatYYMMDD(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.format(formatter);
    }
    public static<U> JsonArray convertListToJson(List<U> list) {

        Gson gson =  new Gson() ;
        StringBuilder stringBuilder =  new StringBuilder(gson.toJson(list)) ;
        if(stringBuilder.charAt(0) =='['){
            stringBuilder.deleteCharAt(0);
            stringBuilder.deleteCharAt(stringBuilder.length()-1) ;
        }
        return  null ;
    }
    public static Pageable pageable(int page , int rows , Sort sort) {
        return PageRequest.of(page-1 , rows , sort) ;
    }




}
