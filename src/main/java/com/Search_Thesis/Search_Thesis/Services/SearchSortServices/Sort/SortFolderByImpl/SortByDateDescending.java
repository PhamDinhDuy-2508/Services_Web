package com.Search_Thesis.Search_Thesis.Services.SearchSortServices.Sort.SortFolderByImpl;

import com.Search_Thesis.Search_Thesis.Model.FolderSolrSearch;
import com.Search_Thesis.Search_Thesis.Services.SearchSortServices.Sort.SortBy;
import com.Search_Thesis.Search_Thesis.Services.Utils.DocumentUtils;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrFolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ByDateDescending")
public class SortByDateDescending implements SortBy<FolderSolrSearch> {
    private SolrFolderRepository folderRepository ;
    @Autowired
    public void setFolderRepository(SolrFolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }
    @Override
    public List<FolderSolrSearch> sortWith(String standard, int pageNum) {
        String rows  = String.valueOf(DocumentUtils.rows(pageNum));
        String start =  String.valueOf(DocumentUtils.start(pageNum));

        return folderRepository.sortFolderResult(standard  ,rows , start, sortByDesc());
    }



    private Sort sortByDesc() {
        return Sort.by(Sort.Direction.DESC , "datePublic" ) ;
    }

}
