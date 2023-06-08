package com.Search_Thesis.Search_Thesis.repository.SolrRepository.DAOImpl;

import com.Search_Thesis.Search_Thesis.Model.DocumentSolrSearch;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrCommandDAO;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service("DaoSolrDocument")
public class DaoImplSolrDocument implements SolrCommandDAO<DocumentSolrSearch> {
    private SolrTemplate solrTemplate;

    @Autowired
    public void setSolrTemplate(SolrTemplate solrTemplate) {
        this.solrTemplate = solrTemplate;
    }

    @Autowired
    private SolrDocumentRepository solrDocumentRepository;

    @Override
    public List<DocumentSolrSearch> getAll() {
        return null;
    }

    @Override
    public Optional<DocumentSolrSearch> get(int id) {
        return Optional.empty();
    }

    @Override
    public void delete(DocumentSolrSearch documentSolrSearch) {

    }

    @Override
    @Transactional
    public void merge(DocumentSolrSearch documentSolrSearch) {
        PartialUpdate partialUpdate = new org.springframework.data.solr.core.query.PartialUpdate("id", documentSolrSearch.getId());
        DocumentSolrSearch documentSolrSearchById = solrDocumentRepository.findDocumentSolrSearchById(documentSolrSearch.getId());
        if (documentSolrSearchById == null) {
            solrDocumentRepository.save(documentSolrSearch);
        } else {
            updateChange(documentSolrSearchById.getContent(), documentSolrSearch.getContent(), partialUpdate);
            partialUpdate.setValueOfField("folderId", documentSolrSearch.getFolderId());
            solrTemplate.saveBean("DocumentSearch", partialUpdate);
            solrTemplate.commit("DocumentSearch");
        }
    }

    @Override
    @Transactional
    public void Persist(DocumentSolrSearch documentSolrSearch) {

    }

    @Override
    public List<DocumentSolrSearch> getByCode(String value) {
        return null;
    }

    public void updateChange(String oldContent, String newContent, PartialUpdate partialUpdate) {
        if (oldContent != newContent) {

            String[] suggestIndex = newContent.split("[-.\\s+]");
            partialUpdate.setValueOfField("autoSuggest_index", suggestIndex);
            partialUpdate.setValueOfField("content", newContent);
        }
    }
}
