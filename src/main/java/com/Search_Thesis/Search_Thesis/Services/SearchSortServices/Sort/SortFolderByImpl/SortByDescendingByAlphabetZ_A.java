package com.Search_Thesis.Search_Thesis.Services.SearchSortServices.Sort.SortFolderByImpl;

import com.Search_Thesis.Search_Thesis.Model.FolderSolrSearch;
import com.Search_Thesis.Search_Thesis.Services.SearchSortServices.Sort.SortBy;
import com.Search_Thesis.Search_Thesis.Services.Utils.DocumentUtils;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrFolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ByDescendingByAlphabet")
public class SortByDescendingByAlphabetZ_A implements SortBy<FolderSolrSearch> {

    private SolrFolderRepository solrFolderRepository;

    @Autowired
    public void setSolrFolderRepository(SolrFolderRepository solrFolderRepository) {
        this.solrFolderRepository = solrFolderRepository;
    }

    @Override
    public List<FolderSolrSearch> sortWith(String code, int pageNum) {
        int rows  = DocumentUtils.getPageNumberSize();
        return solrFolderRepository.sortFolderResult(code  , DocumentUtils.pageable(pageNum , rows , sortByDesc()));
    }


    private Sort sortByDesc() {
        return Sort.by(Sort.Direction.DESC, "Folder");
    }


}
