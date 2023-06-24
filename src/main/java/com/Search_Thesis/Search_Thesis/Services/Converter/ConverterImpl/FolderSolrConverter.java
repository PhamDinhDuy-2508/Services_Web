package com.Search_Thesis.Search_Thesis.Services.Converter.ConverterImpl;

import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.SolrModels.FolderSolrSearch;
import com.Search_Thesis.Search_Thesis.Services.Converter.Converter;
import org.springframework.stereotype.Service;
@Service("FolderSolrConverter")
public class FolderSolrConverter extends Converter< Folder ,FolderSolrSearch> {

    public FolderSolrConverter() {
        super(FolderSolrConverter::convertFromEntity, FolderSolrConverter::convertFromDto);
    }

    public static Folder convertFromDto(final FolderSolrSearch dto) {
       return  null ;
    }

    public static FolderSolrSearch convertFromEntity(final Folder dto) {
        String[] autoSuggest = new String[5];
        autoSuggest[0] = dto.getCategorydocument().getRoot_folder().getName();
        autoSuggest[1] = dto.getTitle();
        autoSuggest[2] = dto.getCategorydocument().getName();
        autoSuggest[3] = dto.getCategorydocument().getCode();
        FolderSolrSearch folderSolrSearch = FolderSolrSearch.builder().id(dto.getIdFolder())
                .Folder(dto.getTitle())
                .Category(dto.getCategorydocument().getCode())
                .code(dto.getCategorydocument().getCode())
                .datePublic(dto.getPublish_date())
                .autoSuggest(autoSuggest).build();

        return folderSolrSearch;    }
}
