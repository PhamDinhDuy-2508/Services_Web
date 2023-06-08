package com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService;

public interface Services_Redis<T, L> {
    T find(String haskey, String ID);

    T findProductById(String userid, int ID);

    void save_folder_ID(String ID, L elemment);
}
