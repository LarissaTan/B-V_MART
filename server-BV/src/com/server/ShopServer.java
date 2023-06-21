package com.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Collectors;

public class ShopServer {

    private static ServerSocket server;
    private static final int port = 16800;

    public static void main(String[] args) throws IOException {
        server = new ServerSocket(port);

        while (true) {
            System.out.println("Waiting for the client request");
            Socket socket = server.accept();

            // 创建一个新线程来处理客户端请求
            Thread clientThread = new Thread(() -> handleClientRequest(socket));
            clientThread.start();
        }
    }

    private static void handleClientRequest(Socket socket) {
        String message;
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            message = (String) ois.readObject();
            System.out.println("Message Received: " + message);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("Hi Client " + message);

            ois.close();
            oos.close();
            socket.close();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

//        如果发送login
        if (message.startsWith("login")) {
            String[] split = message.split("@");
            String username = split[1];
            String password = split[2];
            boolean authenticated = authenticateUser(username, password);
            System.out.println("User " + username + " authenticated: " + authenticated);
        }

//        如果发送unlock
        if (message.startsWith("unlock")) {
            String[] split = message.split("@");
            String username = split[1];
            boolean unlocked = unlockUser(username);
            System.out.println("User " + username + " unlocked: " + unlocked);
        }

        if (message.equalsIgnoreCase("exit")) {
            try {
                close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static synchronized boolean unlockUser(String name) {
//        读取customer.txt，
        File file = new File("customer.txt");
        BufferedReader reader;
        BufferedWriter writer;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
//        逐行读取，如果有一行的用户名和传入的参数相同，返回true
//        否则返回false
        int lineNum = 0;
        for (String line : reader.lines().collect(Collectors.toList())) {
            lineNum++;
            String[] parts = line.split(",");
            if (parts[0].equals(name) && parts[2].equals("locked")) {
//                当前行从locked变为unlocked
                String newLine = name + "," + parts[1] + ",unlock";

                try {
                    RandomAccessFile raf = new RandomAccessFile(file, "rw");
                    long position = (lineNum - 1) * (newLine.length() + 1);
                    byte[] lineBytes = newLine.getBytes();
                    int lineLength = lineBytes.length;

                    // 清空原行
                    raf.seek(position);
                    for (int i = 0; i < lineLength; i++) {
                        raf.writeByte(0);
                    }

                    // 写入新行
                    raf.seek(position);
                    raf.writeBytes(newLine);

                    raf.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
        }

        return false;
    }

    private static synchronized boolean authenticateUser(String username, String password) {
        File file = new File("customer.txt");
        BufferedReader reader;
        BufferedWriter writer;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
//        逐行读取，如果有一行的用户名和密码都和传入的参数相同，返回true
//        否则返回false
        int lineNum = 0;
        for (String line : reader.lines().collect(Collectors.toList())) {
            lineNum++;
            String[] parts = line.split(",");
            if (parts[0].equals(username) && parts[1].equals(password) && parts[2].equals("unlock")) {
//                当前行从unlocked变为locked
                String newLine = username + "," + password + ",locked";

                try {
                    RandomAccessFile raf = new RandomAccessFile(file, "rw");
                    long position = (lineNum - 1) * (newLine.length() + 1);
                    byte[] lineBytes = newLine.getBytes();
                    int lineLength = lineBytes.length;

                    // 清空原行
                    raf.seek(position);
                    for (int i = 0; i < lineLength; i++) {
                        raf.writeByte(0);
                    }

                    // 写入新行
                    raf.seek(position);
                    raf.writeBytes(newLine);

                    raf.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
        }
        return false;
    }

    public static void close() throws IOException {
        server.close();
    }

}
