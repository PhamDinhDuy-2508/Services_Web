package com.Search_Thesis.Search_Thesis.Schedule.ScheduleServiceImpl;

import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Model.SolrModels.FolderSolrSearch;
import com.Search_Thesis.Search_Thesis.Schedule.ScheduleService;
import com.Search_Thesis.Search_Thesis.Services.Converter.Converter;
import com.Search_Thesis.Search_Thesis.repository.FolderRepository;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrCommandDAO;
import com.Search_Thesis.Search_Thesis.repository.SolrRepository.SolrFolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("FolderSearchSolrCronJob")
@EnableScheduling
public class FolderSearchSolrCronJob implements ScheduleService {

    @Autowired
    FolderRepository folder_respository;

    @Autowired
    SolrFolderRepository solrFolderRepository;

    @Autowired
    @Qualifier("DaoImplSolrFolder")
    SolrCommandDAO<FolderSolrSearch> folderSolrSearchSolrCommandDAO;

    @Autowired
    @Qualifier("FolderSolrConverter")
    Converter<Folder, FolderSolrSearch> converter;

    @Async
//    @Scheduled(fixedRate = 180000, initialDelay =  100000)
    @Override
    public void CronJob() {
        List<Folder> folderList = folder_respository.findAll();
        if (solrFolderRepository.findAll() != null) {
            for (Folder folder : folderList) {
                solrFolderRepository.save(converter.convertFromDto(folder));
            }
        } else {
            folderList.stream().forEach(folder -> {
                folderSolrSearchSolrCommandDAO.merge(converter.convertFromDto(folder));
            });
        }
    }
}
