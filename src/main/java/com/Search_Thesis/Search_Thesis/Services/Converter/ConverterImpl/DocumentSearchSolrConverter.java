package com.Search_Thesis.Search_Thesis.Services.Converter.ConverterImpl;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.DocumentSolrSearch;
import com.Search_Thesis.Search_Thesis.Services.Converter.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.function.Function;

@Service("DocumentSearchSolrConverter")
public class DocumentSearchSolrConverter extends Converter<Document, DocumentSolrSearch> {
    Logger logger = LoggerFactory.getLogger(DocumentSearchSolrConverter.class);

    private static final String Bracket = "[\\[\\](){},]" ;

    public DocumentSearchSolrConverter() {
        super(DocumentSearchSolrConverter::convertFromEntity, DocumentSearchSolrConverter::convertFromDto);
    }

    public DocumentSearchSolrConverter(Function<Document, DocumentSolrSearch> fromDto, Function<DocumentSolrSearch, Document> fromEntity) {
        super(fromDto, fromEntity);
    }

    public static DocumentSolrSearch convertFromEntity(final Document entity) {

        String[] suggestIndexContent  =  entity.getTitle().split("[-._\\s+]") ;
        String[] suggestIndexAuthor  ;
        String[] suggestIndex = new String[2] ;

        suggestIndex[0] =  Arrays.toString(suggestIndexContent).replaceAll(Bracket,"") ;

        if(entity.getAuthor() != null) {
            suggestIndexAuthor = entity.getAuthor().split("[-._\\s+]");
            suggestIndex[1]  = Arrays.toString(suggestIndexAuthor).replaceAll(Bracket,"");
        }

        else {
            suggestIndex[1] = "" ;
        }
        DocumentSolrSearch documentSolrSearch = DocumentSolrSearch.builder().id(entity.getID()).content(entity.getTitle()).autoSuggest_index(suggestIndex).folderId(String.valueOf(entity.getId_folder())).build();
        return documentSolrSearch;
    }
    public static Document convertFromDto(final DocumentSolrSearch entity) {
     return  null ;
    }

}