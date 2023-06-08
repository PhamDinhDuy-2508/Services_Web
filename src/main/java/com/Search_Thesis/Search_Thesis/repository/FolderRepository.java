package com.Search_Thesis.Search_Thesis.repository;

import com.Search_Thesis.Search_Thesis.Model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder , Integer> {


    @Query("SELECT u FROM Folder u WHERE u.categorydocument.code = :code")

    List<Folder> findbyCode(@Param("code") String code) ;

    @Query("SELECT u FROM Folder u WHERE  u.title = :Title")

    List<Folder> findByTitle(String Title) ;
    @Query("SELECT u FROM Folder u WHERE u.categorydocument.code = :Code and u.title = :Title")

    Folder findByTitleAndCode(String Code , String Title) ;

    Folder findByIdFolder(int ID) ;

    @Query("SELECT u FROM Folder u WHERE u.Contributor_ID= :Contributor_ID")

    List<Folder> findByContributor_ID(int Contributor_ID) ;


    @Transactional
    @Modifying
    @Query("delete from Folder b where b.idFolder=:id")
    void deleteByIdFolder(int id);








}
