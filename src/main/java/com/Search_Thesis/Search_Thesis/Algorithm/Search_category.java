package com.Search_Thesis.Search_Thesis.Algorithm;

import com.Search_Thesis.Search_Thesis.Model.Category_document;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service

public class Search_category implements Search<List<Category_document>, Map<String , Category_document>> {
    private  List<Category_document> documentList ;
    private Map<String , Category_document> Result ;

    public Search_category(List<Category_document> documentList, Map<String , Category_document> result) {
        this.documentList = documentList;
        Result = result;
    }

    @Override
    public void Search(String req) {
        this.Result.clear();
        HashMap<String , Category_document> hashMap =  new HashMap<>()  ;
        Thread t = new Thread() {
            public void run() {
                for(Category_document x : documentList) {

                    if( NativeSearch(x.getCode() ,req) ) {
                        if(Result.get(x.getCode()) == null) {
                            Result.put(x.getCode(), x);
                        }
                    }
                }
            }
        };
        t.start();
        for(Category_document x : documentList) {

           if( NativeSearch(x.getName() ,req) ) {
               if(Result.get(x.getCode()) == null) {
                   Result.put(x.getCode(), x);
               }
           }
        }
        try{
            t.join();
        }
        catch (Exception e){}

    }

    @Override
    public Map<String , Category_document> getResult() {
        return this.Result;
    }

    @Override
    public void setList(List<Category_document> list) {
        this.documentList =  list ;
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
