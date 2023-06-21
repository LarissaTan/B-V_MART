package com.server.backup;

import java.io.IOException;
import java.net.Socket;

public class SocketConnectTest {
    //    链接本地54321server端口
    public static void main(String[] args) {
        String server = "localhost";
        int port = 54321;

        try {
            Socket socket = new Socket(server, port);
            socket.setSoTimeout(10000);
            System.out.println("Connected to server " + server + " on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
