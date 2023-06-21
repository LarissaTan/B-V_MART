package com.server;

import com.example.bv_mart.bean.chatObject;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class userRemote {
    private static final String FILE_PATH = "customer.txt";
    private Lock fileLock = new ReentrantLock();

    public static void main(String[] args) {

    }

    public void processRequest(){

    }
}
