package com.Search_Thesis.Search_Thesis.repository.SolrRepository;

import com.Search_Thesis.Search_Thesis.Model.SolrModels.DocumentSolrSearch;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface SolrDocumentRepository extends SolrCrudRepository<DocumentSolrSearch , Integer> {
    @Query(name = "*:*")
    List<DocumentSolrSearch> findAll() ;


    DocumentSolrSearch findDocumentSolrSearchById(int id) ;

}
