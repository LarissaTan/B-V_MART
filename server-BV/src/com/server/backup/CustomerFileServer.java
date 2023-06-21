package com.server.backup;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Collectors;

public class CustomerFileServer {
    private static final Object fileLock = new Object();

    public static void main(String[] args) {
        CustomerFileServer server = new CustomerFileServer();
        server.startServer(54321);
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

    private synchronized boolean authenticateUser(String username, String password) {
//        读取customer.txt，
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

    private synchronized boolean unlockUser(String name) {
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
                } else if (request.equals("LOGIN")) {
                    String username = reader.readLine();
                    String password = reader.readLine();
                    boolean success = authenticateUser(username, password);
                    if (success) {
                        writer.write("Login successful");
                    } else {
                        writer.write("Login failed");
                    }
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
