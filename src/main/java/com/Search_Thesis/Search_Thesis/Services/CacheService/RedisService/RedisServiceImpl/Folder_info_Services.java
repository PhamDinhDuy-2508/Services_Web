package com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService.RedisServiceImpl;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.FolderRedisModel;
import com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService.Services_Redis;
import com.Search_Thesis.Search_Thesis.Services.Converter.Converter;
import com.Search_Thesis.Search_Thesis.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class Folder_info_Services implements Services_Redis<FolderRedisModel, List<Document>> {

    private final String Hash_Key = "Delete_folder";

    @Autowired
    @Qualifier("DocumentRedisConverter")
    private Converter<Folder ,  FolderRedisModel> converter ;
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    FolderRepository folder_respository;

    @Override
    public FolderRedisModel find(String haskey, String ID) {
        return null;
    }

    @Override
    public FolderRedisModel findProductById(String userid, int ID) {
        FolderRedisModel folder_model_redis = new FolderRedisModel();
        folder_model_redis = (FolderRedisModel) redisTemplate.opsForHash().get(Hash_Key, userid);

        return folder_model_redis;

    }


    @Override
    public void save_folder_ID(String ID, List<Document> elemment) {

        Folder folder1 = folder_respository.findByIdFolder(Integer.parseInt(ID));

        String root_name = "Chuyen Nganh";

        String Category = folder1.getCategorydocument().getCode();


        String folder_name = folder1.getTitle();

        String url = "D:\\Data\\Document_data\\" + root_name + "\\" + Category + "\\" + folder_name;

        FolderRedisModel folder_model_redis = converter.convertFromDto(folder1);

        Expire(folder1, elemment, url);

        LocalDateTime localDateTime = LocalDateTime.now();

        String id = folder1.getContributor_ID() + "_" + localDateTime.toString();

        redisTemplate.opsForHash().put(this.Hash_Key, id, folder_model_redis);

        redisTemplate.expire(ID, 7, TimeUnit.DAYS);

    }
    public void Expire(Folder folder, List<Document> documents, String url) {
        LocalDateTime now = LocalDateTime.now();

        FolderRedisModel document_redis = new FolderRedisModel();

        document_redis = converter.convertFromDto(folder);


        redisTemplate.opsForHash().put("1_Expire", now, document_redis);
        

        redisTemplate.opsForHash().values("1_Expire");
    }

    public FolderRedisModel findByTime(LocalDateTime localDateTime) {
        FolderRedisModel folder_model_redis = (FolderRedisModel) redisTemplate.opsForHash().get("1_Expire", localDateTime);
        return folder_model_redis;
    }

    public void Delete_Expired_Data(LocalDateTime localDateTime) {
        redisTemplate.opsForHash().delete("1_Expire", localDateTime);
    }


}
