package com.server;

import com.example.bv_mart.bean.chatObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class commentHandler implements Runnable{

    public static ArrayList<commentHandler> array = new ArrayList<>();//to broadcast to all customers
    private Socket socket;
    private ObjectInputStream reader;   // replace buffered reader
    private ObjectOutputStream writer;  // replace buffered writer

    public commentHandler(Socket socket){
        try{
            this.socket = socket;
            this.reader = new ObjectInputStream(socket.getInputStream());
            this.writer = new ObjectOutputStream(socket.getOutputStream());
            array.add(this);
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
        for(commentHandler a: array){
            try{
                a.writer.writeObject(msg);
                a.writer.flush();
            } catch (IOException e) {
                closeEverything(socket,reader,writer);
            }
        }
    }

    public void removeCommentHandler(){
        array.remove(this);
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
