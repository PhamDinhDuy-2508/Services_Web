package com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService.RedisServiceImpl;

import com.Search_Thesis.Search_Thesis.Model.CategoryRedis;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService.Services_Redis;
import com.Search_Thesis.Search_Thesis.repository.Category_document_Repository;
import com.Search_Thesis.Search_Thesis.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Service
public class Category_redis_Services implements Services_Redis<CategoryRedis, List<Folder>> {
    private  final  String  HashKey = "Category" ;
    @Autowired
    RedisTemplate redisTemplate ;
    @Autowired
    CacheManager cacheManager ;
    @Autowired
    Category_document_Repository category_document_responsitory ;
    @Autowired
    FolderRepository folder_respository ;


    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }



    public Category_redis_Services() {
        super();
    }
    @Cacheable(value = "category_redis", key = "#ID")


    @Override
    public CategoryRedis find(String haskey, String ID) {

        CategoryRedis category_redis =  new CategoryRedis() ;
        List<Folder> list =  folder_respository.findbyCode(ID) ;
        category_redis.setCode(ID);
        category_redis.setFolderList(list);


        return  category_redis;
    }

    @Override
    public CategoryRedis findProductById(String userid, int ID) {
        return null;
    }

    public Boolean deleteProduct(String user_id, int id) {
        return null;
    }
    @Override
    public void save_folder_ID(String ID, List<Folder> elemment) {
        CategoryRedis category_redis =  new CategoryRedis() ;

        LocalDateTime now = LocalDateTime.now();

        category_redis.setFolderList(elemment);

        redisTemplate.opsForHash().put(this.HashKey , ID , category_redis);
        redisTemplate.expire(ID , 3 , TimeUnit.DAYS) ;

    }
}
