package com.Search_Thesis.Search_Thesis.resposity;

import com.Search_Thesis.Search_Thesis.Model.Root_Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Root_Responsitory extends JpaRepository<Root_Folder,  Integer> {
    @Query("SELECT u FROM Root_Folder u WHERE u.id = :ID")
    Root_Folder findRoot_FolderByIdById(int ID) ;

}
