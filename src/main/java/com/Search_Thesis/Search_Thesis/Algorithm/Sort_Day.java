package com.Search_Thesis.Search_Thesis.Algorithm;

import com.Search_Thesis.Search_Thesis.Model.Folder;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sort_Day  implements  Sort<List<Folder>>{
    private List<Folder> list_Output ;
    public Sort_Day() {
        super();
    }

    @Override
    public void Set_T(List<Folder> folders) {
    }

    @Override
    public List<Folder> get_Result() {

        return list_Output ;
    }

    @Override
    public void Filter(List<Folder> folders) {
        Folder temp = new Folder() ;
        Folder folder_array[] = (Folder[]) folders.stream().toArray();
        for (int  i = 0; i < folders.size() ; i++) {
            for (int j  =   i +1   ; j < folders.size() ;j++ ) {
                Date date   =  folder_array[i].getPublish_date() ;
                Date date1 =  folder_array[j].getPublish_date();

                if(date.after(date1))  {
                    temp = folder_array[i];
                    folder_array[i]  = folder_array[j]; ;
                    folder_array[j] =  temp ;
                }
            }
        }
        this.list_Output =  Arrays.stream(folder_array).toList();
    }
    public void Reverse(List<Folder> folders) {
        Folder temp = new Folder() ;

        Collections.sort(folders, Comparator.comparing(s -> s.getPublish_date())) ;
        this.list_Output = folders ;

    }
}
