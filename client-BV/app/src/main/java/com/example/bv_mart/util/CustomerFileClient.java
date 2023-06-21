package com.example.bv_mart.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class CustomerFileClient {
    static Socket socket;
    static BufferedWriter writer;
    static BufferedReader reader;

    public CustomerFileClient() {
        start();
    }

    public static void start() {
        String serverAddress = "localhost";
        int serverPort = 54321;

        try {
            socket = new Socket(serverAddress, serverPort);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    //    close connection
    public static void close() {
        try {
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    login request
    public static boolean login(String username, String password) {
        try {
            writer.write("LOGIN " + username + " " + password);
            writer.newLine();
            writer.flush();

            // 接收服务端的响应
            String response = reader.readLine();
            System.out.println("Server response: " + response);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //    start connection


}
