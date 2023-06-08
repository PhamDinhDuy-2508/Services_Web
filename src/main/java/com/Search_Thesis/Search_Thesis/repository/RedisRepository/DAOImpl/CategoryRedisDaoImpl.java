package com.Search_Thesis.Search_Thesis.repository.RedisRepository.DAOImpl;

import com.Search_Thesis.Search_Thesis.Model.CategoryRedis;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.repository.Category_document_Repository;
import com.Search_Thesis.Search_Thesis.repository.FolderRepository;
import com.Search_Thesis.Search_Thesis.repository.RedisRepository.RedisCommandDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service("CategoryRedisDao")
@Scope("prototype")
public class CategoryRedisDaoImpl implements RedisCommandDAO<CategoryRedis> {

    private  final String value = "category_redis" ;
    private Category_document_Repository categoryDocumentRepository;

    @Autowired
    public CategoryRedisDaoImpl(Category_document_Repository categoryDocumentRepository) {
        this.categoryDocumentRepository = categoryDocumentRepository;
    }

    private CategoryRedis categoryRedis;

    @Autowired
    public CategoryRedisDaoImpl(CategoryRedis categoryRedis) {
        this.categoryRedis = categoryRedis;
    }

    private FolderRepository folderRepository;

    @Autowired
    public CategoryRedisDaoImpl(FolderRepository  folderRepository) {
        this.folderRepository = folderRepository;
    }

    @Cacheable(value = value, key = "#ID")
    @Override
    public CategoryRedis finById(String ID) {
        List<Folder> folderList = folderRepository.findbyCode(ID);
        categoryRedis.setCode(ID);
        categoryRedis.setFolderList(folderList);
        return categoryRedis;
    }

    @Override
    @CachePut(value = value, key = "#CategoryID")
    public CategoryRedis saveById(String CategoryID) {
        Set<Folder> folderList = categoryDocumentRepository.findByCode(CategoryID)
                .getNewfolder();

        categoryRedis.setFolderList(new LinkedList<>(folderList));
        categoryRedis.setCode(CategoryID);
        return categoryRedis;
    }

    @Override
    @CacheEvict(value = value  ,  key = "#ID")
    public void Delete(String ID) {}

}
