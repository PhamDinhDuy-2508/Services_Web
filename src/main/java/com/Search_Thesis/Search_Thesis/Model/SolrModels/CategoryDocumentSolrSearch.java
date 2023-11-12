package com.Search_Thesis.Search_Thesis.Model.SolrModels;

import com.google.common.base.Objects;
import lombok.*;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(collection = "categorySearch")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CategoryDocumentSolrSearch {
    @Id
    @Field
    private int id;
    @Field
    private String[] autoSuggest_index;
    @Field
    private String name;
    @Field
    private String code;
    @Field
    private int root;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDocumentSolrSearch that = (CategoryDocumentSolrSearch) o;
        return id == that.id && root == that.root && Objects.equal(autoSuggest_index, that.autoSuggest_index) && Objects.equal(name, that.name) && Objects.equal(code, that.code);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(id, autoSuggest_index, name, code, root);
    }
}
