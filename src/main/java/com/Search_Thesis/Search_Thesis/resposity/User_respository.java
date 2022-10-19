package com.Search_Thesis.Search_Thesis.resposity;

import com.Search_Thesis.Search_Thesis.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface User_respository  extends JpaRepository<User , Integer> {
    User getUserByAccount(String username);
    User findUsersByAccount(String user_name) ;

    @Modifying
    @Transactional
    @Query("update User u set u.password = :password where u.user_id = :user_id  ")
    void updateUserPassword(String password , int user_id );
    User findUsersByEmail(String email) ;
    User getUserByResettoken(String resettoken) ;


    User findById(int userId);
}
