package com.Search_Thesis.Search_Thesis.repository.SolrRepository;

import com.Search_Thesis.Search_Thesis.Model.FolderSolrSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface SolrFolderRepository extends SolrCrudRepository<FolderSolrSearch ,Integer> {
    @Query("*:*")
    List<FolderSolrSearch> findAll() ;
    FolderSolrSearch findFolderSolrSearchById(int id) ;
    @Query("code:*?0*")

    List<FolderSolrSearch> findFolderSolrSearchByCode(String searchStatement) ;

    @Query("code:?0")
    List<FolderSolrSearch> sortFolderResult(String searchStatement  , Pageable sort) ;





}
