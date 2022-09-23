package Server;

import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public  class Document_Server {

    public static void main(String [] argStrings ) {

        ExecutorService executorService =  Executors.newFixedThreadPool(3) ;
        ArrayBlockingQueue<Listen_to_Client> listen_to_clients  = new ArrayBlockingQueue<>(3) ;

        try {
            ServerSocket serverSocket  =  new ServerSocket(2508) ;

            System.out.println("Server is ready");


            Scanner scanner = new Scanner(System.in) ;


            Listen_to_Client listen_to_client =  new Listen_to_Client(serverSocket.accept(), listen_to_clients);
            executorService.execute(listen_to_client);


            while(true ) {

                String uString = scanner.nextLine() ;

                if(!uString.isEmpty()) {
                    listen_to_client.Send_Message_to_Client(uString);
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

}