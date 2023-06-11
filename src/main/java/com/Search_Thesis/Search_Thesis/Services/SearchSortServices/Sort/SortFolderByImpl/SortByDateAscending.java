package com.Search_Thesis.Search_Thesis.Services.SearchSortServices.Sort.SortFolderByImpl;

import com.Search_Thesis.Search_Thesis.Model.FolderSolrSearch;
import com.Search_Thesis.Search_Thesis.Services.SearchSortServices.Sort.SortBy;
import com.Search_Thesis.Search_Thesis.Services.Utils.DocumentUtils;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrFolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ByDateAscending")
public class SortByDateAscending implements SortBy<FolderSolrSearch> {
    private SolrFolderRepository folderRepository ;
    @Autowired
    public void setFolderRepository(SolrFolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }


    @Override
    public List<FolderSolrSearch> sortWith(String code, int pageNum) {
        int rows  = DocumentUtils.getPageNumberSize();

        return folderRepository.sortFolderResult(code  , DocumentUtils.pageable(pageNum , rows , sortByAsc()));
    }



    private Sort sortByAsc() {
        return Sort.by(Sort.Direction.ASC , "datePublic" ) ;
    }


}
