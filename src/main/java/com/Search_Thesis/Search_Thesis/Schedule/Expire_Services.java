package com.Search_Thesis.Search_Thesis.Schedule;

import com.Search_Thesis.Search_Thesis.Model.Question;
import com.Search_Thesis.Search_Thesis.Redis_Model.Document_info_redis;
import com.Search_Thesis.Search_Thesis.Redis_Model.Document_info_redis_Services;
import com.Search_Thesis.Search_Thesis.Redis_Model.Folder_info_Services;
import com.Search_Thesis.Search_Thesis.Redis_Model.Folder_model_redis;
import com.Search_Thesis.Search_Thesis.Server_Service.Message_to_Server;
import com.Search_Thesis.Search_Thesis.Services.Edit_Document_Services;
import com.Search_Thesis.Search_Thesis.resposity.Question_Repository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

@Service
public class Expire_Services {
    @Autowired
    Document_info_redis_Services document_info_redis_services ;

    @Autowired
    Edit_Document_Services edit_document_services ;

    @Autowired
    Folder_info_Services folder_info_services ;
    private  Message_to_Server  message_to_server ;

    @Autowired
    RedisTemplate redisTemplate ;
    @Autowired
    Folder_model_redis folder_model_redis ;

    @Autowired
    Question_Repository question_repository ;

    @Autowired
    CacheManager cacheManager ;


    private  Socket socket ;

    @PostConstruct
    public void Create_Connection() throws IOException {
//        Socket socket =  new Socket("localhost" , 2508) ;
        message_to_server = new Message_to_Server(socket);
    }

    public void Delete(LocalDateTime now) throws IOException {

        Set<LocalDateTime>  localDateTimes = (Set<LocalDateTime>) document_info_redis_services.getHashKey("1_Expire");

        List<LocalDateTime>  list  =  localDateTimes.stream().toList();
        if(list.size() != 0 ) {
            while( now.isAfter(list.get(0))) {
                LocalDateTime test = list.get(0);

                Document_info_redis document_info_redis = document_info_redis_services.findByTime(test);

                String message = Create_Json_Document(document_info_redis);


                list.remove(list.get(0));

                Connect_to_Socket();

                message_to_server.setMessageString(message);
                message_to_server.Send_Message_to_Server();
                document_info_redis_services.Delete_Expired_Data(test);

                if (list.size() == 0) {
                    break;
                }
        }


        }

    }
    public List<Question> load_all() {
        return question_repository.findAll() ;
    }


    @Async
    @Scheduled(fixedRate = 30000)
    public void Update_Cache_Question_list() {



    }
    public void Connect_to_Socket() throws IOException {
            Create_test();
    }

    public String Create_Json_Document(Document_info_redis document_info_redis) {
        Gson gson =  new Gson() ;
        document_info_redis.setID("Delete_document");
        String json =  gson.toJson(document_info_redis) ;
        return json ;
    }
    public String Create_Json_Folder(Folder_model_redis folder_model_redis) {
        Gson gson =  new Gson() ;
        String json =  gson.toJson(folder_model_redis) ;
        return json ;
    }
    public void Create_test() {

        Set<LocalDateTime>  localDateTimes = (Set<LocalDateTime>) document_info_redis_services.getHashKey("1_Expire");

        Queue<LocalDateTime> queue = new LinkedList<>(localDateTimes) ;


        while (queue.size() != 0 )
           try  {

               Document_info_redis document_info_redis =  document_info_redis_services.findByTime(queue.peek()) ;

               String message = Create_Json_Document(document_info_redis) ;

               message_to_server.setMessageString( message);
               System.out.println(document_info_redis);
               message_to_server.Send_Message_to_Server();

               queue.remove() ;

           }
           catch (Exception e) {
               Folder_model_redis folder_model_redis1 =  folder_info_services.findByTime(queue.peek());
               String message = Create_Json_Folder(folder_model_redis1) ;

               message_to_server.setMessageString( message);
               message_to_server.Send_Message_to_Server();


               queue.remove() ;

               continue;


           }

       }


//


    }


