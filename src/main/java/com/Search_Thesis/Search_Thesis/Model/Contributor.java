package com.Search_Thesis.Search_Thesis.Model;

import lombok.Data;

import java.util.Set;

@Data

public class Contributor extends  User {
    private Set<Folder> set_folder ;
}
