package com.Search_Thesis.Search_Thesis.Services.SearchSortServices.Sort.SortServiceImpl;

import com.Search_Thesis.Search_Thesis.DTO.FolderResponse;
import com.Search_Thesis.Search_Thesis.Model.SolrModels.FolderSolrSearch;
import com.Search_Thesis.Search_Thesis.Services.Converter.Converter;
import com.Search_Thesis.Search_Thesis.Services.SearchSortServices.Sort.SortBy;
import com.Search_Thesis.Search_Thesis.Services.SearchSortServices.Sort.SortFolderFactory;
import com.Search_Thesis.Search_Thesis.Services.SearchSortServices.Sort.SortService;
import com.Search_Thesis.Search_Thesis.Services.SearchSortServices.Sort.SortStandardEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
@Service("SortService")
public class SortServiceImpl implements SortService<FolderSolrSearch> {


    private Converter<FolderSolrSearch , FolderResponse> converter ;
    @Autowired
    @Qualifier("FolderEntityConvertToDto")

    public void setConverter(Converter<FolderSolrSearch, FolderResponse> converter) {
        this.converter = converter;
    }


    private SortFolderFactory sortFolderFactory ;
    @Autowired
    @Qualifier("SortFolderFactory")
    public void setSortFolderFactory(SortFolderFactory sortFolderFactory) {
        this.sortFolderFactory = sortFolderFactory;
    }

    @Override
    public SortBy<FolderSolrSearch> setOption(String Case) {
        try {
            switch (Case) {
                case "AZ": {
                    return sortFolderFactory.sortBy(SortStandardEnum.A_Z)  ;
                }
                case "ZA": {
                   return   sortFolderFactory.sortBy(SortStandardEnum.Z_A) ;
                }
                case "DateAsc" :  {
                    return  sortFolderFactory.sortBy(SortStandardEnum.Date_ASC) ;
                }
                case "DateDesc" : {
                    return  sortFolderFactory.sortBy(SortStandardEnum.Date_DESC) ;
                }
                default: {
                    return sortFolderFactory.sortBy(SortStandardEnum.A_Z)  ;
                }
            }
        } catch (Exception ignored) {
            return null ;
        }
    }


}
