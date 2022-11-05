package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public interface  Cache_Services <T ,Y, U> {

    public  void Create_Cache(T object , U...value) ;
    public void  Delete_Cache(T object) ;
    public void Update_Cache(T object) ;
    public List<Y> get_Cache( U...value) ;

}
@Service
class Cache_Services_iml implements  Cache_Services<List<Question> ,Question, Integer> {
    @Autowired
    RedisTemplate redisTemplate ;

    private  final String key = "Question" ;

    public Cache_Services_iml() {
        super();
    }

    @Override
    public void Create_Cache(List<Question> object, Integer... value) {
        System.out.println(object);
        int size =  object.size() ;
        ArrayList<Question> list = new ArrayList<Question>(object.subList(0, size));
        System.out.println(list);


        redisTemplate.opsForHash().put(key ,  value ,  list);

    }

    @Override
    public void Delete_Cache(List<Question> object) {

        return ;
    }

    @Override
    public void Update_Cache(List<Question> object) {
    }

    public  List<Question> get_Cache(Integer... value) {
        System.out.println(redisTemplate.opsForHash().values(key));
        return (List<Question>) redisTemplate.opsForHash().get(key ,  value);
    }
}
