package com.Search_Thesis.Search_Thesis.Model.SolrModels;

import com.google.common.base.Objects;
import lombok.*;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.Id;
import java.util.Date;

@SolrDocument(collection = "FolderSearch2")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FolderSolrSearch {
    @Id
    @Field
    private int id;
    @Field

    @Indexed(name = "category" , type = "string" , searchable = false, stored = false)

    private String Category;
    @Field
    @Indexed(type = "string" , name = "code" , searchable = true,stored = false)
    private String code;
    @Field
    @Indexed(type = "string" , name = "folder" , searchable = true,stored = false)
    private String Folder;
    @Field
    private String[] autoSuggest;

    @Field
    private Date datePublic ;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FolderSolrSearch that = (FolderSolrSearch) o;
        return id == that.id &&  Objects.equal(Category, that.Category) && Objects.equal(code, that.code) && Objects.equal(Folder, that.Folder) && Objects.equal(autoSuggest, that.autoSuggest);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, Category, code, Folder, autoSuggest);
    }
}
