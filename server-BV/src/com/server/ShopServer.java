package com.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ShopServer {

    private static ServerSocket server;
    private static final int port = 16800;
    private static double totalPrice = 0;

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
        String reply;
        String message;

        ObjectInputStream ois;
        ObjectOutputStream oos;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            message = (String) ois.readObject();
            System.out.println("Message Received: " + message);

            reply = extracted(message);

            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("Reply" + "@" + reply);

            ois.close();
            oos.close();
            socket.close();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    private static String extracted(String message) {
        String reply = "";

        //        如果发送login
        if (message.startsWith("login")) {
            String[] split = message.split("@");
            String username = split[1];
            String password = split[2];
            boolean authenticated = authenticateUser(username, password);
            System.out.println("User " + username + " authenticated: " + authenticated);
            reply = username + "@" + authenticated;
        }

//        如果发送unlock
        if (message.startsWith("unlock")) {
            String[] split = message.split("@");
            String username = split[1];
            boolean unlocked = unlockUser(username);
            System.out.println("User " + username + " unlocked: " + unlocked);
            reply = username + "@" + unlocked;
        }

        if (message.startsWith("register")) {
            String[] split = message.split("@");
            String username = split[1];
            String password = split[2];
            boolean registered = registerUser(username, password);
            System.out.println("User " + username + " registered: " + registered);
            reply = username + "@" + registered;
        }

        if (message.startsWith("pay")) {
            String[] split = message.split("@");
            String name = split[1];
//            移除前两个元素
            split = Arrays.copyOfRange(split, 2, split.length);
            boolean paid = payProduct(split, name);
            System.out.println("User paid: " + paid);
            reply = "paid@" + paid;
        }

        if (message.equalsIgnoreCase("exit")) {
            try {
                close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            reply = "Server stopped";
        }


        return reply;
    }

    private static boolean payProduct(String[] split, String userName) {
        // 每三个为一组，分别为商品的名字，数量，以及单价，减少products.txt同名商品的库存
        File file = new File("products.txt");
        List<String> lines = new ArrayList<>();

        System.out.println(userName);
        System.out.println(Arrays.toString(split));

        totalPrice = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            lines = reader.lines().collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < split.length; i += 3) {
            String name = split[i];
            int quantity = (int) Double.parseDouble(split[i + 1]);
            double price = Double.parseDouble(split[i + 2]);

            totalPrice = totalPrice + price * quantity;

            for (int lineNum = 0; lineNum < lines.size(); lineNum++) {
                String line = lines.get(lineNum);
                // parts 中分别为商品名，商品号，单价，库存
                String[] parts = line.split(",");
                System.out.println("parts: " + Arrays.toString(parts));
                System.out.println(name + parts[0]);
                if (parts[0].equals(name)) {
                    double stock = Double.parseDouble(parts[3]);
                    if (stock < quantity) {
                        return false;
                    }
                    stock -= quantity;
                    String newLine = name + "," + parts[1] + "," + parts[2] + "," + stock;
                    lines.set(lineNum, newLine);
                    break;
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        读取sales.txt的行数
        int lineNumSales = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("sales.txt"))) {
            while (reader.readLine() != null) {
                lineNumSales++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        获取19-06-2023格式的日期
        String date = LocalDate.now().plusYears(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//        获取19:47:41格式的时间
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String total = String.valueOf(totalPrice);
        String newLineSales = (lineNumSales+1) + "," + date + "," + time + ","  + total + "," + userName;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("sales.txt", true))) {
            writer.newLine();
            writer.write(newLineSales);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private static boolean registerUser(String username, String password) {
        File file = new File("customer.txt");
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(file, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            writer.write(username + "," + password + ",unlock");
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
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
