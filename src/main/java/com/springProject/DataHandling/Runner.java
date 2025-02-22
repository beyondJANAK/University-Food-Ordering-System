package com.springProject.DataHandling;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Runner {

    public boolean authenticate(String username, String password){
        try (FileReader fileReader = new FileReader("src/Database/Runner.txt")) {
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

    public StringBuilder notification(String username) {
        StringBuilder notifications = new StringBuilder();
        try {
            List<String> allLines = Files.readAllLines(Paths.get("src/Database/TaskAssignedToRunner.txt"));
            int n = 10; // Number of last lines to loop through
            for (int i = Math.max(0, allLines.size() - n); i < allLines.size(); i++) {
                String[] data = allLines.get(i).split(" -> ");
                if (data.length > 1 && data[0].equals(username) && data[1].equals("assigned")) {
                    notifications.append("You have been assigned a new task: ").append(data[2]).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public ArrayList<String> getTasks(String runnerUsername, boolean updatable, boolean isDelivered) {
        ArrayList<String> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Database/TaskAssignedToRunner.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" -> ");
                if (data.length > 2 && data[0].equals(runnerUsername) && data[1].equals("assigned") && !updatable && !isDelivered) {
                    tasks.add(line);
                }
                if (!isDelivered && updatable && data.length > 2 && data[0].equals(runnerUsername) && !data[1].equals("delivered") && !data[1].equals("declined") && !data[1].equals("assigned")) {
                    tasks.add(line);
                } if(isDelivered && data.length > 2 && data[0].equals(runnerUsername) && data[1].equals("delivered")) {
                    tasks.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public void acceptDeclineTask(String runnerUsername, String order, String orderStatus) {
            File inputFile = new File("src/Database/TaskAssignedToRunner.txt");
            File tempFile = new File("src/Database/TaskAssignedToRunner_temp.txt");

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(" -> ");
                    if (data.length > 1 && data[0].equals(runnerUsername) && data[2].equals(order)) {
                        writer.write(runnerUsername + " -> " + orderStatus + " -> " + order + System.lineSeparator());
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

    public void updateTaskStatus(String runnerUsername, String order, String orderStatus) {
        File inputFile = new File("src/Database/TaskAssignedToRunner.txt");
        File tempFile = new File("src/Database/TaskAssignedToRunner_temp.txt");
        File orderInputFile = new File("src/Database/Orders.txt");
        File orderTempFile = new File("src/Database/Orders_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
             BufferedReader orderReader = new BufferedReader(new FileReader(orderInputFile));
             BufferedWriter orderWriter = new BufferedWriter(new FileWriter(orderTempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" -> ");
                if (data.length > 1 && data[0].equals(runnerUsername) && !data[1].equals("delivered") && data[2].equals(order)) {
                    writer.write(runnerUsername + " -> " + orderStatus + " -> " + order + System.lineSeparator());
                } else {
                    writer.write(line + System.lineSeparator());
                }
            }

            while ((line = orderReader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length > 1 && !data[3].equals("delivered") && data[0].equals(order.split(", ")[0]) && data[1].equals(order.split(", ")[1]) && data[2].equals(order.split(", ")[2]) && data[4].equals(order.split(", ")[4]) && data[5].equals(order.split(", ")[5]) && data[6].equals(order.split(", ")[6])) {
                    orderWriter.write(data[0] + ", " + data[1] + ", " + data[2] + ", " + orderStatus + ", " + data[4] + ", " + data[5] + ", " + data[6] + System.lineSeparator());
                } else {
                    orderWriter.write(line + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
        orderInputFile.delete();
        orderTempFile.renameTo(orderInputFile);
    }

    public ArrayList<String> getReview(String runnerName) {
        ArrayList<String> reviews = new ArrayList<>();
        ArrayList<String> deliveredOrders = getTasks(runnerName, false, true);
        try (BufferedReader reviewReader = new BufferedReader(new FileReader("src/Database/Feedback.txt"))) {
            String line;
            int count = 0;
            while ((line = reviewReader.readLine()) != null && count < deliveredOrders.size()) {
                String[] data = line.split(" -> ");
                if (data.length > 1 && data[0].equals(deliveredOrders.get(count).split(" -> ")[2])) {
                    reviews.add(line);
                }
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
