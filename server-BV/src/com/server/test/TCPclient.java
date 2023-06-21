package com.server.test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPclient {

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;

        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;


        //establish socket connection to server
        socket = new Socket(host.getHostName(), 16800);
        //write to socket using ObjectOutputStream
        oos = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Sending request to Socket Server");

        String username = "jack";
        String password = "123";
        oos.writeObject("register" + "@" + username + "@" + password);
//        oos.writeObject("login" + "@" + username + "@" + password);
//        oos.writeObject("unlock" + "@" + username);
        //read the server response message

        ois = new ObjectInputStream(socket.getInputStream());
        String message = (String) ois.readObject();
        String[] split = message.split("@");
        System.out.println("Message#: " + message);
        System.out.println(message);

        //close resources

        ois.close();
        oos.close();

        Thread.sleep(100);

    }

}
