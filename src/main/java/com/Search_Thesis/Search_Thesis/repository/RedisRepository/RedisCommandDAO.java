package com.Search_Thesis.Search_Thesis.repository.RedisRepository;

public interface RedisCommandDAO<Model> {
    Model finById( String ID);

    Model saveById(String ID);

    void Delete(String ID) ;

}
