package com.server;

import javax.swing.*;
import java.awt.*;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

public class server extends JFrame {
    public JTextArea jta = new JTextArea();

    // constructor
    public server() {

    }

    public static void main(String[] args) {
        server server = new server();
        server.build();
    }

    public void build() {
        server s1 = new server();
        try {
            Registry reg = LocateRegistry.createRegistry(4444);
            chatRemote rc = new chatRemote();
            reg.rebind("chatRemote", (Remote) rc);

            System.out.println("Server is ready.");
            s1.jta.append("Server started at " + new Date() + '\n');
            rc.setServerObj(s1);
            rc.initialise();
            s1.setLayout(new BorderLayout());
            s1.add(new JScrollPane(s1.jta), BorderLayout.CENTER);
            s1.setTitle("Server");
            s1.setSize(500, 600);

            s1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            s1.setVisible(true);
            s1.jta.setEditable(false);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void displayMsg(server obj, String s) {
        obj.jta.append(s + ".\n");
    }

}
