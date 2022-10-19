package com.Search_Thesis.Search_Thesis.Algorithm;

import com.Search_Thesis.Search_Thesis.Model.Question;

import java.util.ArrayList;
import java.util.List;

public class Search_Question implements  Search<List<Question>, List<Question>> {

    private List<Question> list_input = new ArrayList<>() ;
    private  List<Question> list_ouput =  new ArrayList<>() ;

    @Override
    public void Search(String req) {
//         this.list_input.stream().parallel().forEach(question -> {
//             if(question.get)
//         });

    }

    @Override
    public List<Question> getResult() {
        return null;
    }

    @Override
    public void setList(List<Question> list) {
        this.list_input =  list ;

    }
}
