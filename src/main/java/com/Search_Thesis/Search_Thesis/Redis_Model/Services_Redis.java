package com.Search_Thesis.Search_Thesis.Redis_Model;

public interface Services_Redis <T, L> {
    T find(String haskey , String ID ) ;
    T findProductById(String userid ,  int ID) ;

    Boolean deleteProduct(String user_id , int  id) ;
    void save_folder_ID(String ID , L elemment) ;
}
