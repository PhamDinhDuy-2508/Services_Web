package com.Search_Thesis.Search_Thesis.Services.Converter.ConverterImpl;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.FolderRedisModel;
import com.Search_Thesis.Search_Thesis.Services.Converter.Converter;
import com.Search_Thesis.Search_Thesis.repository.Document_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
@Service("DocumentRedisConverter")
public class DocumentRedisConverter extends Converter<Folder , FolderRedisModel> {
    @Autowired
    Document_Repository documentRepository ;

    public DocumentRedisConverter() {
        super(DocumentRedisConverter::convertFromEntity , DocumentRedisConverter::convertFromDto);
    }

    private static Folder convertFromDto(FolderRedisModel folderRedisModel) {


        return null ;
    }

    private static FolderRedisModel convertFromEntity(Folder folder) {

        FolderRedisModel folderRedisModel =  new FolderRedisModel() ;
        folderRedisModel.setIdFolder(folder.getIdFolder());  ;
        folderRedisModel.setTitle(folder.getTitle());
        folderRedisModel.setCategorydocument(folder.getCategorydocument());
        folderRedisModel.setContributor_ID(folder.getContributor_ID());
        folderRedisModel.setDocumentList((List<Document>) folder.getDocuments());

        return folderRedisModel ;
    }

    public DocumentRedisConverter(Function<Folder, FolderRedisModel> fromDto, Function<FolderRedisModel, Folder> fromEntity) {
        super(fromDto, fromEntity);
    }



}
