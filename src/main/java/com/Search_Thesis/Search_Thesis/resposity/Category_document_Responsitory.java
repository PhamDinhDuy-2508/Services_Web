package com.Search_Thesis.Search_Thesis.resposity;

import com.Search_Thesis.Search_Thesis.Model.Category_document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Category_document_Responsitory extends JpaRepository<Category_document,  Integer> {

    @Query("SELECT u FROM Category_document u WHERE u.root_folder.id= :ID")
    List<Category_document> findByID(int ID) ;

    @Query("SELECT u FROM Category_document u WHERE u.root_folder.id= :ID AND u.code = :code")

    List<Category_document> find_category_code(@Param("ID") int ID, @Param("code") String code) ;

    List<Category_document> findByCode(String Code) ;
    @Query("SELECT u FROM Category_document u WHERE u.category_id = :ID")

    Category_document findByCategory_id(int ID) ;

}
