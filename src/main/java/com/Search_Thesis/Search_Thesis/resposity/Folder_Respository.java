package com.Search_Thesis.Search_Thesis.resposity;

import com.Search_Thesis.Search_Thesis.Model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Folder_Respository  extends JpaRepository<Folder , Integer> {


    @Query("SELECT u FROM Folder u WHERE u.categorydocument.code = :code")

    List<Folder> findbyCode(@Param("code") String code) ;

    Folder findByTitle(String Title) ;
    @Query("SELECT u FROM Folder u WHERE u.categorydocument.code = :Code and u.title = :Title")

    Folder findByTitleAndCode(String Code , String Title) ;
}
