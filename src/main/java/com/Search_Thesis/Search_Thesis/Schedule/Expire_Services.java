package com.Search_Thesis.Search_Thesis.Schedule;

import com.Search_Thesis.Search_Thesis.Redis_Model.Document_info_redis;
import com.Search_Thesis.Search_Thesis.Redis_Model.Document_info_redis_Services;
import com.Search_Thesis.Search_Thesis.Server_Service.Message_to_Server;
import com.Search_Thesis.Search_Thesis.Services.Edit_Document_Services;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
public class Expire_Services {
    @Autowired
    Document_info_redis_Services document_info_redis_services ;

    @Autowired
    Edit_Document_Services edit_document_services ;
    private  Message_to_Server  message_to_server ;
    private  Socket socket ;

    @PostConstruct
    public void Create_Connection() throws IOException {
        Socket socket =  new Socket("localhost" , 2508) ;
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

                message_to_server.setMessageString(message);

                list.remove(list.get(0));

                Connect_to_Socket();

                document_info_redis_services.Delete_Expired_Data(test);

                if (list.size() == 0) {
                    break;
                }
        }


        }

    }
    public void Connect_to_Socket() throws IOException {

            Create_test();
            message_to_server.Send_Message_to_Server();
    }

    public String Create_Json_Document(Document_info_redis document_info_redis) {
        Gson gson =  new Gson() ;
        document_info_redis.setID("Delete_document");
        String json =  gson.toJson(document_info_redis) ;
        return json ;
    }
    public void Create_test() {

       Set<LocalDateTime>  localDateTimes = (Set<LocalDateTime>) document_info_redis_services.getHashKey("1_Expire");

       List<LocalDateTime>  list  =  localDateTimes.stream().toList();
        System.out.println(list);
       for (int i = 0; i < list.size() ; i++) {
           try  {
               LocalDateTime localDateTime  ;

               DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

                String formattedDateTime = list.get(i).format(formatter);
               Document_info_redis document_info_redis =  document_info_redis_services.findByTime(list.get(i)) ;

               String message = Create_Json_Document(document_info_redis) ;

               message_to_server.setMessageString(message);


           }
           catch (Exception e) {
               System.out.println(e.getMessage());
               System.out.println(list.get(i).toLocalDate());
//               document_info_redis_services.Delete_Expired_Data(list.get(i));

//               document_info_redis_services.deleteProduct(list.get(i).toString() , 2) ;

               continue;



           }

       }

//


    }

}
