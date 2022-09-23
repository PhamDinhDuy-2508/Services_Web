package com.Search_Thesis.Search_Thesis.Server_Service;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
public class Message_to_Server extends  Thread {

    private Socket socket  ;

    private String messageString ;

    private PrintWriter printWriter ; // output

    private BufferedReader bufferedReader ; // send message ;

    public Message_to_Server(Socket socket) {
        super();
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getMessageString() {
        return messageString;
    }

    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public void setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public void Send_Message_to_Server() {
        try {
            OutputStream ouputOutputStream  =  socket.getOutputStream() ;
            printWriter = new PrintWriter(ouputOutputStream ,  true) ;
            printWriter.println(this.messageString);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public synchronized void run() {
        Send_Message_to_Server();
        super.run();
    }


}
