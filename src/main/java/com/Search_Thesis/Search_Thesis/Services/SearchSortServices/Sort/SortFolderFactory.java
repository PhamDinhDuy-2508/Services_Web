package com.Search_Thesis.Search_Thesis.Services.SearchSortServices.Sort;

import com.Search_Thesis.Search_Thesis.Model.SolrModels.FolderSolrSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("SortFolderFactory")
public class SortFolderFactory {
    private static SortBy<FolderSolrSearch> sortByAscendingByAlphabet;

    @Autowired
    @Qualifier("AscendingByAlphabet")
    private void setSortByAscendingByAlphabet(SortBy<FolderSolrSearch> sortByAscendingByAlphabet) {
        SortFolderFactory.sortByAscendingByAlphabet = sortByAscendingByAlphabet;
    }

    private static SortBy<FolderSolrSearch> sortByDescendingByAlphabet;

    @Autowired
    @Qualifier("ByDescendingByAlphabet")
    private void setSortByDescendingByAlphabet(SortBy<FolderSolrSearch> sortByAscendingByAlphabet) {
        SortFolderFactory.sortByDescendingByAlphabet = sortByAscendingByAlphabet;
    }

    private static SortBy<FolderSolrSearch> sortByDateAscending;

    @Autowired
    @Qualifier("ByDateAscending")
    private void setSolrSearchSortByDateAscending(SortBy<FolderSolrSearch> solrSearchSortByDateAscending) {
        SortFolderFactory.sortByDateAscending = solrSearchSortByDateAscending;
    }

    private static SortBy<FolderSolrSearch> searchByDateDescending;

    @Autowired
    @Qualifier("ByDateDescending")

    private void setSolrSearchSortByDescending(SortBy<FolderSolrSearch> solrSearchSortByDescending) {
        SortFolderFactory.searchByDateDescending = solrSearchSortByDescending;
    }


    private SortFolderFactory() {
    }

    public final SortBy<FolderSolrSearch> sortBy(SortStandardEnum standardEnum) {
        switch (standardEnum) {
            case A_Z -> {
                return sortByAscendingByAlphabet;
            }
            case Z_A -> {
                return sortByDescendingByAlphabet;
            }
            case Date_ASC -> {
                return sortByDateAscending;
            }
            case Date_DESC -> {
                return searchByDateDescending;
            }
            default -> {
                return null;
            }
        }
    }
}
