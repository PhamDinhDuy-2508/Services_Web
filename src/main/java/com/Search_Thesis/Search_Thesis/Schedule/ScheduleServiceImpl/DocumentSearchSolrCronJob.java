package com.Search_Thesis.Search_Thesis.Schedule.ScheduleServiceImpl;

import com.Search_Thesis.Search_Thesis.Model.Document;
import com.Search_Thesis.Search_Thesis.Model.DocumentSolrSearch;
import com.Search_Thesis.Search_Thesis.Schedule.ScheduleService;
import com.Search_Thesis.Search_Thesis.Services.Converter.ConverterImpl.DocumentSearchSolrConverter;
import com.Search_Thesis.Search_Thesis.repository.Document_Repository;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrCommandDAO;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling

public class DocumentSearchSolrCronJob implements ScheduleService {

    @Autowired
    Document_Repository documentRepository;
    SolrCommandDAO<DocumentSolrSearch> solrSearchSolrCommandDAO;

    @Autowired
    public void setSolrSearchSolrCommandDAO(@Qualifier("DaoSolrDocument") SolrCommandDAO<DocumentSolrSearch> solrSearchSolrCommandDAO) {
        this.solrSearchSolrCommandDAO = solrSearchSolrCommandDAO;
    }

    @Autowired
    DocumentSearchSolrConverter documentSearchSolrConverter;

    @Autowired
    SolrDocumentRepository solrDocumentRepository;

    @Override
    @Async
    @Scheduled(fixedRate = 180000, initialDelay = 100000)

    public void CronJob() {
        List<Document> documents = documentRepository.findAll();

        if (solrDocumentRepository.findAll() != null) {
            for (Document document : documents) {
                solrDocumentRepository.save(documentSearchSolrConverter.convertFromDto(document));
            }
        } else {
            for (Document document : documents) {
                solrSearchSolrCommandDAO.merge(documentSearchSolrConverter.convertFromDto(document));
            }
        }
    }

}
