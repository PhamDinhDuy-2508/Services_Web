package com.Search_Thesis.Search_Thesis.repository;

import com.Search_Thesis.Search_Thesis.Model.Category_Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Tag_repository extends JpaRepository<Category_Question, Long> {
    @Query("SELECT u FROM Category_Question u WHERE u.Category_id = :ID")
    Category_Question findByCategory_id(int  ID) ;

}
