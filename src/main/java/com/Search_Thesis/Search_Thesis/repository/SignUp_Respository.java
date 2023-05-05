package com.Search_Thesis.Search_Thesis.repository;

import com.Search_Thesis.Search_Thesis.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SignUp_Respository extends JpaRepository<User  , Integer> {
    @Query("SELECT u FROM User u WHERE u.account = :username")
    User  findByAccount(@Param("username") String username);
    User findByEmail(String email) ;

}
