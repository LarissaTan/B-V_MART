package com.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CustomerFileServer {
    private static final Object fileLock = new Object();

    public static void main(String[] args) {
        CustomerFileServer server = new CustomerFileServer();
        server.startServer(8080);
    }

    public void startServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                // 创建一个新线程来处理客户端请求
                Thread clientThread = new Thread(() -> handleClientRequest(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readCustomerFile() {
        synchronized (fileLock) {
            try {
                File file = new File("customer.txt");
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();
                return content.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "Error reading file";
            }
        }
    }

    private boolean authenticateUser(){
        return false;
    }

    private void writeCustomerFile(String data) {
        synchronized (fileLock) {
            try {
                File file = new File("customer.txt");
                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                writer.write(data);
                writer.newLine();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleClientRequest(Socket clientSocket) {
        try {
            // 获取客户端请求的输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // 获取客户端响应的输出流
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            String request;
            while ((request = reader.readLine()) != null) {
                if (request.equals("READ")) {
                    String response = readCustomerFile();
                    writer.write(response);
                    writer.newLine();
                    writer.flush();
                } else if (request.equals("WRITE")) {
                    writeCustomerFile("New customer information");
                    writer.write("File updated successfully");
                    writer.newLine();
                    writer.flush();
                } else if (request.equals("EXIT")) {
                    break;
                }
            }
            // 关闭客户端连接
            reader.close();
            writer.close();
            clientSocket.close();
            System.out.println("Client disconnected: " + clientSocket.getInetAddress().getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
