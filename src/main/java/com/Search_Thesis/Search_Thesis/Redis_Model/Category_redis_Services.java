package com.Search_Thesis.Search_Thesis.Redis_Model;

import com.Search_Thesis.Search_Thesis.Model.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Service
public class Category_redis_Services implements Services_Redis<Category_Redis , List<Folder>> {
    private  final  String  HashKey = "Category" ;
    @Autowired
    RedisTemplate redisTemplate ;


    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }


    public Category_redis_Services() {
        super();
    }

    @Override
    public Category_Redis find(String haskey, String ID) {
        Category_Redis category_redis =  new Category_Redis() ;
        category_redis = (Category_Redis) redisTemplate.opsForHash().get(haskey ,  ID) ;
        return  category_redis;
    }

    @Override
    public Category_Redis findProductById(String userid, int ID) {
        return null;
    }

    @Override
    public Boolean deleteProduct(String user_id, int id) {
        return null;
    }
    @Override
    public void save_folder_ID(String ID, List<Folder> elemment) {
        Category_Redis category_redis =  new Category_Redis() ;

        LocalDateTime now = LocalDateTime.now();

        category_redis.setFolderList(elemment);

        redisTemplate.opsForHash().put(this.HashKey , ID , category_redis);
        redisTemplate.expire(ID , 3 , TimeUnit.DAYS) ;

    }
}
