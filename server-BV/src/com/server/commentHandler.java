package com.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class commentHandler implements Runnable{

    public static ArrayList<commentHandler> array = new ArrayList<>();//to broadcast to all customers
    private Socket socket;
    private ObjectInputStream reader;   // replace buffered reader
    private ObjectOutputStream writer;  // replace buffered writer

    public commentHandler(Socket socket){
        
    }

    @Override
    public void run() {

    }
}
