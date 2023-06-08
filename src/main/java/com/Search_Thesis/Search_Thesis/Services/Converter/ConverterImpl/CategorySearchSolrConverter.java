package com.Search_Thesis.Search_Thesis.Services.Converter.ConverterImpl;

import com.Search_Thesis.Search_Thesis.Model.CategoryDocumentSolrSearch;
import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Services.Converter.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service("CategorySearchSolrConverter")
public class CategorySearchSolrConverter extends Converter<Category_document, CategoryDocumentSolrSearch> {
    private static Logger logger = LoggerFactory.getLogger(CategorySearchSolrConverter.class);


    public CategorySearchSolrConverter() {
        super(CategorySearchSolrConverter::convertFromEntity, CategorySearchSolrConverter::convertFromDto);
    }

    public CategorySearchSolrConverter(Function<Category_document, CategoryDocumentSolrSearch> fromDto, Function<CategoryDocumentSolrSearch, Category_document> fromEntity) {
        super(fromDto, fromEntity);
    }

    public static CategoryDocumentSolrSearch convertFromEntity(final Category_document entity) {
        try {
            String[] suggestIndexContent = new String[3];
            suggestIndexContent[1] = entity.getName();
            suggestIndexContent[0] = entity.getCode();
            suggestIndexContent[2] = entity.getRoot_folder().getName();

            CategoryDocumentSolrSearch categoryDocumentSolrSearch = CategoryDocumentSolrSearch.builder()
                    .name(entity.getName())
                    .id(entity.getCategory_id())
                    .code(entity.getCode())
                    .root(entity.getRoot_folder().getId()).autoSuggest_index(suggestIndexContent).
                    build();

            return categoryDocumentSolrSearch;

        } catch (Exception e) {
            logger.error("1 field is null");
            return null;
        }
    }

    public static Category_document convertFromDto(final CategoryDocumentSolrSearch entity) {
        return  null ;
    }
}
