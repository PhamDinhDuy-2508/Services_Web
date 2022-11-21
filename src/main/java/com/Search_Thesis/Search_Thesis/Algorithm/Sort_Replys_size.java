package com.Search_Thesis.Search_Thesis.Algorithm;

import com.Search_Thesis.Search_Thesis.Model.Question;

import java.util.Arrays;
import java.util.List;

public class Sort_Replys_size implements Sort<List<Question>> {
    private  List<Question> questionList ;
    @Override
    public void Set_T(List<Question> questions) {

        this.questionList =  questions ;
    }

    @Override
    public List<Question> get_Result() {
        return  this.questionList;
    }

    @Override
    public void Filter(List<Question> questions) {

        Question temp = new Question() ;
        Question folder_array[] =  questions.toArray(new Question[0]);
        for (int  i = 0; i < questions.size() ; i++) {
            for (int j  =   i +1   ; j < questions.size() ;j++ ) {
                int size_1   =  folder_array[i].getReply().size();
                int size_2   =  folder_array[j].getReply().size();
                if(size_1 <= size_2)  {
                    temp = folder_array[i];
                    folder_array[i]  = folder_array[j]; ;
                    folder_array[j] =  temp ;
                }
            }
        }
        this.questionList = Arrays.stream(folder_array).toList();

    }
}
