package com.server;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Functions {
    //    this method return the no of rows/tuples in any file
    public static int recordCount(String fileName) {
        int count = 0;
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNext()) {
                String data = myReader.nextLine();
                count++;
            }
            myReader.close();
        } catch (FileNotFoundException exception) {
            System.out.println(fileName + "not found");
            exception.printStackTrace();
        }
        return count;
    }

    //    this method return the whole file data of any file
    public static String[][] fileData(String fileName, int noOfColumns) {
        int rowCount = Functions.recordCount(fileName);
        String[][] fileData = new String[rowCount][noOfColumns];
        try {
            int i = 0;
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNext()) {
                String data = myReader.nextLine();
                String[] token = data.split(",");
                for (int j = 0; j < noOfColumns; j++) {
                    fileData[i][j] = token[j];
                }
                i++;
            }
            myReader.close();
        } catch (FileNotFoundException exception) {
            System.out.println(fileName + "not found");
            exception.printStackTrace();
        }
        return fileData;
    }

    //    this method return the data of any column of any file
    public static String[] getColumnData(String fileName, int columnIndex) {
        String[] columnData = new String[Functions.recordCount(fileName)];
        try {
            int i = 0;
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNext()) {
                String data = myReader.nextLine();
                String[] token = data.split(",");
                columnData[i] = token[columnIndex];
                i++;
            }
            myReader.close();
        } catch (FileNotFoundException exception) {
            System.out.println(fileName + "not found");
            exception.printStackTrace();
        }
        return columnData;
    }

    //    this method search the key in any file and return the correponding key data
    public static String search(String fileName, int keyDataIndex, String key) {
        String searchedData = "";
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNext()) {
                String data = myReader.nextLine();
                String[] token = data.split(",");
                if (key.equals(token[keyDataIndex])) {
                    searchedData = data;
                }
            }
            myReader.close();
        } catch (FileNotFoundException exception) {
            System.out.println(fileName + "not found");
            exception.printStackTrace();
        }
        return searchedData;
    }

    public static void writeTxt(Frame frame, Integer finalTotal,JTextField customerNameField,String[][] billData,float[] productQuantity){
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
    }
}
