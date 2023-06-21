package com.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class commentRemote {
    private static final String FILE_PATH = "comment.txt";
    private static final Object lock = new Object();

    public static void main(String[] args) {

        while(true) {
            try {
                ServerSocket serverSocket = new ServerSocket(12345); // 指定监听的端口
                System.out.println("等待客户端连接...");
                Socket clientSocket = serverSocket.accept(); // 等待客户端连接

                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    System.out.println("客户端消息：" + inputLine);
                    writer.println("服务器收到消息：" + inputLine);
                }

                reader.close();
                writer.close();
                clientSocket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 创建两个线程进行文件读写
            Thread writerThread = new Thread(() -> writeFile("1"));
            Thread readerThread = new Thread(commentRemote::readFile);

            // 启动线程
            writerThread.start();
            readerThread.start();

            // 等待线程结束
            try {
                writerThread.join();
                readerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeFile(String content) {
        synchronized (lock) {
            try (FileWriter writer = new FileWriter(FILE_PATH)) {
                writer.write(content);
                System.out.println("写入文件成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void readFile() {
        synchronized (lock) {
            // 读取文件的代码
            System.out.println("读取文件成功！");
        }
    }
}
