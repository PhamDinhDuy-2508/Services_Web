package com.Search_Thesis.Search_Thesis.resposity;

import com.Search_Thesis.Search_Thesis.Model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface Document_Repository extends JpaRepository<Document ,  Integer> {
    @Query("SELECT  u FROM Document u where u.Id_folder= :ID ")
    List<Document> findById_folder(int ID) ;

    Document findByID(int ID) ;

    @Transactional
    @Query("DELETE  FROM Document u where u.Id_folder= :ID ")
    void deleteById_folder(int ID) ;


}


