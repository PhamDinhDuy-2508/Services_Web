package com.Search_Thesis.Search_Thesis.repository.SolrRepository;

import com.Search_Thesis.Search_Thesis.Model.SolrModels.CategoryDocumentSolrSearch;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface SolrCategoryDocumentRepository extends SolrCrudRepository<CategoryDocumentSolrSearch , Integer> {
    @Query(name ="*:*")
    List<CategoryDocumentSolrSearch> findAll() ;
    CategoryDocumentSolrSearch findCategoryDocumentSolrSearchById(int id) ;

    @Query("code:*?0*")
    CategoryDocumentSolrSearch findCategoryDocumentSolrSearchByCode(String code) ;

}
