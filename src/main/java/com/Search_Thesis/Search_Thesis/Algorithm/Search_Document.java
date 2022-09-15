package com.Search_Thesis.Search_Thesis.Algorithm;

import com.Search_Thesis.Search_Thesis.Model.Document;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
@Service
public class Search_Document  implements Search<List<Document> , List<Document>>{
    private  List<Document> documentList ;
    private List<Document> Result ;

    public Search_Document(List<Document> documentList, List<Document>  result) {
        this.documentList = documentList;
        Result = result;
    }

    @Override
    public void Search(String req) {
        this.Result.clear();
        HashMap<String , Document> hashMap =  new HashMap<>()  ;

        for(Document x : documentList) {

            if( NativeSearch(x.getTitle() ,req) ) {
                    Result.add(x);
                }
            }
        }

    @Override
    public List<Document> getResult() {
        return this.Result;
    }

    @Override
    public void setList(List<Document> list) {
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
