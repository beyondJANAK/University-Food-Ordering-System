package com.springProject.DataHandling;

import java.io.*;
import java.util.ArrayList;

public class Admin {

    public boolean authenticate(String username, String password){
        return username.equals("admin") && password.equals("admin");
    }


    public void saveCustomer(String username, String password, String email){
        try (FileWriter writer = new FileWriter("src/Database/Customer.txt", true)) {
            writer.write(username + ", " + password + ", " + email + ", " + "1000" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteCustomer(String username) {
        File inputFile = new File("src/Database/Customer.txt");
        File tempFile = new File("src/Database/Customer_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (!data[0].equals(username)) {
                    writer.write(line + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!inputFile.delete()) {
            return false;
        }
        return tempFile.renameTo(inputFile);
    }

    public boolean topupCredit(String username, String credit) {
        File inputFile = new File("src/Database/Customer.txt");
        File tempFile = new File("src/Database/Customer_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data[0].equals(username)) {
                    try {
                        int newCredit = (int) Double.parseDouble(data[3]) + Integer.parseInt(credit);
                        writer.write(data[0] + ", " + data[1] + ", " + data[2] + ", " + newCredit + "\n");
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return false;
                    }
                } else {
                    writer.write(line + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!inputFile.delete()) {
            return false;
        }
        return tempFile.renameTo(inputFile);
    }

    public ArrayList<String> getAllCustomer() {
        ArrayList<String> usernames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Database/Customer.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length > 0) {
                    usernames.add(data[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usernames;
    }

    public void saveVendor(String username, String password, String email){
        try (FileWriter writer = new FileWriter("src/Database/Vendor.txt", true)) {
            writer.write(username + ", " + password + ", " + email + ", " + "0" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getAllVendor() {
        ArrayList<String> usernames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Database/Vendor.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length > 0) {
                    usernames.add(data[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usernames;
    }

    public boolean deleteVendor(String username) {
        File inputFile = new File("src/Database/Vendor.txt");
        File tempFile = new File("src/Database/Vendor_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (!data[0].equals(username)) {
                    writer.write(line + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!inputFile.delete()) {
            return false;
        }
        return tempFile.renameTo(inputFile);
    }

    public void saveRunner(String username, String password, String email){
        try (FileWriter writer = new FileWriter("src/Database/Runner.txt", true)) {
            writer.write(username + ", " + password + ", " + email + ", " + "0" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getAllRunner() {
        ArrayList<String> usernames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Database/Runner.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length > 0) {
                    usernames.add(data[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usernames;
    }

    public boolean deleteRunner(String username) {
        File inputFile = new File("src/Database/Runner.txt");
        File tempFile = new File("src/Database/Runner_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (!data[0].equals(username)) {
                    writer.write(line + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!inputFile.delete()) {
            return false;
        }
        return tempFile.renameTo(inputFile);
    }
}
