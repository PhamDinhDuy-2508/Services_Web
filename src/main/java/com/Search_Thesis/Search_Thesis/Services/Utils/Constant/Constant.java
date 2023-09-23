package com.Search_Thesis.Search_Thesis.Services.Utils.Constant;

public class Constant {
    public static final String dropBoxUrl = "https://api.dropboxapi.com/2/";
    public static final String accessTokenDropBox = "sl.BgEzxsR-8TINI8vaIEiK-wHhVfuYP2o4vsRFReYuL7b4cgkbf_IgUCx9jQscJSPBzKl4AphAUGCvYcDc7bcgMTvZHbeaNA9hOXsg_dVQc_eedzyT-6QwoZ3AHcUbXRqiHKyJllRA";

    public static String Authorization() {
        return "Bearer " + accessTokenDropBox;
    }

    public static final String createFolderBatch = "files/create_folder_batch";

    public static  final String deleteFolderBatch ="files/delete_batch" ;
    public static  final  String rootFolder = "/Web_Service/Pham Duy/Root" ;
    public  static final  int USER_ROLE = 1001 ;
    public  static final  int ADMIN_ROLE = 1002 ;
    public  static final  String USER_ROLE_KEY = "Role.User" ;



}
