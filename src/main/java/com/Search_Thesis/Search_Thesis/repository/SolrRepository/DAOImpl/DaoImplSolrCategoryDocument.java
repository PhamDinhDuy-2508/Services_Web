package com.Search_Thesis.Search_Thesis.repository.SolrRepository.DAOImpl;

import com.Search_Thesis.Search_Thesis.Model.SolrModels.CategoryDocumentSolrSearch;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrCategoryDocumentRepository;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrCommandDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("DaoSolrCategoryDocument")
public class DaoImplSolrCategoryDocument implements SolrCommandDAO<CategoryDocumentSolrSearch> {
    private SolrTemplate solrTemplate;
    private SolrCategoryDocumentRepository solrCategoryDocumentRepository;

    @Autowired
    public void setSolrTemplate(SolrTemplate solrTemplate) {
        this.solrTemplate = solrTemplate;
    }

    @Autowired
    public void setSolrCategoryDocumentRepository(SolrCategoryDocumentRepository solrCategoryDocumentRepository) {
        this.solrCategoryDocumentRepository = solrCategoryDocumentRepository;
    }

    @Override
    public List<CategoryDocumentSolrSearch> getAll() {
        return null;
    }

    @Override
    public Optional<CategoryDocumentSolrSearch> get(int id) {
        return Optional.empty();
    }


    @Override
    public void delete(CategoryDocumentSolrSearch categoryDocumentSolrSearch) {

    }

    @Override
    public void merge(CategoryDocumentSolrSearch categoryDocumentSolrSearch) {
        PartialUpdate partialUpdate = new PartialUpdate("id", categoryDocumentSolrSearch.getId());
        CategoryDocumentSolrSearch categoryDocumentSolrSearch1 = solrCategoryDocumentRepository.findCategoryDocumentSolrSearchById(categoryDocumentSolrSearch.getId());
        if (categoryDocumentSolrSearch1 == null) {
            solrCategoryDocumentRepository.save(categoryDocumentSolrSearch1);
        } else {

            updateChange(categoryDocumentSolrSearch, categoryDocumentSolrSearch1, partialUpdate);
            solrTemplate.saveBean("categorySearch", partialUpdate);
            solrTemplate.commit("categorySearch");

        }
    }

    @Override
    public void Persist(CategoryDocumentSolrSearch categoryDocumentSolrSearch) {

    }

    @Override
    public List<CategoryDocumentSolrSearch> getByCode(String value) {
        return null;
    }

    public void updateChange(CategoryDocumentSolrSearch oldModel, CategoryDocumentSolrSearch newModel, PartialUpdate partialUpdate) {
        if (!oldModel.equals(newModel)) {

            String[] suggestIndex = newModel.getAutoSuggest_index();

            partialUpdate.setValueOfField("autoSuggest_index", suggestIndex);

            partialUpdate.setValueOfField("name", newModel.getName());

            partialUpdate.setValueOfField("code", newModel.getCode());

            partialUpdate.setValueOfField("root", newModel.getRoot());
        }
    }


}
