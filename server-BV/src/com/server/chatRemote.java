package com.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class chatRemote {
    private String filename;
    public server server;
    private String writeStatusMsg, createStatusMsg;
    private String fileContent;
    private String username;
    private List<TextFile> allTextFile = new ArrayList();
    private File fileDirectory;

    public chatRemote() throws RemoteException {

    }

    public void initialise() {
        server.displayMsg(server, getTimestamp() + "    Available files in the server:");
        final File directory = new File("chat.txt");
        fileDirectory = directory;
        for (final File f : directory.listFiles()) {
            if (f.getName().endsWith(".txt")) {
                server.displayMsg(server, f.getName());
                if (!f.getName().endsWith("_replica.txt")) {
                    try {
                        trueRead(f.getName());
                        trueWrite(f.getName(), fileContent);
                    } catch (Exception e) {
                        System.out.println("Error" + e);
                    }
                }
            }
        }
    }

    public synchronized boolean read(String FName) throws RemoteException {
        try {
            if (FName.endsWith("_replica.txt")) {
                throw new Exception();
            }
            //if file exist
            FileInputStream fin = new FileInputStream(FName);
            //initially when allTextFile is empty
            if (allTextFile.size() == 0) {
                //initialise and register semaphores for this file
                allTextFile.add(new TextFile(FName, 0, new Semaphore(1), new Semaphore(1), new Semaphore(1)));
            } else {
                for (int i = 0; i < allTextFile.size(); i++) {
                    //if doesnt locate the file and already at the last index
                    if (!allTextFile.get(i).getFilename().equals(FName) && i == (allTextFile.size() - 1)) {
                        //register this file entry
                        allTextFile.add(new TextFile(FName, 0, new Semaphore(1), new Semaphore(1), new Semaphore(1)));
                    }
                }
            }
            server.displayMsg(server, getTimestamp() + "    " + username + " trying to access " + FName);
            return true;
        } catch (Exception e) {
            System.out.println("Read Error: " + e);
            server.displayMsg(server, getTimestamp() + "    " + username + " failed to locate " + FName);
            return false;
        }
    }

    public void trueRead(String FName) throws RemoteException {
        filename = FName;
        List<Character> fileContentInCharacterList = new ArrayList();
        try {
            if (FName.endsWith("_replica.txt")) {
                throw new Exception();
            }
            FileInputStream fin = new FileInputStream(FName);
            System.out.println("Successfully read " + FName);
            int i = 0;
            while ((i = fin.read()) != -1) {
                fileContentInCharacterList.add((char) i);
            }
            fileContent = convertCharListToString(fileContentInCharacterList);
            fin.close();
        } catch (Exception e) {
            System.out.println("Read Error: " + e);
            fileContent = "file not found";
            if (FName.endsWith("_replica.txt")) {
                fileContent = "Access denied for current entry";
            }
            //try to read from replica instead
            try {
                FileInputStream fin = new FileInputStream(FName.substring(0, FName.length() - 4) + "_replica.txt");
                //if replica found read from replica
                int i = 0;
                while ((i = fin.read()) != -1) {
                    fileContentInCharacterList.add((char) i);
                }
                fileContent = convertCharListToString(fileContentInCharacterList);
                fin.close();
                server.displayMsg(server, getTimestamp() + "    restored file " + FName + " from the replica in the server");
                //restore the file
                trueWrite(FName, fileContent);
            } catch (Exception ex) {
                System.out.println("Error" + ex);
            }
        }
    }

    public synchronized boolean write(String FName) throws RemoteException {
        try {
            if (FName.endsWith("_replica.txt")) {
                throw new Exception();
            }
            FileInputStream fin = new FileInputStream(FName);
            fin.close();
            writeStatusMsg = "Successfully wrote to " + FName;
            server.displayMsg(server, getTimestamp() + "    " + username + " trying to write " + FName);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to locate: " + e);
            server.displayMsg(server, getTimestamp() + "    " + username + " is not able to write to " + FName);
            writeStatusMsg = "Failed wrote to " + FName + "\nFile not found";
            return false;
        }
    }

    public synchronized void create(String FName) throws RemoteException {
        filename = FName;
        try {
            FileInputStream fin = new FileInputStream(FName);
            fin.close();
            System.out.println("Failed to create file");
            server.displayMsg(server, getTimestamp() + "    " + username + " failed create file " + FName + ". File already existed");
            createStatusMsg = "Failed to create " + FName + "\nFile already existed";
        } catch (FileNotFoundException e) {
            trueWrite(FName, "");
            createStatusMsg = "Successfully create the file " + FName + "\n";
            server.displayMsg(server, getTimestamp() + "    " + username + " successfully create file " + FName);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public synchronized void trueWrite(String FName, String Itext) throws RemoteException {
        try {
            FileWriter myWriter = new FileWriter(FName);
            FileWriter myWriter2 = new FileWriter(FName.substring(0, FName.length() - 4) + "_replica.txt");
            myWriter.write(Itext);
            myWriter2.write(Itext);
            myWriter.close();
            myWriter2.close();
        } catch (Exception e) {
            System.out.println("Write Error: " + e);
        }
    }

    public void setServerObj(server obj) {
        server = obj;
    }

    public String getFileName() {
        return filename;
    }

    public String getWriteStatusMsg() {
        return writeStatusMsg;
    }

    public String getCreateStatusMsg() {
        return createStatusMsg;
    }

    public String getTimestamp() {
        LocalDateTime temp = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("'['dd-MM-yyyy'] ['HH:mm:ss']'");
        String timestamp = temp.format(dateFormat);
        return timestamp;
    }

    public void setUsername(String name) {
        username = name;
    }

    public void incrementReaderCount(String FName) {
        for (TextFile f : allTextFile) {
            if (f.getFilename().equals(FName)) {
                f.setReaderCount(f.getReaderCount() + 1);
                System.out.println("1 more reader enter read of " + f.getFilename());
            }
        }
    }

    public void decrementReaderCount(String FName) {
        for (TextFile f : allTextFile) {
            if (f.getFilename().equals(FName)) {
                f.setReaderCount(f.getReaderCount() - 1);
                System.out.println("1 reader left read of " + f.getFilename());
            }
        }
    }

    public int getReaderCount(String FName) {
        for (TextFile f : allTextFile) {
            if (f.getFilename().equals(FName)) {
                return f.getReaderCount();
            }
        }
        return -1;
    }

    public void acquireResourceSemaphore(String FName) {
        try {
            for (TextFile f : allTextFile) {
                if (f.getFilename().equals(FName)) {
                    f.getResourceSemaphore().acquire();
                    System.out.println("acquire resource semaphore for " + f.getFilename());
                }
            }
        } catch (Exception e) {
            System.out.println("Error" + e);
            System.out.println("Aquire resource semaphore error");
        }
    }

    public void releaseResourceSemaphore(String FName) {
        try {
            for (TextFile f : allTextFile) {
                if (f.getFilename().equals(FName)) {
                    f.getResourceSemaphore().release();
                    System.out.println("release resource semaphore for " + f.getFilename());
                }
            }
        } catch (Exception e) {
            System.out.println("Error" + e);
            System.out.println("Release resource semaphore error");
        }
    }

    public void acquireMutex(String FName) {
        try {
            for (TextFile f : allTextFile) {
                if (f.getFilename().equals(FName)) {
                    f.getMutex().acquire();
                    System.out.println("acquire mutex for " + f.getFilename());
                }
            }
        } catch (Exception e) {
            System.out.println("Error" + e);
            System.out.println("Aquire mutex error");
        }
    }

    public void releaseMutex(String FName) {
        try {
            for (TextFile f : allTextFile) {
                if (f.getFilename().equals(FName)) {
                    f.getMutex().release();
                    System.out.println("release mutex for " + f.getFilename());
                }
            }
        } catch (Exception e) {
            System.out.println("Error" + e);
            System.out.println("Release mutex error");
        }
    }

    public void acquireQueueSemaphore(String FName) {
        try {
            for (TextFile f : allTextFile) {
                if (f.getFilename().equals(FName)) {
                    f.getQueueSemaphore().acquire();
                    System.out.println("acquire queue semaphore for " + f.getFilename());
                }
            }
        } catch (Exception e) {
            System.out.println("Error" + e);
            System.out.println("Aquire queue semaphore error");
        }
    }

    public void releaseQueueSemaphore(String FName) {
        try {
            for (TextFile f : allTextFile) {
                if (f.getFilename().equals(FName)) {
                    f.getQueueSemaphore().release();
                    System.out.println("release queue semaphore for " + f.getFilename());
                }
            }
        } catch (Exception e) {
            System.out.println("Error" + e);
            System.out.println("Release queue semaphore error");
        }
    }

    public void userLogin(String name) {
        server.displayMsg(server, getTimestamp() + "    Welcome " + name);
        server.displayMsg(server, getTimestamp() + "    " + name + " joined the meeting memo");
    }

    public void userLogout(String name) {
        server.displayMsg(server, getTimestamp() + "    Bye " + name);
        server.displayMsg(server, getTimestamp() + "    " + name + " leaved the meeting memo");
    }

    public File getFileDirectory() {
        return fileDirectory;
    }

    public String convertCharListToString(List<Character> charlist) {
        String s = "";
        for (char c : charlist) {
            s += c;
        }
        return s;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void displayMsgOnServer(String msg) {
        server.displayMsg(server, getTimestamp() + "    " + username + " " + msg);
    }
}
