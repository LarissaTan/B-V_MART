package com.server;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;

public class Access extends JFrame {
    static String[][] billData = new String[1000][4];
    int noOfBillDataRow = 0;
    static float[] productQuantity = new float[1000];

    Access() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(800, 600);
        this.setResizable(false);
        this.setTitle("Internal personnel only");

        JLayeredPane containerLayeredPane = new JLayeredPane();
        containerLayeredPane.setLayout(null);
        containerLayeredPane.setBounds(0, 0, 800, 600);


        JPanel accessPanel = new JPanel();
        accessPanel.setLayout(new BorderLayout());
        accessPanel.setBounds(0, 0, 800, 600);

        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BorderLayout());
        dashboardPanel.setBounds(0, 0, 800, 600);
        dashboardPanel.setVisible(false);

        /**** Access frame start ****/
        JLayeredPane accessLayeredPane = new JLayeredPane();
        accessLayeredPane.setLayout(null);
        accessLayeredPane.setPreferredSize(new Dimension(600, 400));
        JPanel signInPanel = new JPanel();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        signInPanel.setBounds(0, 0, 800, 600);
        panel1.setPreferredSize(new Dimension(100, 100));
        panel2.setPreferredSize(new Dimension(100, 100));
        panel3.setPreferredSize(new Dimension(100, 100));
        panel4.setPreferredSize(new Dimension(100, 100));
        signInPanel.setLayout(null);
//        #d6d9df
        signInPanel.setBackground(new Color(214, 217, 223));

        /**** SignInPanel Component ****/
        JLabel titleLabel = new JLabel("B-V Mart");
        titleLabel.setBounds(240, 40, 200, 32);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 24));

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(170, 80, 200, 25);
        usernameLabel.setForeground(Color.GRAY);
        usernameLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        JTextField usernameField = new JTextField(50);
        usernameField.setBounds(170, 110, 260, 36);
        usernameField.setFont(new Font("Poppins", Font.PLAIN, 14));

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(170, 160, 200, 25);
        passwordLabel.setForeground(Color.GRAY);
        passwordLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(170, 190, 260, 36);
        passwordField.setFont(new Font("Poppins", Font.PLAIN, 14));

        JButton signinButton = new JButton("Sign In");
        signinButton.setBounds(220, 260, 160, 36);
        signinButton.setFont(new Font("Poppins", Font.PLAIN, 14));
        signinButton.setForeground(Color.BLACK);
        signinButton.setFocusPainted(false);
        signinButton.setRolloverEnabled(false);
        signinButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (!usernameField.getText().equals("") && passwordField.getPassword().length != 0) {
                Scanner sc = new Scanner(System.in);
                try {
                    File myObj = new File("users.txt");
                    Scanner myReader = new Scanner(myObj);
                    while (myReader.hasNext()) {
                        String data = myReader.nextLine();
                        String[] token = data.split(",");
                        String usernameFromDb = token[0];
                        String passwordFromDb = token[1];
                        if (username.equals(usernameFromDb) && password.equals(passwordFromDb)) {
                            JOptionPane.showMessageDialog(signInPanel, "Successfully signed in!!!!");
                            accessPanel.setVisible(false);
                            dashboardPanel.setVisible(true);
                            usernameField.setText("");
                            passwordField.setText("");
                            break;
                        } else if (!myReader.hasNext()) {
                            int option = JOptionPane.showConfirmDialog(signInPanel, "Invalid username/password\nTry again?");
                            if (option == 0) {
                                usernameField.setText("");
                                passwordField.setText("");
                            } else {
                                System.exit(0);
                            }
                        }
                    }
                    myReader.close();
                } catch (FileNotFoundException exception) {
                    JOptionPane.showMessageDialog(signInPanel, "File not found");
                    exception.printStackTrace();
                }
            } else if (usernameField.getText().equals("") || passwordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(signInPanel, "Username/password field can't be empty");
            }
        });

        /**** SignInPanel Component ****/
        signInPanel.add(titleLabel);
        signInPanel.add(usernameLabel);
        signInPanel.add(usernameField);
        signInPanel.add(passwordLabel);
        signInPanel.add(passwordField);
        signInPanel.add(signinButton);


        /**** Dashboard panel starts ****/
        JPanel sideMenu = new JPanel();
        sideMenu.setLayout(null);
        sideMenu.setPreferredSize(new Dimension(800, 30));
        sideMenu.setBackground(Color.WHITE);

        JLayeredPane dashboardContentLayeredPane = new JLayeredPane();
        dashboardContentLayeredPane.setLayout(null);
        dashboardContentLayeredPane.setPreferredSize(new Dimension(600, 600));

        JPanel newBillPanel = new JPanel();
        newBillPanel.setLayout(null);
        newBillPanel.setBounds(0, 50, 800, 550);
        newBillPanel.setVisible(false);

        JPanel addProductPanel = new JPanel();
        addProductPanel.setLayout(null);
        addProductPanel.setBounds(0, 50, 800, 550);
        addProductPanel.setVisible(false);

        JPanel availableStockPanel = new JPanel();
        availableStockPanel.setLayout(null);
        availableStockPanel.setBounds(0, 50, 800, 550);
        availableStockPanel.setVisible(false);

        JPanel updateStockPanel = new JPanel();
        updateStockPanel.setLayout(null);
        updateStockPanel.setBounds(0, 50, 800, 550);
        updateStockPanel.setVisible(false);

        JPanel salesPanel = new JPanel();
        salesPanel.setLayout(null);
        salesPanel.setBounds(0, 100, 800, 500);
        salesPanel.setVisible(false);

        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(null);
        aboutPanel.setBounds(0, 100, 800, 500);
        aboutPanel.setVisible(false);

        String[] catalog = {"---Select---", "New Bill", "Add Product", "Available Product","Update Product","Sales","About","Sign Out"};
        JComboBox selectFunc = new JComboBox(catalog);
        selectFunc.setBounds(20, 20, 150, 36);
        selectFunc.setFont(new Font("Poppins", Font.PLAIN, 14));
        selectFunc.setBackground(Color.WHITE);
        selectFunc.setEditable(true);
        selectFunc.setSelectedIndex(0);

        JButton select = new JButton("Select");
        select.setFont(new Font("Poppins", Font.PLAIN, 14));
        select.setForeground(Color.BLACK);
        select.setFocusPainted(false);
        select.setRolloverEnabled(false);
        select.setBounds(180, 20, 80, 36);

        /**** New bill start ****/
        int count = Functions.recordCount("products.txt");

        JLabel newBillTitle = new JLabel("New Bill");
        newBillTitle.setBounds(20, 20, 360, 25);
        newBillTitle.setFont(new Font("Poppins", Font.BOLD, 20));

        JLabel newBillProductNameLabel = new JLabel("Product name");
        newBillProductNameLabel.setBounds(20, 65, 250, 25);
        newBillProductNameLabel.setForeground(Color.GRAY);
        newBillProductNameLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        JComboBox newBillProductNameField = new JComboBox(new String[]{"---Select---"});
        newBillProductNameField.setBounds(20, 85, 250, 36);
        newBillProductNameField.setFont(new Font("Poppins", Font.PLAIN, 14));
        newBillProductNameField.setBackground(Color.WHITE);
        newBillProductNameField.setEditable(true);
        newBillProductNameField.setSelectedIndex(0);

        JLabel newBillProductQuantityLabel = new JLabel("Quantity");
        newBillProductQuantityLabel.setBounds(280, 65, 250, 25);
        newBillProductQuantityLabel.setForeground(Color.GRAY);
        newBillProductQuantityLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        JTextField newBillProductQuantityField = new JTextField(50);
        newBillProductQuantityField.setBounds(280, 85, 250, 36);
        newBillProductQuantityField.setFont(new Font("Poppins", Font.PLAIN, 14));

        JLabel newBillTotalLabel = new JLabel("Total: 0");
        newBillTotalLabel.setBounds(280, 510, 250, 32);
        newBillTotalLabel.setOpaque(true);
        newBillTotalLabel.setBackground(Color.WHITE);
        newBillTotalLabel.setForeground(Color.BLACK);
        newBillTotalLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Product");
        tableModel.addColumn("Price");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Amount");

        JTable newBillTable = new JTable(tableModel);
        newBillTable.getTableHeader().setFont(new Font("Poppins", Font.PLAIN, 14));
        newBillTable.setFont(new Font("Poppins", Font.PLAIN, 14));
        newBillTable.setShowGrid(false);
        newBillTable.setRowHeight(newBillTable.getRowHeight() + 10);
        DefaultTableCellRenderer defaultHeaderRenderer = (DefaultTableCellRenderer) newBillTable.getTableHeader().getDefaultRenderer();
        defaultHeaderRenderer.setHorizontalAlignment(JLabel.LEFT);
        newBillTable.getTableHeader().setDefaultRenderer(defaultHeaderRenderer);
        final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(null);
        newBillTable.getTableHeader().setDefaultRenderer(renderer);

        JScrollPane newBillScrollPane = new JScrollPane(newBillTable);
        newBillScrollPane.setBounds(20, 195, 510, 295);

        JButton newBillAddButton = new JButton("Add");
        newBillAddButton.setBounds(20, 140, 250, 36);
        newBillAddButton.setFont(new Font("Poppins", Font.PLAIN, 14));
        newBillAddButton.setForeground(Color.DARK_GRAY);
        newBillAddButton.setFocusPainted(false);
        newBillAddButton.setRolloverEnabled(false);
        newBillAddButton.addActionListener(e -> {
            float price = 0;
            float quantity = 0;
            if (newBillProductNameField.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(newBillPanel, "Please select item", "Select item", JOptionPane.WARNING_MESSAGE);
            } else if (newBillProductQuantityField.getText().equals("")) {
                JOptionPane.showMessageDialog(newBillPanel, "Enter quantity", "Enter quantity", JOptionPane.WARNING_MESSAGE);
            } else {
                quantity = Float.parseFloat(newBillProductQuantityField.getText());
                try {
                    File myObj = new File("products.txt");
                    Scanner myReader = new Scanner(myObj);
                    while (myReader.hasNext()) {
                        String data = myReader.nextLine();
                        String[] token = data.split(",");
                        if (((String) newBillProductNameField.getSelectedItem()).equals(token[0])) {
                            price = Float.parseFloat(token[2]);
                        }
                    }
                    myReader.close();
                } catch (FileNotFoundException exception) {
                    exception.printStackTrace();
                }
                if (quantity != 0.0) {
                    tableModel.insertRow(tableModel.getRowCount(), new Object[]{(String) newBillProductNameField.getSelectedItem(),
                            price,
                            newBillProductQuantityField.getText(),
                            price * quantity
                    });
                    productQuantity[noOfBillDataRow] = quantity;
                    billData[noOfBillDataRow][0] = (String) newBillProductNameField.getSelectedItem();
                    billData[noOfBillDataRow][1] = Float.toString(price);
                    billData[noOfBillDataRow][2] = newBillProductQuantityField.getText();
                    billData[noOfBillDataRow][3] = Float.toString(price * quantity);
                    noOfBillDataRow++;
                }
                String[] data = newBillTotalLabel.getText().split(":");
                float total = Float.parseFloat(data[1]);
                total = total + (price * quantity);
                newBillTotalLabel.setText("Total: " + String.valueOf(total));

                newBillProductNameField.setSelectedIndex(0);
                newBillProductQuantityField.setText("");
            }
        });

        JButton newBillButton = new JButton("Create bill");
        newBillButton.setBounds(280, 140, 250, 36);
        newBillButton.setFont(new Font("Poppins", Font.PLAIN, 14));
        newBillButton.setForeground(Color.BLACK);
        newBillButton.setFocusPainted(false);
        newBillButton.setRolloverEnabled(false);
        newBillButton.addActionListener(e -> {
            newBillInfo();
        });

        JButton newBillClearButton = new JButton("Clear");
        newBillClearButton.setBounds(20, 510, 250, 32);
        newBillClearButton.setFont(new Font("Poppins", Font.PLAIN, 14));
        newBillClearButton.setForeground(Color.BLACK);
        newBillClearButton.setFocusPainted(false);
        newBillClearButton.setRolloverEnabled(false);
        newBillClearButton.addActionListener(e -> {
            billData = new String[1000][4];
            noOfBillDataRow = 0;
            productQuantity = new float[1000];

            tableModel.setRowCount(0);
            newBillProductQuantityField.setText("");
            newBillProductNameField.setSelectedIndex(0);
        });

        /**** Add product start ****/
        JLabel addProductTitle = new JLabel("Add Product");
        addProductTitle.setBounds(20, 20, 360, 25);
        addProductTitle.setFont(new Font("Poppins", Font.BOLD, 20));

        JLabel productNameLabel = new JLabel("Product name");
        productNameLabel.setBounds(20, 65, 200, 25);
        productNameLabel.setForeground(Color.GRAY);
        productNameLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        JTextField productNameField = new JTextField(50);
        productNameField.setBounds(20, 85, 460, 36);
        productNameField.setFont(new Font("Poppins", Font.PLAIN, 14));

        JLabel productIdLabel = new JLabel("Product id");
        productIdLabel.setBounds(20, 140, 200, 25);
        productIdLabel.setForeground(Color.GRAY);
        productIdLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        JTextField productIdField = new JTextField(50);
        productIdField.setBounds(20, 160, 460, 36);
        productIdField.setFont(new Font("Poppins", Font.PLAIN, 14));

        JLabel productPriceLabel = new JLabel("Product price");
        productPriceLabel.setBounds(20, 215, 200, 25);
        productPriceLabel.setForeground(Color.GRAY);
        productPriceLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        JTextField productPriceField = new JTextField(50);
        productPriceField.setBounds(20, 235, 460, 36);
        productPriceField.setFont(new Font("Poppins", Font.PLAIN, 14));

        JLabel productStockLabel = new JLabel("Product stock");
        productStockLabel.setBounds(20, 290, 200, 25);
        productStockLabel.setForeground(Color.GRAY);
        productStockLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        JTextField productStockField = new JTextField(50);
        productStockField.setBounds(20, 310, 460, 36);
        productStockField.setFont(new Font("Poppins", Font.PLAIN, 14));

        JButton addProductButton = new JButton("Add Product");
        addProductButton.setBounds(20, 365, 460, 36);
        addProductButton.setFont(new Font("Poppins", Font.PLAIN, 14));
        addProductButton.setBackground(Color.BLACK);
        addProductButton.setForeground(Color.BLACK);
        addProductButton.setFocusPainted(false);
        addProductButton.setRolloverEnabled(false);

        JButton addProductClearButton = new JButton("Clear Field");
        addProductClearButton.setBounds(20, 420, 460, 36);
        addProductClearButton.setFont(new Font("Poppins", Font.PLAIN, 14));
        addProductClearButton.setForeground(Color.BLACK);
        addProductClearButton.setFocusPainted(false);
        addProductClearButton.setRolloverEnabled(false);
        addProductClearButton.addActionListener(e -> {
            productNameField.setText("");
            productIdField.setText("");
            productPriceField.setText("");
            productStockField.setText("");
        });

        addProductButton.addActionListener(e -> {
            Scanner sc = new Scanner(System.in);
            String name = productNameField.getText();
            String id = productIdField.getText();
            String price = productPriceField.getText();
            String stock = productStockField.getText();

            /****** file creation ******/
            try {
                File myObj = new File("products.txt");
                if (myObj.createNewFile()) {
                    System.out.println("File created: " + myObj.getName());
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            /****** writing in file *******/
            try {
                FileWriter myWriter = new FileWriter("products.txt", true);
                myWriter.write(name + "," + id + "," + Float.parseFloat(price) + "," + Float.parseFloat(stock) + "\n");
                myWriter.close();
                JOptionPane.showMessageDialog(addProductPanel, "Product added");

                productNameField.setText("");
                productIdField.setText("");
                productPriceField.setText("");
                productStockField.setText("");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        /**** Available stock start ****/
        String[] stockColumnName = {"Product", "Id", "Price", "Stock"};

        JLabel availableStockTitle = new JLabel("Available Stock");
        availableStockTitle.setBounds(20, 20, 360, 25);
        availableStockTitle.setFont(new Font("Poppins", Font.BOLD, 20));

        DefaultTableModel availableStockTableModel = new DefaultTableModel();
        availableStockTableModel.addColumn("Product");
        availableStockTableModel.addColumn("Product Id");
        availableStockTableModel.addColumn("Price");
        availableStockTableModel.addColumn("Stock");

        JTable availableStockTable = new JTable(availableStockTableModel);
        availableStockTable.getTableHeader().setFont(new Font("Poppins", Font.PLAIN, 14));
        availableStockTable.setFont(new Font("Poppins", Font.PLAIN, 14));
        availableStockTable.setShowGrid(false);
        availableStockTable.setRowHeight(availableStockTable.getRowHeight() + 10);
        DefaultTableCellRenderer availableStockHeaderRenderer = (DefaultTableCellRenderer) availableStockTable.getTableHeader().getDefaultRenderer();
        availableStockHeaderRenderer.setHorizontalAlignment(JLabel.LEFT);
        availableStockTable.getTableHeader().setDefaultRenderer(availableStockHeaderRenderer);
        final DefaultTableCellRenderer availableStockRenderer = new DefaultTableCellRenderer();
        availableStockRenderer.setBorder(null);
        availableStockTable.getTableHeader().setDefaultRenderer(availableStockRenderer);

        JScrollPane availableStockScrollPane = new JScrollPane(availableStockTable);
        availableStockScrollPane.setBounds(20, 65, 545, 450);

        /**** Update stock start ****/
        JLabel updateStockTitle = new JLabel("Update Stock");
        updateStockTitle.setBounds(20, 20, 360, 25);
        updateStockTitle.setFont(new Font("Poppins", Font.BOLD, 20));

        JLabel updateStockProductIdLabel = new JLabel("Product id");
        updateStockProductIdLabel.setBounds(20, 65, 250, 25);
        updateStockProductIdLabel.setForeground(Color.GRAY);
        updateStockProductIdLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        JComboBox updateStockProductIdField = new JComboBox(new String[]{"---Select---"});
        updateStockProductIdField.setBounds(20, 85, 250, 36);
        updateStockProductIdField.setFont(new Font("Poppins", Font.PLAIN, 14));
        updateStockProductIdField.setBackground(Color.WHITE);
        updateStockProductIdField.setEditable(true);
        updateStockProductIdField.setSelectedIndex(0);

        JLabel updateStockProductNameLabel = new JLabel("Product name");
        updateStockProductNameLabel.setBounds(20, 140, 250, 25);
        updateStockProductNameLabel.setForeground(Color.GRAY);
        updateStockProductNameLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        JTextField updateStockProductNameField = new JTextField(50);
        updateStockProductNameField.setBounds(20, 160, 250, 36);
        updateStockProductNameField.setFont(new Font("Poppins", Font.PLAIN, 14));
        updateStockProductNameField.setEditable(false);
        updateStockProductNameField.setBackground(Color.WHITE);

        JLabel updateStockProductPriceLabel = new JLabel("Price");
        updateStockProductPriceLabel.setBounds(20, 215, 250, 25);
        updateStockProductPriceLabel.setForeground(Color.GRAY);
        updateStockProductPriceLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        JTextField updateStockProductPriceField = new JTextField(50);
        updateStockProductPriceField.setBounds(20, 235, 250, 36);
        updateStockProductPriceField.setFont(new Font("Poppins", Font.PLAIN, 14));
        updateStockProductPriceField.setEditable(false);
        updateStockProductPriceField.setBackground(Color.WHITE);

        JLabel updateStockProductStockLabel = new JLabel("Stock");
        updateStockProductStockLabel.setBounds(20, 290, 250, 25);
        updateStockProductStockLabel.setForeground(Color.GRAY);
        updateStockProductStockLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        JTextField updateStockProductStockField = new JTextField(50);
        updateStockProductStockField.setBounds(20, 310, 250, 36);
        updateStockProductStockField.setFont(new Font("Poppins", Font.PLAIN, 14));
        updateStockProductStockField.setEditable(false);
        updateStockProductStockField.setBackground(Color.WHITE);

        JButton updateStockSelectButton = new JButton("Select");
        JButton updateStockDeleteButton = new JButton("Delete");
        JButton updateStockUpdateButton = new JButton("Update");

        updateStockDeleteButton.setBounds(20, 365, 250, 36);
        updateStockDeleteButton.setFont(new Font("Poppins", Font.PLAIN, 14));
        updateStockDeleteButton.setForeground(Color.BLACK);
        updateStockDeleteButton.setFocusPainted(false);
        updateStockDeleteButton.setRolloverEnabled(false);
        updateStockDeleteButton.setEnabled(false);
        updateStockDeleteButton.addActionListener(e -> {
            try {
                File inputFile = new File("products.txt");
                if (!inputFile.isFile()) {
                    System.out.println("File does not exist");
                    return;
                }
                File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");
                BufferedReader br = new BufferedReader(new FileReader("products.txt"));
                PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
                String line = null;
                //Read from the original file and write to the new
                //unless content matches data to be removed.
                while ((line = br.readLine()) != null) {
                    String[] token = line.split(",");
                    if (!updateStockProductIdField.getSelectedItem().equals(token[1])) {
                        pw.println(line);
                        pw.flush();
                    }
                }
                pw.close();
                br.close();
                //Delete the original file
                if (!inputFile.delete()) {
                    System.out.println("Could not delete file");
                    return;
                }
                //Rename the new file to the filename the original file had.
                if (!tempFile.renameTo(inputFile))
                    System.out.println("Could not rename file");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            updateStockProductNameField.setText("");
            updateStockProductPriceField.setText("");
            updateStockProductStockField.setText("");
            updateStockProductIdField.setSelectedIndex(0);
            updateStockProductNameField.setEditable(false);
            updateStockProductPriceField.setEditable(false);
            updateStockProductStockField.setEditable(false);
            updateStockDeleteButton.setEnabled(false);
            updateStockUpdateButton.setEnabled(false);
            JOptionPane.showMessageDialog(updateStockPanel, "Product removed");
        });

        updateStockUpdateButton.setBounds(280, 365, 250, 36);
        updateStockUpdateButton.setFont(new Font("Poppins", Font.PLAIN, 14));
        updateStockUpdateButton.setForeground(Color.BLACK);
        updateStockUpdateButton.setFocusPainted(false);
        updateStockUpdateButton.setRolloverEnabled(false);
        updateStockUpdateButton.setEnabled(false);
        updateStockUpdateButton.addActionListener(e -> {
            try {
                File inputFile = new File("products.txt");
                if (!inputFile.isFile()) {
                    System.out.println("File does not exist");
                    return;
                }
                File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");
                BufferedReader br = new BufferedReader(new FileReader("products.txt"));
                PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
                String line = null;
                //Read from the original file and write to the new
                //unless content matches data to be removed.
                while ((line = br.readLine()) != null) {
                    String[] token = line.split(",");
                    if (updateStockProductIdField.getSelectedItem().equals(token[1])) {
                        pw.println(updateStockProductNameField.getText() + "," + updateStockProductIdField.getSelectedItem() + "," + updateStockProductPriceField.getText() + "," + updateStockProductStockField.getText());
                        pw.flush();
                    } else {
                        pw.println(line);
                    }
                }
                pw.close();
                br.close();
                //Delete the original file
                if (!inputFile.delete()) {
                    System.out.println("Could not delete file");
                    return;
                }
                //Rename the new file to the filename the original file had.
                if (!tempFile.renameTo(inputFile))
                    System.out.println("Could not rename file");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            updateStockProductNameField.setText("");
            updateStockProductPriceField.setText("");
            updateStockProductStockField.setText("");
            updateStockProductIdField.setSelectedIndex(0);
            updateStockProductNameField.setEditable(false);
            updateStockProductPriceField.setEditable(false);
            updateStockProductStockField.setEditable(false);
            updateStockDeleteButton.setEnabled(false);
            updateStockUpdateButton.setEnabled(false);
            JOptionPane.showMessageDialog(updateStockPanel, "Product updated");
        });

        updateStockSelectButton.setBounds(280, 85, 250, 36);
        updateStockSelectButton.setFont(new Font("Poppins", Font.PLAIN, 14));
        updateStockSelectButton.setForeground(Color.BLACK);
        updateStockSelectButton.setFocusPainted(false);
        updateStockSelectButton.setRolloverEnabled(false);
        updateStockSelectButton.addActionListener(e -> {
            updateStockProductNameField.setText("");
            updateStockProductPriceField.setText("");
            updateStockProductStockField.setText("");

            updateStockProductNameField.setEditable(false);
            updateStockProductPriceField.setEditable(false);
            updateStockProductStockField.setEditable(false);

            try {
                File myObj = new File("products.txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNext()) {
                    String data = myReader.nextLine();
                    String[] token = data.split(",");
                    if (updateStockProductIdField.getSelectedItem().equals(token[1])) {
                        updateStockProductNameField.setText(token[0]);
                        updateStockProductPriceField.setText(token[2]);
                        updateStockProductStockField.setText(token[3]);

                        updateStockProductNameField.setEditable(true);
                        updateStockProductPriceField.setEditable(true);
                        updateStockProductStockField.setEditable(true);

                        updateStockDeleteButton.setEnabled(true);
                        updateStockUpdateButton.setEnabled(true);
                    }
                }
                myReader.close();
            } catch (FileNotFoundException exception) {
                //JOptionPane.showMessageDialog(availableStock, "File not found");
                exception.printStackTrace();
            }
        });
//        Update stock end

        /**** Sales start ****/
        String[] salesSearchByFieldData = {"---Select---", "Bill No", "Customer Name", "Phone NO"};
        JLabel salesTitle = new JLabel("Sales");
        salesTitle.setBounds(20, 20, 360, 25);
        salesTitle.setFont(new Font("Poppins", Font.BOLD, 20));

        JLabel salesSearchByLabel = new JLabel("Search by");
        salesSearchByLabel.setBounds(20, 65, 250, 25);
        salesSearchByLabel.setForeground(Color.GRAY);
        salesSearchByLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        JComboBox salesSearchByField = new JComboBox(salesSearchByFieldData);
        salesSearchByField.setBounds(20, 85, 250, 36);
        salesSearchByField.setFont(new Font("Poppins", Font.PLAIN, 14));
        salesSearchByField.setBackground(Color.WHITE);
        salesSearchByField.setEditable(false);
        salesSearchByField.setSelectedIndex(0);

        JLabel salesSearchBoxLabel = new JLabel("Search");
        salesSearchBoxLabel.setBounds(20, 130, 250, 25);
        salesSearchBoxLabel.setForeground(Color.GRAY);
        salesSearchBoxLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        JTextField salesSearchBoxField = new JTextField(50);
        salesSearchBoxField.setBounds(20, 150, 250, 36);
        salesSearchBoxField.setFont(new Font("Poppins", Font.PLAIN, 14));

        JButton salesSearchButton = new JButton("Search");
        salesSearchButton.setBounds(280, 150, 250, 36);
        salesSearchButton.setFont(new Font("Poppins", Font.PLAIN, 14));
        salesSearchButton.setForeground(Color.BLACK);
        salesSearchButton.setFocusPainted(false);
        salesSearchButton.setRolloverEnabled(false);

        DefaultTableModel salesTableModel = new DefaultTableModel();
        salesTableModel.addColumn("Bill No");
        salesTableModel.addColumn("Date");
        salesTableModel.addColumn("Time");
        salesTableModel.addColumn("Amount");
        salesTableModel.addColumn("Name");

        JTable salesTable = new JTable(salesTableModel);
        salesTable.getTableHeader().setFont(new Font("Poppins", Font.PLAIN, 14));
        salesTable.setFont(new Font("Poppins", Font.PLAIN, 14));
        salesTable.setShowGrid(false);
        salesTable.setRowHeight(salesTable.getRowHeight() + 10);
        DefaultTableCellRenderer salesDefaultHeaderRenderer = (DefaultTableCellRenderer) salesTable.getTableHeader().getDefaultRenderer();
        salesDefaultHeaderRenderer.setHorizontalAlignment(JLabel.LEFT);
        salesTable.getTableHeader().setDefaultRenderer(salesDefaultHeaderRenderer);
        final DefaultTableCellRenderer salesRenderer = new DefaultTableCellRenderer();
        salesRenderer.setBorder(null);
        salesTable.getTableHeader().setDefaultRenderer(salesRenderer);

        JScrollPane salesScrollPane = new JScrollPane(salesTable);
        salesScrollPane.setBounds(20, 200, 510, 295);

        JButton salesUpdateButton = new JButton("Update");
        salesUpdateButton.setBounds(20, 505, 250, 36);
        salesUpdateButton.setFont(new Font("Poppins", Font.PLAIN, 14));
        salesUpdateButton.setForeground(Color.BLACK);
        salesUpdateButton.setFocusPainted(false);
        salesUpdateButton.setRolloverEnabled(false);
        salesUpdateButton.setEnabled(false);

        select.addActionListener(e -> {
            int searchBy = selectFunc.getSelectedIndex();

            switch (searchBy){
                case 0:
                    JOptionPane.showMessageDialog(salesPanel, "Please select search by options");
                    break;
                case 1:// new bill
                    newBillPanel.setVisible(true);
                    addProductPanel.setVisible(false);
                    availableStockPanel.setVisible(false);
                    updateStockPanel.setVisible(false);
                    salesPanel.setVisible(false);
                    aboutPanel.setVisible(false);

                    billData = new String[1000][4];
                    noOfBillDataRow = 0;
                    productQuantity = new float[1000];

                    tableModel.setRowCount(0);
                    newBillProductQuantityField.setText("");
                    newBillProductNameField.setSelectedIndex(0);

                    newBillProductNameField.removeAllItems();
                    newBillProductNameField.addItem("---Select---");
                    String[] productNameData = Functions.getColumnData("products.txt", 0);
                    for (int i = 0; i < Functions.recordCount("products.txt"); i++) {
                        newBillProductNameField.addItem(productNameData[i]);
                    }
                    break;
                case 2://add product
                    newBillPanel.setVisible(false);
                    addProductPanel.setVisible(true);
                    availableStockPanel.setVisible(false);
                    updateStockPanel.setVisible(false);
                    salesPanel.setVisible(false);
                    aboutPanel.setVisible(false);
                    break;
                case 3://available product
                    newBillPanel.setVisible(false);
                    addProductPanel.setVisible(false);
                    availableStockPanel.setVisible(true);
                    updateStockPanel.setVisible(false);
                    salesPanel.setVisible(false);
                    aboutPanel.setVisible(false);

                    availableStockTableModel.setRowCount(0);
                    String[][] stockData = Functions.fileData("products.txt", 4);
                    for (int i = 0; i < Functions.recordCount("products.txt"); i++) {
                        availableStockTableModel.insertRow(availableStockTableModel.getRowCount(), new Object[]{
                                stockData[i][0],
                                stockData[i][1],
                                stockData[i][2],
                                stockData[i][3]
                        });
                    }
                    break;
                case 4://update product
                    newBillPanel.setVisible(false);
                    addProductPanel.setVisible(false);
                    availableStockPanel.setVisible(false);
                    updateStockPanel.setVisible(true);
                    salesPanel.setVisible(false);
                    aboutPanel.setVisible(false);

                    updateStockProductIdField.setSelectedIndex(0);
                    updateStockProductNameField.setText("");
                    updateStockProductPriceField.setText("");
                    updateStockProductStockField.setText("");
                    updateStockProductNameField.setEditable(false);
                    updateStockProductPriceField.setEditable(false);
                    updateStockProductStockField.setEditable(false);
                    updateStockDeleteButton.setEnabled(false);
                    updateStockUpdateButton.setEnabled(false);

                    updateStockProductIdField.removeAllItems();
                    updateStockProductIdField.addItem("---Select---");
                    String[] productIdData = Functions.getColumnData("products.txt", 1);
                    for (int i = 0; i < Functions.recordCount("products.txt"); i++) {
                        updateStockProductIdField.addItem(productIdData[i]);
                    }
                    break;
                case 5://sales
                    newBillPanel.setVisible(false);
                    addProductPanel.setVisible(false);
                    availableStockPanel.setVisible(false);
                    updateStockPanel.setVisible(false);
                    salesPanel.setVisible(true);
                    aboutPanel.setVisible(false);

                    salesTableModel.setRowCount(0);
                    int salesRowCount = Functions.recordCount("sales.txt");
                    String[][] salesData = Functions.fileData("sales.txt", 5);
                    for (int i = 0; i < salesRowCount; i++) {
                        salesTableModel.insertRow(salesTableModel.getRowCount(), new Object[]{
                                salesData[i][0],
                                salesData[i][1],
                                salesData[i][2],
                                salesData[i][3],
                                salesData[i][4],
                        });
                    }
                    break;
                case 6://about
                    newBillPanel.setVisible(false);
                    addProductPanel.setVisible(false);
                    availableStockPanel.setVisible(false);
                    updateStockPanel.setVisible(false);
                    salesPanel.setVisible(false);
                    aboutPanel.setVisible(true);
                    break;
                case 7://sign out
                    dashboardPanel.setVisible(false);
                    accessPanel.setVisible(true);

                    newBillPanel.setVisible(false);
                    addProductPanel.setVisible(false);
                    availableStockPanel.setVisible(false);
                    updateStockPanel.setVisible(false);
                    salesPanel.setVisible(false);
                    aboutPanel.setVisible(false);
                    break;
            }
        });

        salesSearchButton.addActionListener(e -> {
            int searchBy = salesSearchByField.getSelectedIndex();
            String salesSearchTableData = switch (searchBy) {
                case 1 -> Functions.search("sales.txt", 0, salesSearchBoxField.getText());
                case 2 -> Functions.search("sales.txt", 4, salesSearchBoxField.getText());
                case 3 -> Functions.search("sales.txt", 5, salesSearchBoxField.getText());
                default -> "";
            };

            if (salesSearchByField.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(salesPanel, "Please select search by options");
            } else if (salesSearchByField.getSelectedIndex() != 0 && !Objects.equals(salesSearchTableData, "")) {
                salesTableModel.setRowCount(0);
                salesUpdateButton.setEnabled(true);
            } else if (salesSearchBoxField.getText().equals("")) {
                JOptionPane.showMessageDialog(salesPanel, "Search box can't be empty");
            } else {
                JOptionPane.showMessageDialog(salesPanel, "Data not found");
            }

            String[] salesSearchTableDataChunk = salesSearchTableData.split(",");
            salesTableModel.insertRow(salesTableModel.getRowCount(), new Object[]{
                    salesSearchTableDataChunk[0],
                    salesSearchTableDataChunk[1],
                    salesSearchTableDataChunk[2],
                    salesSearchTableDataChunk[3],
                    salesSearchTableDataChunk[4],
                    salesSearchTableDataChunk[5],
            });
        });

        salesTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                String selectedCellValue = (String) salesTable.getValueAt(salesTable.getSelectedRow(), salesTable.getSelectedColumn());
                System.out.println(salesTable.getSelectedRow());
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
//        Sales end

        /**** About Start ****/
        JTextPane aboutTextPane = new JTextPane();
        aboutTextPane.setBounds(0, 0, 600, 600);
        aboutTextPane.setFont(new Font("Poppins", Font.PLAIN, 14));
        aboutTextPane.setMargin(new Insets(20, 20, 20, 20));
        aboutTextPane.setEditable(false);
        aboutTextPane.setText("\n\n\n\nDEVELOPED BY\n\n" +
                "\tTan Qianqian\tSWE2009514\n\n" + "\tLiu Aofan\t\tSWE2009510\n\n\n\n\n" +
                "WARNING!\n\n" + "\tThis is for internal staff of B-V Mart! If you are customers, go to \n\tthe customers version!\n");

        /****** side menu ******/

        sideMenu.add(selectFunc);
        sideMenu.add(select);

        dashboardContentLayeredPane.add(newBillPanel);
        dashboardContentLayeredPane.add(addProductPanel);
        dashboardContentLayeredPane.add(availableStockPanel);
        dashboardContentLayeredPane.add(updateStockPanel);
        dashboardContentLayeredPane.add(salesPanel);
        dashboardContentLayeredPane.add(aboutPanel);


        addProductPanel.add(addProductTitle);
        addProductPanel.add(productNameLabel);
        addProductPanel.add(productNameField);
        addProductPanel.add(productIdLabel);
        addProductPanel.add(productIdField);
        addProductPanel.add(productPriceLabel);
        addProductPanel.add(productPriceField);
        addProductPanel.add(productStockLabel);
        addProductPanel.add(productStockField);
        addProductPanel.add(addProductButton);
        addProductPanel.add(addProductClearButton);

        availableStockPanel.add(availableStockTitle);
        availableStockPanel.add(availableStockScrollPane);

        newBillPanel.add(newBillTitle);
        newBillPanel.add(newBillProductNameLabel);
        newBillPanel.add(newBillProductNameField);
        newBillPanel.add(newBillProductQuantityLabel);
        newBillPanel.add(newBillProductQuantityField);
        newBillPanel.add(newBillAddButton);
        newBillPanel.add(newBillButton);
        newBillPanel.add(newBillScrollPane);
        newBillPanel.add(newBillTotalLabel);
        newBillPanel.add(newBillClearButton);

        updateStockPanel.add(updateStockTitle);
        updateStockPanel.add(updateStockProductIdLabel);
        updateStockPanel.add(updateStockProductIdField);
        updateStockPanel.add(updateStockSelectButton);
        updateStockPanel.add(updateStockProductNameLabel);
        updateStockPanel.add(updateStockProductNameField);
        updateStockPanel.add(updateStockProductPriceLabel);
        updateStockPanel.add(updateStockProductPriceField);
        updateStockPanel.add(updateStockProductStockLabel);
        updateStockPanel.add(updateStockProductStockField);
        updateStockPanel.add(updateStockDeleteButton);
        updateStockPanel.add(updateStockUpdateButton);

        salesPanel.add(salesTitle);
        salesPanel.add(salesSearchByLabel);
        salesPanel.add(salesSearchByField);
        salesPanel.add(salesSearchBoxLabel);
        salesPanel.add(salesSearchBoxField);
        salesPanel.add(salesSearchButton);
        salesPanel.add(salesScrollPane);
        salesPanel.add(salesUpdateButton);


        aboutPanel.add(aboutTextPane);

        /***** Dashboard panel ends *****/
        containerLayeredPane.add(accessPanel);
        containerLayeredPane.add(dashboardPanel);

        accessPanel.add(accessLayeredPane, BorderLayout.CENTER);
        accessLayeredPane.add(signInPanel, BorderLayout.CENTER);
        accessPanel.add(panel1, BorderLayout.NORTH);
        accessPanel.add(panel2, BorderLayout.SOUTH);
        accessPanel.add(panel3, BorderLayout.WEST);
        accessPanel.add(panel4, BorderLayout.EAST);

        dashboardPanel.add(sideMenu, BorderLayout.WEST);
        dashboardPanel.add(dashboardContentLayeredPane, BorderLayout.EAST);

        this.add(containerLayeredPane);
        this.setVisible(true);
    }

    public static void newBillInfo() {
        float total = 0;
        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setSize(600, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Set Customers Information");

        JLabel customerInfoTitle = new JLabel("Customer Info");
        customerInfoTitle.setBounds(20, 20, 360, 25);
        customerInfoTitle.setFont(new Font("Poppins", Font.BOLD, 20));

        JLabel customerNameLabel = new JLabel("Customer name");
        customerNameLabel.setBounds(20, 65, 250, 25);
        customerNameLabel.setForeground(Color.GRAY);
        customerNameLabel.setFont(new Font("Poppins", Font.PLAIN, 14));

        JTextField customerNameField = new JTextField(50);
        customerNameField.setBounds(20, 85, 250, 36);
        customerNameField.setFont(new Font("Poppins", Font.PLAIN, 14));

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Product");
        tableModel.addColumn("Price");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Amount");

        JTable customerInfoTable = new JTable(tableModel);
        customerInfoTable.getTableHeader().setFont(new Font("Poppins", Font.PLAIN, 14));
        customerInfoTable.setFont(new Font("Poppins", Font.PLAIN, 14));
        customerInfoTable.setShowGrid(false);
        customerInfoTable.setRowHeight(customerInfoTable.getRowHeight() + 10);
        DefaultTableCellRenderer defaultHeaderRenderer = (DefaultTableCellRenderer) customerInfoTable.getTableHeader().getDefaultRenderer();
        defaultHeaderRenderer.setHorizontalAlignment(JLabel.LEFT);
        customerInfoTable.getTableHeader().setDefaultRenderer(defaultHeaderRenderer);
        final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(null);
        customerInfoTable.getTableHeader().setDefaultRenderer(renderer);

        JScrollPane customerInfoScrollPane = new JScrollPane(customerInfoTable);
        customerInfoScrollPane.setBounds(20, 140, 510, 350);

        for (int i = 0; billData[i][0] != null; i++) {
            if (!billData[i][0].equals("")) {
                tableModel.insertRow(tableModel.getRowCount(), new Object[]{billData[i][0], billData[i][1], billData[i][2], billData[i][3]});
                total = total + Float.parseFloat(billData[i][3]);
            }
        }
        tableModel.insertRow(tableModel.getRowCount(), new Object[]{"", "", "Total:", total});

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(20, 510, 250, 36);
        saveButton.setFont(new Font("Poppins", Font.PLAIN, 14));
        saveButton.setBackground(Color.BLACK);
        saveButton.setForeground(Color.BLACK);
        saveButton.setFocusPainted(false);
        saveButton.setRolloverEnabled(false);
        float finalTotal = total;
        saveButton.addActionListener(e -> {
            if (!customerNameField.getText().equals("")) {
                /***** File creation ******/
                try {
                    File myObj = new File("sales.txt");
                    if (myObj.createNewFile()) {
                        System.out.println("File created: " + myObj.getName());
                    }
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(frame, "Unable to create sales file");
                    exception.printStackTrace();
                }
                /***** writing in file *****/
                try {
                    LocalDateTime myObj = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String[] formatedDate = myObj.format(formatter).split(" ");

                    FileWriter myWriter = new FileWriter("sales.txt", true);
                    myWriter.write(
                            (Functions.recordCount("sales.txt") + 1) + ","
                                    + formatedDate[0] + ","
                                    + formatedDate[1] + ","
                                    + finalTotal + ","
                                    + customerNameField.getText() + "\n"
                    );
                    myWriter.close();
                    JOptionPane.showMessageDialog(frame, "Bill saved");
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(frame, "Error saving data");
                    exception.printStackTrace();
                }

                /******* Updating function *******/
                try {
                    File inputFile = new File("products.txt");
                    if (!inputFile.isFile()) {
                        System.out.println("File does not exist");
                        return;
                    }
                    File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");
                    BufferedReader br = new BufferedReader(new FileReader("products.txt"));
                    PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
                    String line = null;
                    //Read from the original file and write to the new
                    //unless content matches data to be removed.
                    while ((line = br.readLine()) != null) {
                        float tempProductQuantity = 0;
                        String[] token = line.split(",");
                        for (int i = 0; billData[i][0] != null; i++) {
                            if (!billData[i][0].equals("")) {
                                if (billData[i][0].equals(token[0])) {
                                    tempProductQuantity = tempProductQuantity + productQuantity[i];
                                }
                            }
                        }
                        if (tempProductQuantity > 0) {
                            pw.println(token[0] + "," + token[1] + "," + token[2] + "," + (Float.parseFloat(token[3]) - tempProductQuantity));
                            pw.flush();
                        } else {
                            pw.println(line);
                        }
                    }
                    pw.close();
                    br.close();
                    //Delete the original file
                    if (!inputFile.delete()) {
                        System.out.println("Could not delete file");
                        return;
                    }
                    //Rename the new file to the filename the original file had.
                    if (!tempFile.renameTo(inputFile))
                        System.out.println("Could not rename file");
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else if (customerNameField.getText().equals("")) {
                JOptionPane.showMessageDialog(frame, "Please enter customer name");
            }
        });

        frame.add(customerInfoTitle);
        frame.add(customerNameLabel);
        frame.add(customerNameField);
        frame.add(customerInfoScrollPane);
        frame.add(saveButton);
        frame.setVisible(true);
    }
}
