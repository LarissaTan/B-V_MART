package com.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class commentServer {
    private ServerSocket serverSocket;

    public commentServer(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{
             while(!serverSocket.isClosed()){
                 Socket socket = serverSocket.accept();//waiting here still connect
                 System.out.println("a new client is connected to chat room!");
                 commentHandler handler = new commentHandler(socket);

                 Thread thread = new Thread(handler);
                 thread.start();
             }
        }catch (IOException e){

        }
    }

    public void closeServerSocket() {
        try{
            if(serverSocket!=null)  serverSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        commentServer cs = new commentServer(serverSocket);
        cs.startServer();
    }
}
