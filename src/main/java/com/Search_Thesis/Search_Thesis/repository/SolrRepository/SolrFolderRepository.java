package com.Search_Thesis.Search_Thesis.repository.SolrRepository;

import com.Search_Thesis.Search_Thesis.Model.FolderSolrSearch;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface SolrFolderRepository extends SolrCrudRepository<FolderSolrSearch ,Integer> {
    @Query("*:*")
    List<FolderSolrSearch> findAll() ;
    FolderSolrSearch findFolderSolrSearchById(int id) ;
    @Query("code:*?0*")

    List<FolderSolrSearch> findFolderSolrSearchByCode(String searchStatement) ;

    @Query("code:?0 & q.op=OR & indent=true & rows=?1 &start=?2")
    List<FolderSolrSearch> sortFolderResult(String searchStatement , String rows , String start , Sort sort) ;





}
