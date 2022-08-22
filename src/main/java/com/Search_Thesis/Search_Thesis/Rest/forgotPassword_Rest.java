package com.Search_Thesis.Search_Thesis.Rest;

import com.Search_Thesis.Search_Thesis.Event.sendEmail_Event;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.Services.ForgotPassword_Services;
import lombok.Data;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/forgot_password")


public class forgotPassword_Rest {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher ;

    @Autowired
    ForgotPassword_Services forgotPassword_services ;

    @PostMapping("/send_email")

    public void sendEmail(@RequestBody Gmail_response gmail_response , HttpServletRequest request) throws ExecutionException, InterruptedException {
        ExecutorService threadpool = Executors.newCachedThreadPool();
        String token = RandomString.make(45);

        Future<User> futureTask = threadpool.submit(() -> forgotPassword_services.checkUser(gmail_response.getEmail(),token));

        User result = futureTask.get();

        if (result == null){
            System.out.println("Null");
        }
        else {
            System.out.println( "Success") ;
            applicationEventPublisher.publishEvent(new sendEmail_Event(this,gmail_response.getEmail(), request , token));

        }
        threadpool.shutdown();
    }


}
@Data
class  Gmail_response{
    private String email ;
}