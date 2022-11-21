package com.Search_Thesis.Search_Thesis.resposity;

import com.Search_Thesis.Search_Thesis.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Question_Repository extends JpaRepository<Question , Integer> {
    @Query("SELECT u FROM Question u WHERE u.Question_id = :ID")
    Question findByQuestion_id(int ID) ;

    @Query("SELECT u FROM Question  u ORDER BY u.View  DESC")
    List<Question> findByViewContaining();
    @Query("SELECT u FROM Question  u ORDER BY u.reply.size  DESC")
    List<Question> findByReplyContaining();
}
