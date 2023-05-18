package com.Search_Thesis.Search_Thesis.Schedule;

import com.Search_Thesis.Search_Thesis.Services.RedisService.RedisServiceImpl.Document_Service_redis;
import com.Search_Thesis.Search_Thesis.Services.RedisService.RedisServiceImpl.Document_info_redis_Services;
import com.Search_Thesis.Search_Thesis.Services.CacheService.CacheServiceImpl.CacheManager_iml_PAGINATION_question;
import com.Search_Thesis.Search_Thesis.Services.Edit_Document_Services;
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
public class Schedule_Expired {

    @Autowired
    Document_Service_redis document_service_redis ;

    @Autowired
    Document_info_redis_Services document_info_redis_services ;

    @Autowired
    Edit_Document_Services edit_document_services ;

    @Autowired
    Expire_Services expire_services ;

    @Autowired
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
    @Scheduled(fixedRate = 100000 , initialDelay  = 50000)

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
//        expire_services.Delete(now);

    }
}
