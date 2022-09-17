package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class Listen_to_Client extends Thread {

    private Socket socket ;

    private ArrayBlockingQueue<Listen_to_Client> arrayBlockingQueue  =  new ArrayBlockingQueue<>(3) ;

    private String message ;

    private PrintWriter outputWriter ;

    private BufferedReader inputReader ;

    public Listen_to_Client(Socket socket, ArrayBlockingQueue<Listen_to_Client> arrayBlockingQueue) {
        super();
        this.socket = socket;
        this.arrayBlockingQueue = arrayBlockingQueue;

    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ArrayBlockingQueue<Listen_to_Client> getArrayBlockingQueue() {
        return arrayBlockingQueue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setArrayBlockingQueue(ArrayBlockingQueue<Listen_to_Client> arrayBlockingQueue) {
        this.arrayBlockingQueue = arrayBlockingQueue;
    }

    public void Send_Message_to_Client(String messString) {

        try {
            PrintWriter	outputPrintWriter =  new PrintWriter(socket.getOutputStream(),true) ;

            outputPrintWriter.println(messString);

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void listen_from_Client() {
        try {
            inputReader =  new BufferedReader(
                    new InputStreamReader(socket.getInputStream())) ;

            outputWriter = new PrintWriter(socket.getOutputStream()) ;
            while(true ) {
                String inputString =  inputReader.readLine()  ;
                if(!inputString.isEmpty()) {
                    System.out.println(inputString);
                }
                else {
                    System.out.println("asd");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                System.out.println("client Exit");
                socket.close();
            } catch (Exception e2) {
                System.out.println(e2.getMessage());
            }
        }
    }
    @Override
    public void run() {
        listen_from_Client() ;
        super.run();
    }


}
