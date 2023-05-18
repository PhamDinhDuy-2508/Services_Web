package com.Search_Thesis.Search_Thesis.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.Hashtable;

@Data
public class ContributorHistory implements Serializable {
    private int ID;
    private Hashtable hashtable;

}
