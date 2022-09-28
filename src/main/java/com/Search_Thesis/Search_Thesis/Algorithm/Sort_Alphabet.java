package com.Search_Thesis.Search_Thesis.Algorithm;

import com.Search_Thesis.Search_Thesis.Model.Folder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sort_Alphabet  implements  Sort<List<Folder>>{
    private List<Folder> list_input ;
    private  List<Folder> list_Output ;
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
        Folder folder_array[] =  folders.toArray(new Folder[0]);
        System.out.println(folder_array);

        for (int  i = 0; i < folders.size() ; i++) {
            for (int j  =   i +1   ; j < folders.size() ;j++ ) {
                String name1   =  folder_array[i].getTitle() ;
                String name2 =  folder_array[j].getTitle();
                if(name1.compareTo(name2) > 0)  {
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
        Folder folder_array[] =  folders.toArray(new Folder[0]);
        System.out.println(folder_array);

        Collections.sort(folders, Comparator.comparing(s -> s.getTitle())) ;
        this.list_Output = folders ;

    }



}
