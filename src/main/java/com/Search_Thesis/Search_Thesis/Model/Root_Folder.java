package com.Search_Thesis.Search_Thesis.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "root_folder")
@Component("root")
@EnableAutoConfiguration
@Data
public class Root_Folder {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "idroot_folder")
    private  int id ;

    @Column
    private  String name ;

    @OneToMany(mappedBy = "root_folder", cascade = CascadeType.ALL ,fetch = FetchType.EAGER)
    private List< Category_document> category_document ;

}
