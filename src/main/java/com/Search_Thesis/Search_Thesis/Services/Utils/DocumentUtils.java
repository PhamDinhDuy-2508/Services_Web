package com.Search_Thesis.Search_Thesis.Services.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

}
