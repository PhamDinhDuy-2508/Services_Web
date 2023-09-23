package com.Search_Thesis.Search_Thesis.Schedule;

import com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService.RedisServiceImpl.Document_Service_redis;
import com.Search_Thesis.Search_Thesis.Services.CacheService.RedisService.RedisServiceImpl.Document_info_redis_Services;
import com.Search_Thesis.Search_Thesis.Services.CacheService.CacheServiceImpl.CacheManager_iml_PAGINATION_question;
import com.Search_Thesis.Search_Thesis.Services.DocumentService.EditDocumentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Component
@EnableAsync
@EnableScheduling
public class ScheduleExpired {

    Document_Service_redis document_service_redis ;
    Document_info_redis_Services document_info_redis_services ;
    EditDocumentServices edit_document_services ;
    ExpireServices expire_services ;
    CacheManager_iml_PAGINATION_question cacheManager_iml_pagination_question ;


    private  Thread thread  ;

    public void processing_update_Cache() {
        Runnable task_update_question =()->{
            cacheManager_iml_pagination_question.add_Ques_into_Cache() ;
        }  ;
       thread = new Thread(task_update_question)  ;
       thread.start();
    }

    @Async
//    @Scheduled(fixedRate = 100000 , initialDelay  = 50000)

    public void Update_Cache() throws InterruptedException {
        if(thread != null) {
            this.thread.join();
        }
        processing_update_Cache();
    }
    @Async
    @Scheduled(fixedDelay = 60*3600*1000)
    public void check_Expired() throws IOException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime  ;

        Set<LocalDateTime> Time_Expired  = (Set<LocalDateTime>) document_info_redis_services.getHashKey("1_Expire");
    }
    @Autowired
    public void setDocument_service_redis(Document_Service_redis document_service_redis) {
        this.document_service_redis = document_service_redis;
    }
    @Autowired
    public void setDocument_info_redis_services(Document_info_redis_Services document_info_redis_services) {
        this.document_info_redis_services = document_info_redis_services;
    }
    @Autowired
    public void setEdit_document_services(EditDocumentServices edit_document_services) {
        this.edit_document_services = edit_document_services;
    }
    @Autowired
    public void setExpire_services(ExpireServices expire_services) {
        this.expire_services = expire_services;
    }
    @Autowired
    public void setCacheManager_iml_pagination_question(CacheManager_iml_PAGINATION_question cacheManager_iml_pagination_question) {
        this.cacheManager_iml_pagination_question = cacheManager_iml_pagination_question;
    }
}
