package com.springProject.DataHandling;

import java.io.*;
import java.util.ArrayList;

public class Vendor {

    public boolean authenticate(String username, String password){
        try (FileReader fileReader = new FileReader("src/Database/Vendor.txt")) {
            int character;
            StringBuilder line = new StringBuilder();
            while ((character = fileReader.read()) != -1) {
                if (character == '\n') {
                    String[] data = line.toString().split(", ");
                    if (data[0].equals(username) && data[1].equals(password)) {
                        return true;
                    }
                    line.setLength(0);
                } else {
                    line.append((char) character);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String notification(String username) {
        File notificationFile = new File("src/Database/Orders.txt");
        StringBuilder notifications = new StringBuilder();
        if (notificationFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(notificationFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(", ");
                    if (data.length > 1 && data[1].equals(username) && data.length > 4 && data[4].equals("vendor not notified")) {
                        notifications.append("You have a pending order from ").append(data[0]).append(" of ").append(data[2]).append(" in ").append(data[5]).append(".").append("\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Update the notification status after reading all lines
            updateNotificationStatus(username);
        }
        return !notifications.isEmpty() ? notifications.toString().trim() : null;
    }

    private void updateNotificationStatus(String username) {
        File inputFile = new File("src/Database/Orders.txt");
        File tempFile = new File("src/Database/Orders_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length > 4 && data[1].equals(username) && data[4].equals("vendor not notified") && data[3].equals("pending")) {
                    writer.write(line.replace("vendor not notified", "vendor notified") + System.lineSeparator());
                } else {
                    writer.write(line + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }

    public ArrayList<String> getMenu(String username) {
        ArrayList<String> menu = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Database/Menu/" + username + "Menu.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                menu.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return menu;
    }

    public void addItem(String username, String item, String price) {
        File menuFile = new File("src/Database/Menu/" + username + "Menu.txt");

        if (!menuFile.exists()) {
            try {
                menuFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(menuFile, true))) {
            writer.write(item + ", " + price + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteItem(String username, String item) {
        File inputFile = new File("src/Database/Menu/" + username + "Menu.txt");
        File tempFile = new File("src/Database/Menu/" + username + "Menu_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (!data[0].equals(item)) {
                    writer.write(line + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inputFile.delete()) {
            return false;
        }
        tempFile.renameTo(inputFile);
        return true;
    }

    public void updateItem(String username, String oldItemName, String newItemName, String newItemPrice) {
        File inputFile = new File("src/Database/Menu/" + username + "Menu.txt");
        File tempFile = new File("src/Database/Menu/" + username + "Menu_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data[0].equals(oldItemName)) {
                    writer.write(newItemName + ", " + newItemPrice + System.lineSeparator());
                } else {
                    writer.write(line + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inputFile.delete()) {
            return;
        }
        tempFile.renameTo(inputFile);
    }

    public ArrayList<String> getOrder(String username) {
        ArrayList<String> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Database/Orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length > 1 && data[1].equals(username) && data[3].equals("pending")) {
                    orders.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void acceptCancelOrder(String vendorUsername, String customerUsername, String order, String orderStatus) {
        File inputFile = new File("src/Database/Orders.txt");
        File tempFile = new File("src/Database/Orders_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length > 1 && data[1].equals(vendorUsername) && data[0].equals(customerUsername)  && line.equals(order)) {
                    data[3] = orderStatus;
                    writer.write(String.join(", ", data) + System.lineSeparator());
                } else {
                    writer.write(line + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }
}
