package com.Search_Thesis.Search_Thesis.Algorithm;

import com.Search_Thesis.Search_Thesis.Model.Folder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service

public class Search_Folder implements  Search<List<Folder> ,  List<Folder>> {

    private List<Folder> list_folder_request =  new ArrayList<>() ;
    private List<Folder> list_folder_res =  new ArrayList<>() ;

    @Override
    public void Search(String req) {
        for(Folder x : list_folder_request) {

            if( NativeSearch(x.getTitle() ,req) ) {
                    list_folder_res.add(x);
                }
            }
        }

    @Override
    public List<Folder> getResult() {
        return this.list_folder_res;
    }
    @Override
    public void setList(List<Folder> list)  {

        this.list_folder_request =  list ;

    }
    public boolean NativeSearch(String request ,  String txt){

        int request_length =  request.length() ;
        int txt_length = txt.length()   ;
        int i = 0, j = txt_length - 1;

        for (i = 0, j = txt_length - 1; j < request_length;) {

            if (txt.equals(request.substring(i, j + 1))) {
                return true ;
            }

            i++;
            j++;
        }
        return  false ;

    }
}
