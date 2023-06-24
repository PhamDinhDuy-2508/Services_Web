package com.Search_Thesis.Search_Thesis.Services.Converter.ConverterImpl;

import com.Search_Thesis.Search_Thesis.DTO.FolderResponse;
import com.Search_Thesis.Search_Thesis.Model.SolrModels.FolderSolrSearch;
import com.Search_Thesis.Search_Thesis.Services.Converter.Converter;
import com.Search_Thesis.Search_Thesis.Services.Utils.DocumentUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("FolderEntityConvertToDto")
public class FolderSolDtoConverter extends Converter< FolderSolrSearch ,FolderResponse> {


    public FolderSolDtoConverter() {
        super(FolderSolDtoConverter::convertFromEntity, FolderSolDtoConverter::convertFromDto);
    }

    public static FolderSolrSearch convertFromDto(final FolderResponse dto) {
        return  null ;
    }

    public static FolderResponse convertFromEntity(final FolderSolrSearch entity) {
        return FolderResponse.builder().folderName(entity.getFolder())
                .id(entity.getId())
                .datePublic(DocumentUtils.convertToFormatYYMMDD(entity.getDatePublic()))
                .code(entity.getCode())
                .build();
    }

    public static String convertFromEntityListType(final  List<FolderSolrSearch> listEntity) {
          List<FolderResponse> list = listEntity.stream().map(entity -> {
            FolderResponse dto = convertFromEntity(entity);
            return dto;
        }).toList();
          return  null ;
    }

}
