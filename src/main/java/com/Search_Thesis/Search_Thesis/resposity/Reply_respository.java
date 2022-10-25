package com.Search_Thesis.Search_Thesis.resposity;

import com.Search_Thesis.Search_Thesis.Model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Reply_respository extends JpaRepository<Reply, Long> {

}
