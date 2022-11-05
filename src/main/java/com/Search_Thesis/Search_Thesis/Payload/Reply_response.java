package com.Search_Thesis.Search_Thesis.Payload;

import com.Search_Thesis.Search_Thesis.Model.Reply;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
@Data
public class Reply_response implements Serializable

{
    private static final long  serialVersionUID = -297553281792804395L;


    private ArrayList<Reply> reply ;

    private  int size ;
}
