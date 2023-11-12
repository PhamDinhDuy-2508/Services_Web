package com.Search_Thesis.Search_Thesis.Model.SolrModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(collection = "DocumentSearch")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentSolrSearch {
    @Id
    @Field
    private  int id;
    @Field
    private String[] autoSuggest_index;
    @Field
    private String content;

    @Field
    private String folderId ;


}
