package com.Search_Thesis.Search_Thesis.repository.SolrRepository.DAOImpl;

import com.Search_Thesis.Search_Thesis.Model.SolrModels.FolderSolrSearch;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrCommandDAO;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrFolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service("DaoImplSolrFolder")
public class DaoImplSolrFolder implements SolrCommandDAO<FolderSolrSearch> {
    private SolrTemplate solrTemplate;

    @Autowired
    SolrFolderRepository solrFolderRepository ;

    @Override
    public List<FolderSolrSearch> getAll() {
        return  solrFolderRepository.findAll() ;
    }

    @Override
    public Optional<FolderSolrSearch> get(int id) {
        return Optional.empty();
    }

    @Override
    public void delete(FolderSolrSearch folderSolrSearch) {

    }
    @Override
    public void merge(FolderSolrSearch folderSolrSearch) {
        PartialUpdate partialUpdate = new PartialUpdate("id", folderSolrSearch.getId());
        FolderSolrSearch folderSolrSearch1 = solrFolderRepository.findFolderSolrSearchById(folderSolrSearch.getId()) ;
        if(folderSolrSearch1 == null) {
            solrFolderRepository.save(folderSolrSearch1) ;
        }
        else {
            updateChange(folderSolrSearch , folderSolrSearch1 ,  partialUpdate );
            solrTemplate.saveBean("FolderSearch2", partialUpdate);
            solrTemplate.commit("FolderSearch2");
        }
    }
    @Override
    public void Persist(FolderSolrSearch folderSolrSearch) {
    }

    public List<FolderSolrSearch> getByCode(String value) {
        return solrFolderRepository.findFolderSolrSearchByCode(value);
    }
    public int getRecord(String value) {
        return getByCode(value).size();
    }

    public void updateChange(FolderSolrSearch  oldModel, FolderSolrSearch newModel, PartialUpdate partialUpdate) {
        if(!oldModel.equals(newModel)) {
            String[] suggestIndex = newModel.getAutoSuggest();
            partialUpdate.setValueOfField("Category" ,  newModel.getCategory());
            partialUpdate.setValueOfField("code" , newModel.getCode());
            partialUpdate.setValueOfField("Folder_t" , newModel.getFolder());
            partialUpdate.setValueOfField("autoSuggest" , newModel.getAutoSuggest());
        }

    }
}
