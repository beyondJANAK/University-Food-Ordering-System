package com.springProject.DataHandling;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Customer {

    public boolean authenticate(String username, String password){
        try (FileReader fileReader = new FileReader("src/Database/Customer.txt")) {
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

    public double getCredit(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Database/Customer.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data[0].equals(username)) {
                    return Double.parseDouble(data[3]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void deductCredit(String username, double price) {
        File inputFile = new File("src/Database/Customer.txt");
        File tempFile = new File("src/Database/Customer_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data[0].equals(username)) {
                    double newCredit = Double.parseDouble(data[3]) - price;
                    writer.write(data[0] + ", " + data[1] + ", " + data[2] + ", " + newCredit + "\n");
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

    public void placeOrder(String customerUsername, String vendorUsername, Map<String, Integer> items, double totalPrice, String status) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/Database/Orders.txt", true))) {
            StringBuilder order = new StringBuilder();
            order.append(customerUsername).append(", ")
                    .append(vendorUsername).append(", ")
                    .append(totalPrice).append(", ")
                    .append(status).append(", ")
                    .append("vendor not notified").append(", ").append(LocalDate.now()).append(", ");

            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                order.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
            }

            writer.write(order.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        deductCredit(customerUsername, totalPrice);
    }

    public ArrayList<String> getOrder(String username, boolean toCancel, boolean isDelivered) {
        ArrayList<String> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Database/Orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (!toCancel && !isDelivered && data.length > 1 && data[0].equals(username) && (data[3].equals("accepted") || data[3].equals("pending") || data[3].equals("cancelled by vendor"))) {
                    orders.add(line);
                } else if (toCancel && !isDelivered && data.length > 1 && data[0].equals(username) && data[3].equals("pending")) {
                    orders.add(line);
                } else if (!toCancel && isDelivered && data.length > 1 && data[0].equals(username) && data[3].equals("delivered")) {
                    orders.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public String checkOrderStatus(String order) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Database/Orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                ArrayList<String> getOrder = getOrder(data[0],false, false);
                if ((line.equals(getOrder.get(1)) || line.equals(getOrder.get(2)) || line.equals(getOrder.get(3))) && (line.split(", ")[3].equals("accepted") || line.split(", ")[3].equals("pending") || line.split(", ")[3].equals("cancelled by vendor"))) {
                    return getOrder.get(3) + data[3];
                } else {
                    return "No order found";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cancelOrder(String username, String order) {
        File inputFile = new File("src/Database/Orders.txt");
        File tempFile = new File("src/Database/Orders_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(order)) {
                    String[] parts = line.split(", ");
                    parts[3] = "cancelled by customer";
                    writer.write(String.join(", ", parts) + System.lineSeparator());
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

    public void addFeedback(String order, String feedback) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/Database/Feedback.txt", true))) {
            writer.write(order + " -> " + feedback + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getTransactionHistory(String username) {
        ArrayList<String> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Database/Orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length > 1 && data[0].equals(username) && data[3].equals("delivered")) {
                    transactions.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}



