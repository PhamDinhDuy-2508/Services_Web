package com.Search_Thesis.Search_Thesis.Schedule;

import com.Search_Thesis.Search_Thesis.Redis_Model.Document_Service_redis;
import com.Search_Thesis.Search_Thesis.Redis_Model.Document_info_redis_Services;
import com.Search_Thesis.Search_Thesis.Services.Edit_Document_Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Component
@EnableAsync
public class Schedule_Expired {

    @Autowired
    Document_Service_redis document_service_redis ;

    @Autowired
    Document_info_redis_Services document_info_redis_services ;

    @Autowired
    Edit_Document_Services edit_document_services ;

    @Autowired
    Expire_Services expire_services ;

    @Async
    @Scheduled(fixedDelay = 60*3600*1000)
    public void check_Expired() throws IOException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime  ;

        Set<LocalDateTime> Time_Expired  = (Set<LocalDateTime>) document_info_redis_services.getHashKey("1_Expire");
        expire_services.Connect_to_Socket();
    }
}
