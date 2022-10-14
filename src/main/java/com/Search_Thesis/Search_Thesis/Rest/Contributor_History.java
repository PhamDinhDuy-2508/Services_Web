package com.Search_Thesis.Search_Thesis.Rest;

import lombok.Data;

import java.io.Serializable;
import java.util.Hashtable;

@Data
public
class Contributor_History implements Serializable {
    private int ID;
    private Hashtable hashtable;

}
