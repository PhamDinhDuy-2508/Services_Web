package com.Search_Thesis.Search_Thesis.resposity;

import com.Search_Thesis.Search_Thesis.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Question_Repository extends JpaRepository<Question , Integer> {
    @Query("SELECT u FROM Question u WHERE u.Question_id = :ID")
    Question findByQuestion_id(int ID) ;
}
