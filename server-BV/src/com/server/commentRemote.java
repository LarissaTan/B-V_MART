package com.server;

import com.example.bv_mart.bean.chatObject;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class commentRemote {
    private static final String FILE_PATH = "comment.txt";
    private static final Object lock = new Object();
    public static chatObject inputLine = null;

    public static void main(String[] args) {
        while (true) {
            try {
                ServerSocket serverSocket = new ServerSocket(12345); // 指定监听的端口
                System.out.println("等待客户端连接...");
                Socket clientSocket = serverSocket.accept(); // 等待客户端连接

                ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());//bug!!
                //BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                while ((inputLine = (chatObject) reader.readObject()) != null) {
                    System.out.println("客户端消息：" + inputLine);
                    writer.println("服务器收到消息：" + inputLine);
                    chatObject content = inputLine; // 将消息内容存储在临时变量中

                    // 创建两个线程进行文件读写
                    Thread writerThread = new Thread(() -> writeFile(content));
                    Thread readerThread = new Thread(commentRemote::readFile);//here call read file

                    // 启动线程
                    writerThread.start();
                    readerThread.start();

                    // 等待线程结束
                    writerThread.join();
                    readerThread.join();
                }

                // 关闭资源
                reader.close();
                writer.close();
                clientSocket.close();
                serverSocket.close();
            } catch (BindException | EOFException e) {
//                e.printStackTrace();
            }catch (IOException | InterruptedException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeFile(chatObject content) {
        synchronized (lock) {
            System.out.println("the output is " + content);
            try (FileWriter writer = new FileWriter(FILE_PATH,true)) {
                writer.write("\n" + content.username + "," + content.msg + "," +content.time);
                System.out.println("writing to txt is success");
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
