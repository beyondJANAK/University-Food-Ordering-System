package com.springProject.View;

import com.springProject.DataHandling.Runner;
import com.springProject.DataHandling.Vendor;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class RunnerWindow extends Components {

    MyFrame frame;
    JButton button;
    JTextField usernameField;
    JPanel currentPanel;
    JScrollPane currentScrollPane;

    Runner runner = new Runner();
    Vendor vendor = new Vendor();

    public RunnerWindow() {
        frame = new MyFrame("Runner Login");

        super.backButton(frame);

        JLabel runnerLabel = new JLabel("Enter credentials: ");
        runnerLabel.setBounds(230, 150, 150, 30);
        runnerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        runnerLabel.setForeground(Color.WHITE);
        frame.add(runnerLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(110, 230, 100, 30);
        frame.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(210, 230, 200, 30);
        frame.add(usernameField);


        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(110, 280, 100, 30);
        frame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(210, 280, 200, 30);
        frame.add(passwordField);

        button = new JButton("Login");
        button.setBounds(245, 330, 100, 42);
        button.setFocusable(false);
        button.setBackground(new Color(17, 72, 125));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEtchedBorder());
        button.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            boolean success = runner.authenticate(username, password);
            if (success) {
                frame.dispose();
                runnerWork();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        frame.add(button);

        frame.setVisible(true);
    }

    private void runnerWork() {
        frame = new MyFrame("Runner Dashboard");

        super.backButton(frame);

        super.addButtonToFrame(frame, "Accept/Decline Task", 85, e -> acceptDeclineTask(frame));
        super.addButtonToFrame(frame, "Update Task Status", 135, e -> updateTaskStatus(frame));
        super.addButtonToFrame(frame, "Check Task History", 185, e -> checkTaskHistory(frame));
        super.addButtonToFrame(frame, "Read Review", 235, e -> readReview(frame));
        super.addButtonToFrame(frame, "Revenue DashBoard", 285, e -> revenueDashboard(frame));

        frame.setVisible(true);

        runnerNotification(frame);
    }


    private void runnerNotification(MyFrame frame) {
        StringBuilder message = runner.notification(usernameField.getText());
        if (message != null && !message.toString().isEmpty()) JOptionPane.showMessageDialog(frame, message.toString(), "Notifications", JOptionPane.INFORMATION_MESSAGE);
    }

    private void acceptDeclineTask(MyFrame frame) {

        if (currentPanel != null) frame.getContentPane().remove(currentScrollPane);
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);

        // Implementation for accepting or declining tasks
        JPanel panel = super.createPanel("Accept/Decline Task", 190, 20, 400, 550);

        // Wrap panel inside a scroll pane with conditional scrolling
        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(190, 20, 450, 500);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(frame.getContentPane().getBackground());

        // Example task list
        ArrayList<String> tasks = runner.getTasks(usernameField.getText(), false, false);
        if (tasks == null || tasks.isEmpty()) {
            JLabel noTasksLabel = new JLabel("No tasks available.");
            noTasksLabel.setBounds(0, 65, 400, 45);
            noTasksLabel.setForeground(Color.WHITE);
            panel.add(noTasksLabel);
        } else {
            int yPosition = 67;
            int maxHeight = 20; // Track max height to adjust scrolling if needed
            int count = 1;

            for (String task : tasks) {
                String[] taskParts = task.split(" -> ");

                JLabel taskLabel = new JLabel("<html><pre>" + count + ". Task: " + taskParts[2].split(", ")[6] + "<br>   Customer: " + taskParts[2].split(", ")[0] + ".<br>   Vendor: " + taskParts[2].split(", ")[1] + ".</pre></html>");
                taskLabel.setBounds(0, yPosition, 300, 45);
                taskLabel.setForeground(Color.WHITE);
                panel.add(taskLabel);

                JButton acceptButton = new JButton("Accept");
                JButton declineButton = new JButton("Decline");
                acceptButton.setBounds(302, yPosition, 80, 20);
                acceptButton.setBorder(BorderFactory.createEtchedBorder());
                acceptButton.setBackground(new Color(17, 72, 125));
                acceptButton.setForeground(Color.WHITE);
                acceptButton.setFocusable(false);
                acceptButton.addActionListener(e -> {
                    runner.acceptDeclineTask(usernameField.getText(), taskParts[2], "accepted");
                    JOptionPane.showMessageDialog(frame, "Task accepted successfully");
                    acceptDeclineTask(frame);
                });
                panel.add(acceptButton);

                declineButton.setBounds(302, yPosition + 24, 80, 20);
                declineButton.setBorder(BorderFactory.createEtchedBorder());
                declineButton.setBackground(new Color(220, 53, 69));
                declineButton.setForeground(Color.WHITE);
                declineButton.setFocusable(false);
                declineButton.addActionListener(e -> {
                    runner.acceptDeclineTask(usernameField.getText(), taskParts[2], "declined");
                    vendor.assignTaskToRunner(taskParts[2].split(", ")[1], taskParts[2]);
                    JOptionPane.showMessageDialog(frame, "Task declined successfully");
                    acceptDeclineTask(frame);
                });
                panel.add(declineButton);

                count++;
                yPosition += 55;
                maxHeight = yPosition; // Update max height
            }

            // Ensure the panel expands only if needed (for scrolling)
            panel.setPreferredSize(new Dimension(400, Math.max(400, maxHeight)));
        }

        frame.getContentPane().add(scrollPane, JLayeredPane.POPUP_LAYER);
        currentPanel = panel;
        currentScrollPane = scrollPane;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void updateTaskStatus(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentScrollPane);
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);

        JPanel panel = super.createPanel("Update Task Status", 190, 20, 400, 550);

        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(190, 20, 450, 500);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(frame.getContentPane().getBackground());

        ArrayList<String> tasks = runner.getTasks(usernameField.getText(), true, false);
        int yPosition = 67;
        int maxHeight = 20; // Track max height to adjust scrolling if needed
        int count = 1;

        for (String task : tasks) {
            String[] taskParts = task.split(" -> ");
            JLabel taskLabel = new JLabel("<html><pre>" + count + ". Task: " + taskParts[2].split(", ")[6] + "<br>   Customer: " + taskParts[2].split(", ")[0] + ".<br>   Vendor: " + taskParts[2].split(", ")[1] + ".</pre></html>");
            taskLabel.setBounds(0, yPosition, 300, 45);
            taskLabel.setForeground(Color.WHITE);
            panel.add(taskLabel);
            count++;

            JButton updateStatusButton = new JButton("Update Status");
            updateStatusButton.setBounds(290, yPosition, 95, 25);
            updateStatusButton.setFocusable(false);
            updateStatusButton.setBackground(new Color(17, 72, 125));
            updateStatusButton.setForeground(Color.WHITE);
            updateStatusButton.setBorder(BorderFactory.createEtchedBorder());
            updateStatusButton.addActionListener(e -> {
                String[] statuses = {"being prepared", "being delivered", "at your location", "delivered"};
                String newStatus = (String) JOptionPane.showInputDialog(frame, "Select new status:", "Update Order Status", JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);
                if (newStatus != null) {
                    runner.updateTaskStatus(usernameField.getText(), task.split(" -> ")[2], newStatus);
                    JOptionPane.showMessageDialog(frame, "Order status updated successfully!");
                    updateTaskStatus(frame); // Refresh the order list
                }
            });
            panel.add(updateStatusButton);

            yPosition += 60;
            maxHeight = yPosition; // Update max height
        }

        panel.setPreferredSize(new Dimension(400, Math.max(400, maxHeight)));

        frame.getContentPane().add(scrollPane, JLayeredPane.POPUP_LAYER);
        currentPanel = panel;
        currentScrollPane = scrollPane;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void checkTaskHistory(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);
        if (currentScrollPane != null) frame.getContentPane().remove(currentScrollPane);

        JPanel panel = super.createPanel("Task History", 130, 40, 400, 550);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Task History", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        panel.setLayout(null);

        // Wrap panel inside a scroll pane with conditional scrolling
        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(190, 20, 450, 500);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(frame.getContentPane().getBackground());

        // Filter options
        String[] filterOptions = {"All", "Daily", "Monthly", "Quarterly"};
        JComboBox<String> filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.setBounds(7, 25, 100, 30);
        panel.add(filterComboBox);

        filterComboBox.addActionListener(e -> {
            String selectedFilter = (String) filterComboBox.getSelectedItem();
            ArrayList<String> orders = new ArrayList<>();
            // Implement quarterly filter logic
            assert selectedFilter != null;
            switch (selectedFilter) {
                case "All" -> {
                    orders = runner.getTasks(usernameField.getText(), false, true);
                    displayOrders(panel, orders);
                }

                case "Daily" -> {
                    ArrayList<String> dailyOrders = new ArrayList<>();
                    for (String order : runner.getTasks(usernameField.getText(), false, true)) {
                        if (isToday(order.split(" -> ")[2].split(", ")[5])) {
                            dailyOrders.add(order);
                        }
                    }
                    displayOrders(panel, dailyOrders);
                }
                case "Monthly" -> {
                    ArrayList<String> monthlyOrders = new ArrayList<>();
                    for (String order : runner.getTasks(usernameField.getText(), false, true)) {
                        if (isThisMonth(order.split(" -> ")[2].split(", ")[5])) {
                            monthlyOrders.add(order);
                        }
                    }
                    displayOrders(panel, monthlyOrders);
                }
                case "Quarterly" -> {
                    ArrayList<String> quarterlyOrders = new ArrayList<>();
                    for (String order : runner.getTasks(usernameField.getText(), false, true)) {
                        if (isThisQuarter(order.split(" -> ")[2].split(", ")[5])) {
                            quarterlyOrders.add(order);
                        }
                    }
                    displayOrders(panel, quarterlyOrders);
                }
                default -> {
                    orders = runner.getTasks(usernameField.getText(), false, true);
                    displayOrders(panel, orders);
                }
            }
        });

        // Display all orders initially
        ArrayList<String> allOrders = runner.getTasks(usernameField.getText(), false, true);
        displayOrders(panel, allOrders);

        frame.getContentPane().add(scrollPane, JLayeredPane.POPUP_LAYER);
        currentPanel = panel;
        currentScrollPane = scrollPane;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void displayOrders(JPanel panel, ArrayList<String> orders) {
        // Remove only order-related components, not the filterComboBox
        Component[] components = panel.getComponents();
        for (Component c : components) {
            if (c instanceof JLabel) {
                panel.remove(c);
            }
        }

        int yPosition = 72; // Start below the combobox
        int maxHeight = yPosition;

        int count = 1;
        for (String order : orders) {
            String[] taskParts = order.split(" -> ");
            JLabel orderLabel = new JLabel("<html><pre>" + count + ". Task: " + taskParts[2].split(", ")[6] + " of $" + taskParts[2].split(", ")[2] + "<br>   Customer: " + taskParts[2].split(", ")[0] + ". Vendor: " + taskParts[2].split(", ")[1] + ".</pre></html>");
            orderLabel.setBounds(0, yPosition, 300, 45);
            orderLabel.setForeground(Color.WHITE);
            panel.add(orderLabel);
            count++;

            yPosition += 55;
            maxHeight = yPosition;
        }

        panel.setPreferredSize(new Dimension(400, Math.max(400, maxHeight)));
        panel.revalidate();
        panel.repaint();
    }

    private boolean isToday(String date) {
        LocalDate inputDate = LocalDate.parse(date);
        LocalDate today = LocalDate.now();
        return inputDate.isEqual(today);
    }
    private boolean isThisMonth(String date) {
        LocalDate inputDate = LocalDate.parse(date);
        YearMonth currentMonth = YearMonth.now();
        YearMonth inputMonth = YearMonth.from(inputDate);
        return inputMonth.equals(currentMonth);
    }
    private boolean isThisQuarter(String date) {
        LocalDate inputDate = LocalDate.parse(date);
        LocalDate now = LocalDate.now();
        int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
        int inputQuarter = (inputDate.getMonthValue() - 1) / 3 + 1;
        return inputDate.getYear() == now.getYear() && inputQuarter == currentQuarter;
    }

    private void readReview(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentScrollPane);
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);

        JPanel panel = super.createPanel("Read Review", 190, 20, 400, 550);

        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(190, 20, 450, 500);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(frame.getContentPane().getBackground());

        ArrayList<String> reviews = runner.getReview(usernameField.getText());
        int yPosition = 65;
        int maxHeight = 20; // Track max height to adjust scrolling if needed
        int count = 1;

        for (String review : reviews) {
            String[] reviewParts = review.split(" -> ");
            JLabel reviewLabel = new JLabel("<html><pre>" + count + ". Order: " + reviewParts[0].split(", ")[6] + " from " + reviewParts[0].split(", ")[0] + ". <br>   Review: " + reviewParts[1] + "</pre></html>");
            reviewLabel.setBounds(0, yPosition, 300, 45);
            reviewLabel.setForeground(Color.WHITE);
            panel.add(reviewLabel);
            count++;

            yPosition += 55;
            maxHeight = yPosition; // Update max height
        }

        panel.setPreferredSize(new Dimension(400, Math.max(400, maxHeight)));

        frame.getContentPane().add(scrollPane, JLayeredPane.POPUP_LAYER);
        currentPanel = panel;
        currentScrollPane = scrollPane;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void revenueDashboard(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);
        if (currentScrollPane != null) frame.getContentPane().remove(currentScrollPane);

        JPanel panel = super.createPanel("Revenue Dashboard", 130, 40, 400, 550);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Revenue Dashboard", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        panel.setLayout(null);

        // Wrap panel inside a scroll pane with conditional scrolling
        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(185, 20, 450, 500);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(frame.getContentPane().getBackground());

        // Filter options
        String[] filterOptions = {"All", "Daily", "Monthly", "Quarterly"};
        JComboBox<String> filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.setBounds(7, 25, 100, 30);
        panel.add(filterComboBox);

        filterComboBox.addActionListener(e -> {
            String selectedFilter = (String) filterComboBox.getSelectedItem();
            ArrayList<String> orders = new ArrayList<>();
            assert selectedFilter != null;
            switch (selectedFilter) {
                case "All" -> {
                    orders = runner.getTasks(usernameField.getText(), false, true);
                    displayRevenue(panel, orders, filterComboBox);
                }
                case "Daily" -> {
                    ArrayList<String> dailyOrders = new ArrayList<>();
                    for (String order : runner.getTasks(usernameField.getText(), false, true)) {
                        if (isToday(order.split(", ")[5])) {
                            dailyOrders.add(order);
                        }
                    }
                    displayRevenue(panel, dailyOrders, filterComboBox);
                }
                case "Monthly" -> {
                    ArrayList<String> monthlyOrders = new ArrayList<>();
                    for (String order : runner.getTasks(usernameField.getText(), false, true)) {
                        if (isThisMonth(order.split(", ")[5])) {
                            monthlyOrders.add(order);
                        }
                    }
                    displayRevenue(panel, monthlyOrders, filterComboBox);
                }
                case "Quarterly" -> {
                    ArrayList<String> quarterlyOrders = new ArrayList<>();
                    for (String order : runner.getTasks(usernameField.getText(), false, true)) {
                        if (isThisQuarter(order.split(", ")[5])) {
                            quarterlyOrders.add(order);
                        }
                    }
                    displayRevenue(panel, quarterlyOrders, filterComboBox);
                }
                default -> {
                    orders = runner.getTasks(usernameField.getText(), false, true);
                    displayRevenue(panel, orders, filterComboBox);
                }
            }
        });

        // Display all orders initially
        ArrayList<String> allOrders = runner.getTasks(usernameField.getText(), false, true);
        displayRevenue(panel, allOrders, filterComboBox);

        frame.getContentPane().add(scrollPane, JLayeredPane.POPUP_LAYER);
        currentPanel = panel;
        currentScrollPane = scrollPane;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void displayRevenue(JPanel panel, ArrayList<String> orders, JComboBox<String> filterComboBox) {
        Component[] components = panel.getComponents();
        for (Component c : components) {
            if (c instanceof JLabel && !c.equals(filterComboBox)) {
                panel.remove(c);
            }
        }

        int yPosition = 72; // Start below the combobox
        int maxHeight = yPosition;
        double totalRevenue = 0.0;

        int count = 1;
        for (String order : orders) {
            double orderRevenue = Double.parseDouble(order.split(", ")[2]) * 0.2; // Deduct 20%
            totalRevenue += orderRevenue;

            JLabel orderLabel = new JLabel("<html><pre>" + count +  ". In " + order.split(", ")[5] + " " + order.split(", ")[0] + " ordered <br>" + "   " + Arrays.toString(order.split(", ")[6].split(";")) +
                    " for $" + order.split(", ")[2] + ". <br>=> Revenue : $" + orderRevenue + ".</pre></html>");
            orderLabel.setBounds(0, yPosition, 400, 55);
            orderLabel.setForeground(Color.WHITE);
            panel.add(orderLabel);
            count++;

            yPosition += 65;
            maxHeight = yPosition;
        }

        JLabel totalRevenueLabel = new JLabel("Total Revenue: $" + totalRevenue);
        totalRevenueLabel.setBounds(0, yPosition, 300, 45);
        totalRevenueLabel.setForeground(new Color(10, 91, 28)); // Green color
        totalRevenueLabel.setFont(new Font("Arial", Font.BOLD, 15)); // Bold and bigger
        panel.add(totalRevenueLabel);

        panel.setPreferredSize(new Dimension(400, Math.max(400, maxHeight)));
        panel.revalidate();
        panel.repaint();
    }
}


