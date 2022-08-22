package com.Search_Thesis.Search_Thesis.resposity;

import com.Search_Thesis.Search_Thesis.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository

public interface SignIn_Respository extends JpaRepository<User ,  Integer> {
    
    @Query("SELECT u FROM User u WHERE u.account = :username and u.password = :password")
    User  findByAccountAndPassword(@Param("username") String username , @Param("password") String password);

    User findUsersByAccount(String user_name) ;

}

