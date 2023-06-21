package com.example.bv_mart.network;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class BackUpNetworkTask extends AsyncTask<Void, Void, String> {
    private String username;
    private String password;

    public BackUpNetworkTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            InetAddress host = InetAddress.getLocalHost();
            Socket socket = new Socket(host.getHostName(), 16800);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("login" + "@" + username + "@" + password);

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String message;
            message = (String) ois.readObject();

            ois.close();
            oos.close();
            socket.close();

            return message;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String message) {
        if (message != null) {
            // 处理服务器响应
            System.out.println(message);
        } else {
            // 处理连接或通信错误
        }
    }
}
