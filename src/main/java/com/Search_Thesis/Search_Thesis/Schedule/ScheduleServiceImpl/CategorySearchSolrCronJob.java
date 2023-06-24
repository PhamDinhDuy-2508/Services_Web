package com.Search_Thesis.Search_Thesis.Schedule.ScheduleServiceImpl;

import com.Search_Thesis.Search_Thesis.Model.SolrModels.CategoryDocumentSolrSearch;
import com.Search_Thesis.Search_Thesis.Model.Category_document;
import com.Search_Thesis.Search_Thesis.Schedule.ScheduleService;
import com.Search_Thesis.Search_Thesis.Services.Converter.Converter;
import com.Search_Thesis.Search_Thesis.repository.Category_document_Repository;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrCategoryDocumentRepository;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrCommandDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
public class CategorySearchSolrCronJob implements ScheduleService {
    private Category_document_Repository categoryDocumentRepository;
    Converter<Category_document, CategoryDocumentSolrSearch> converter;

    @Autowired
    public CategorySearchSolrCronJob(@Qualifier("CategorySearchSolrConverter")Converter<Category_document, CategoryDocumentSolrSearch> converter) {
        this.converter = converter;
    }



    @Autowired
    public void setCategoryDocumentRepository(Category_document_Repository categoryDocumentRepository) {
        this.categoryDocumentRepository = categoryDocumentRepository;
    }

    private SolrCommandDAO<CategoryDocumentSolrSearch> solrSearchSolrCommandDAO;

    @Autowired
    public void setSolrSearchSolrCommandDAO(SolrCommandDAO<CategoryDocumentSolrSearch> solrSearchSolrCommandDAO) {
        this.solrSearchSolrCommandDAO = solrSearchSolrCommandDAO;
    }

    private SolrCategoryDocumentRepository solrCategoryDocumentRepository;

    @Autowired
    public void setSolrCategoryDocumentRepository(SolrCategoryDocumentRepository solrCategoryDocumentRepository) {
        this.solrCategoryDocumentRepository = solrCategoryDocumentRepository;
    }

    @Async
    @Scheduled(fixedRate = 180000, initialDelay = 100000)
    @Override
    public void CronJob() {
        List<Category_document> categoryDocumentSolrSearch = categoryDocumentRepository.findAll();
        if (solrCategoryDocumentRepository.findAll() != null) {
            for (Category_document document : categoryDocumentSolrSearch) {
                solrCategoryDocumentRepository.save(converter.convertFromDto(document));
            }
        } else {
            categoryDocumentSolrSearch.stream().forEach(categoryDocument -> {
                solrSearchSolrCommandDAO.merge(converter.convertFromDto(categoryDocument));
            });
        }
    }
}
