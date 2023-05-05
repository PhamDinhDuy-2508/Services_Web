package com.Search_Thesis.Search_Thesis.DTO;

import java.io.Serializable;

public class Category_question_id_name implements Serializable {
    private static final long  serialVersionUID = -297553281792804395L;
    public Category_question_id_name(int id, String name) {
        this.id = id;
        Name = name;
    }
    int id ;
    String Name ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
