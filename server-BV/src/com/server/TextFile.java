package com.server;

import java.util.concurrent.Semaphore;

public class TextFile {
    private String filename;
    private int readerCount;
    //sempahores to prevent starvation
    private Semaphore resourceSemaphore;  //to control the access to the resources
    private Semaphore mutex;  //to enforce critcal section
    private Semaphore queueSemaphore; //to manage the queueing to prevent starvation from reader/writer

    //constructor
    public TextFile(String filename, int readerCount, Semaphore resourceSemaphore, Semaphore mutex, Semaphore queueSemaphore) {
        this.filename = filename;
        this.readerCount = readerCount;
        this.resourceSemaphore = resourceSemaphore;
        this.mutex = mutex;
        this.queueSemaphore = queueSemaphore;
    }

    public void setFilename(String fname) {
        filename = fname;
    }

    public String getFilename() {
        return filename;
    }

    public void setReaderCount(int rc) {
        readerCount = rc;
    }

    public int getReaderCount() {
        return readerCount;
    }

    public void setResourceSemaphore(Semaphore s) {
        resourceSemaphore = s;
    }

    public Semaphore getResourceSemaphore() {
        return resourceSemaphore;
    }

    public void setMutex(Semaphore s) {
        mutex = s;
    }

    public Semaphore getMutex() {
        return mutex;
    }

    public void setQueueSemaphore(Semaphore s) {
        queueSemaphore = s;
    }

    public Semaphore getQueueSemaphore() {
        return queueSemaphore;
    }
}
