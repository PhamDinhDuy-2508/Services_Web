package com.Search_Thesis.Search_Thesis.resposity;

import com.Search_Thesis.Search_Thesis.Model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Reply_respository extends JpaRepository<Reply, Long> {
    @Query("SELECT u FROM Reply u WHERE u.Reply_id = :ID")
    Reply findByReply_id(int ID )  ;

}
