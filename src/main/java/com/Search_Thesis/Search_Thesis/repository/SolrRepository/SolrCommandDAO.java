package com.Search_Thesis.Search_Thesis.repository.SolrRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
public interface SolrCommandDAO<T> {
    List<T> getAll();
    Optional<T> get(int id);
    @Transactional
    void delete(T t);
    @Transactional
    void merge(T t);
    @Transactional
    void Persist(T t) ;
    public List<T> getByCode(String value) ;


    }
