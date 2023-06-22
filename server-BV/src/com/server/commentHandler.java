package com.server;

import com.example.bv_mart.bean.chatObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class commentHandler implements Runnable{

    //public static ArrayList<commentHandler> array = new ArrayList<>();//to broadcast to all customers
    public static List<commentHandler> array = new CopyOnWriteArrayList<>();
    private Socket socket;
    private ObjectInputStream reader;   // replace buffered reader
    private ObjectOutputStream writer;  // replace buffered writer

    public commentHandler(Socket socket){
        try{
            this.socket = socket;
            this.reader = new ObjectInputStream(socket.getInputStream());
            this.writer = new ObjectOutputStream(socket.getOutputStream());
            synchronized (array) {
                array.add(this);
            }
        }catch (IOException e){
            closeEverything(socket,reader,writer);
        } 
    }

    @Override   // run on a separate thread
    public void run() {
        chatObject msg;

        while(socket.isConnected()){
            try{
                msg = (chatObject) reader.readObject();
                boardcast(msg);
            } catch (IOException | ClassNotFoundException e) {
                closeEverything(socket,reader,writer);
                break;
            }
        }

    }

    public void boardcast(chatObject msg){
        synchronized (array) {
            for (commentHandler a : array) {
                System.out.println("here and array is : " + a);
                try {
                    if (!a.socket.isClosed()) {
                        System.out.println(msg.msg);
                        a.writer.writeObject(msg);
                        System.out.println("work~~");
                        a.writer.flush();
                    }
                } catch (IOException e) {
                    closeEverything(socket, reader, writer);
                }
            }
        }
    }

    public void removeCommentHandler(){
        synchronized (array) {
            array.remove(this);
        }
    }

    public void closeEverything(Socket socket, ObjectInputStream reader, ObjectOutputStream writer){
        removeCommentHandler();
        try{
            if(reader != null)  reader.close();
            if(writer != null)  writer.close();
            if(socket != null)  socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
