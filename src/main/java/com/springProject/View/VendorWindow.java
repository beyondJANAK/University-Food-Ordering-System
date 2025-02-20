package com.springProject.View;

import com.springProject.DataHandling.Vendor;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class VendorWindow extends Components {
    MyFrame frame;
    JButton button;
    JTextField usernameField;
    JPanel currentPanel;
    JScrollPane currentScrollPane;
    Vendor vendor = new Vendor();

    public VendorWindow() {
        frame = new MyFrame("Vendor Login");

        super.backButton(frame);

        JLabel vendorLabel = new JLabel("Enter credentials: ");
        vendorLabel.setBounds(230, 150, 150, 30);
        vendorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        vendorLabel.setForeground(Color.WHITE);
        frame.add(vendorLabel);

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
        button.setBounds(235, 325, 130, 50);
        button.setFocusable(false);
        button.setBackground(new Color(60, 61, 55));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEtchedBorder());
        button.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            boolean success = vendor.authenticate(username, password);
            if (success) {
                frame.dispose();
                vendorWork();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(button);

        frame.setVisible(true);
    }


    private void vendorWork() {
        frame = new MyFrame("Vendor Dashboard");

        super.backButton(frame);

        super.addButtonToFrame(frame, "Add/Update Item", 85, e -> addUpdateItem(frame));
        super.addButtonToFrame(frame, "Accept/Cancel Order", 135, e -> acceptCancelOrder(frame));
        super.addButtonToFrame(frame, "Update Order Status", 185, e -> updateOrderStatus(frame));
        super.addButtonToFrame(frame, "Check Order History", 235, e -> orderHistory(frame));
        super.addButtonToFrame(frame, "Read Review", 285, e -> readReview(frame));
        super.addButtonToFrame(frame, "Revenue DashBoard", 335, e -> revenueDashboard(frame));

        frame.setVisible(true);

        vendorNotification(frame);
    }

    private void vendorNotification(MyFrame frame) {
        String notification = vendor.notification(usernameField.getText());
        if (notification != null) {
            JOptionPane.showMessageDialog(frame, notification, "Notification", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addUpdateItem(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);
        if (currentScrollPane != null) frame.getContentPane().remove(currentScrollPane);

        JPanel panel = super.createPanel(null, 200, 20, 400, 550); // Your existing panel

        // Wrap panel inside a scroll pane with conditional scrolling
        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(200, 20, 450, 500);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(frame.getContentPane().getBackground());

        JButton addItemButton = new JButton("Add Item");
        addItemButton.setBounds(60, 20, 90, 32);
        addItemButton.setFocusable(false);
        addItemButton.setBorder(BorderFactory.createEtchedBorder());
        addItemButton.addActionListener(e -> showAddItemPopup());
        panel.add(addItemButton);

        ArrayList<String> menu = vendor.getMenu(usernameField.getText());
        int yPosition = 65;
        int maxHeight = 65; // Track max height to adjust scrolling if needed

        for (String item : menu) {
            JLabel itemLabel = new JLabel(item.split(", ")[0] + " - $" + item.split(", ")[1]);
            itemLabel.setBounds(60, yPosition, 190, 30);
            itemLabel.setForeground(Color.WHITE);
            panel.add(itemLabel);

            JButton updateButton = new JButton("Update");
            updateButton.setBounds(200, yPosition, 70, 25);
            updateButton.setFocusable(false);
            updateButton.setBorder(BorderFactory.createEtchedBorder());
            updateButton.addActionListener(e -> showUpdateItemPopup(item));
            panel.add(updateButton);

            JButton deleteButton = new JButton("Delete");
            deleteButton.setBounds(280, yPosition, 70, 25);
            deleteButton.setFocusable(false);
            deleteButton.setBorder(BorderFactory.createEtchedBorder());
            deleteButton.addActionListener(e -> {
                boolean success = vendor.deleteItem(usernameField.getText(), item.split(", ")[0]);
                if(success) {
                    panel.remove(itemLabel);
                    panel.remove(updateButton);
                    panel.remove(deleteButton);
                    panel.revalidate();
                    panel.repaint();
                    JOptionPane.showMessageDialog(frame, "Item deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Item deletion failed!");
                }
            });
            panel.add(deleteButton);

            yPosition += 50;
            maxHeight = yPosition; // Update max height
        }

        // Ensure the panel expands only if needed (for scrolling)
        panel.setPreferredSize(new Dimension(400, Math.max(400, maxHeight)));

        frame.getContentPane().add(scrollPane, JLayeredPane.POPUP_LAYER);
        currentScrollPane = scrollPane;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void showAddItemPopup() {
        JTextField itemNameField = new JTextField();
        JTextField itemPriceField = new JTextField();
        Object[] message = {
                "Item Name:", itemNameField,
                "Item Price:", itemPriceField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Add New Item", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String itemName = itemNameField.getText();
            String itemPrice = itemPriceField.getText();
            vendor.addItem(usernameField.getText(), itemName, itemPrice);
            JOptionPane.showMessageDialog(frame, "Item added successfully!");
            addUpdateItem(frame); // Refresh the item list
        }
    }

    private void acceptCancelOrder(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);
        if (currentScrollPane != null) frame.getContentPane().remove(currentScrollPane);

        JPanel panel = super.createPanel("Orders", 190, 20, 400, 550); // Your existing panel

        // Wrap panel inside a scroll pane with conditional scrolling
        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(190, 20, 450, 500);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(frame.getContentPane().getBackground());

        ArrayList<String> orders = vendor.getOrder(usernameField.getText(), true, false);
        int yPosition = 65;
        int maxHeight = 20; // Track max height to adjust scrolling if needed
        int count = 1;

        for (String order : orders) {
            JLabel orderLabel = new JLabel("<html><pre>" + count +  ". In " + order.split(", ")[5] + " " + order.split(", ")[0] + " ordered <br>" + "   " + Arrays.toString(order.split(", ")[6].split(";")) +
                                            "<br>   for $" + order.split(", ")[2] + ".</pre></html>");
            orderLabel.setBounds(0, yPosition, 300, 45);
            orderLabel.setForeground(Color.WHITE);
            panel.add(orderLabel);
            count++;

            JButton acceptButton = new JButton("Accept");
            acceptButton.setBounds(302, yPosition, 80, 20);
            acceptButton.setFocusable(false);
            acceptButton.setBorder(BorderFactory.createEtchedBorder());
            acceptButton.addActionListener(e -> {
                vendor.acceptCancelOrder(usernameField.getText(), order.split(", ")[0], order, "accepted");
                JOptionPane.showMessageDialog(frame, "Order accepted successfully!");
                acceptCancelOrder(frame); // Refresh the order list
            });
            panel.add(acceptButton);

            JButton cancelButton = new JButton("Cancel");
            cancelButton.setBounds(302, yPosition + 22, 80, 20);
            cancelButton.setFocusable(false);
            cancelButton.setBorder(BorderFactory.createEtchedBorder());
            cancelButton.addActionListener(e -> {
                vendor.acceptCancelOrder(usernameField.getText(), order.split(", ")[0], order, "cancelled by vendor");
                JOptionPane.showMessageDialog(frame, "Order cancelled successfully!");
                acceptCancelOrder(frame); // Refresh the order list
            });
            panel.add(cancelButton);

            yPosition += 55;
            maxHeight = yPosition; // Update max height
        }

        // Ensure the panel expands only if needed (for scrolling)
        panel.setPreferredSize(new Dimension(400, Math.max(400, maxHeight)));

        frame.getContentPane().add(scrollPane, JLayeredPane.POPUP_LAYER);
        currentPanel = panel;
        currentScrollPane = scrollPane;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void showUpdateItemPopup(String item) {
        String[] itemDetails = item.split(", ");
        JTextField itemNameField = new JTextField(itemDetails[0]);
        JTextField itemPriceField = new JTextField(itemDetails[1]);
        Object[] message = {
                "Item Name:", itemNameField,
                "Item Price:", itemPriceField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Update Item", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String newItemName = itemNameField.getText();
            String newItemPrice = itemPriceField.getText();
            vendor.updateItem(usernameField.getText(), itemDetails[0], newItemName, newItemPrice);
            JOptionPane.showMessageDialog(frame, "Item updated successfully!");
            addUpdateItem(frame); // Refresh the item list
        }
    }


    private void updateOrderStatus(MyFrame frame) {
        if(currentPanel != null) frame.getContentPane().remove(currentPanel);
        if(currentPanel != null) frame.getContentPane().remove(currentScrollPane);

        JPanel panel = super.createPanel("Orders", 190, 20, 400, 550); // Your existing panel

        // Wrap panel inside a scroll pane with conditional scrolling
        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(190, 20, 450, 500);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(frame.getContentPane().getBackground());

        ArrayList<String> orders = vendor.getOrder(usernameField.getText(), false, false);
        int yPosition = 67;
        int maxHeight = 20; // Track max height to adjust scrolling if needed
        int count = 1;

        for (String order : orders) {
            JLabel orderLabel = new JLabel("<html><pre>" + count +  ". In " + order.split(", ")[5] + " " + order.split(", ")[0] + " ordered <br>" + "   " + Arrays.toString(order.split(", ")[6].split(";")) +
                                            "<br>   for $" + order.split(", ")[2] + ".</pre></html>");
            orderLabel.setBounds(0, yPosition, 300, 45);
            orderLabel.setForeground(Color.WHITE);
            panel.add(orderLabel);
            count++;

            JButton updateStatusButton = new JButton("Update Status");
            updateStatusButton.setBounds(290, yPosition, 95, 25);
            updateStatusButton.setFocusable(false);
            updateStatusButton.setBorder(BorderFactory.createEtchedBorder());
            updateStatusButton.addActionListener(e -> {
                String[] statuses = {"being prepared", "being delivered", "taken by runner", "delivered"};
                String newStatus = (String) JOptionPane.showInputDialog(frame, "Select new status:", "Update Order Status", JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);
                if (newStatus != null) {
                    vendor.acceptCancelOrder(usernameField.getText(), order.split(", ")[0], order, newStatus);
                    JOptionPane.showMessageDialog(frame, "Order status updated successfully!");
                    updateOrderStatus(frame); // Refresh the order list
                }
                updateOrderStatus(frame); // Refresh the order list
            });
            panel.add(updateStatusButton);

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


    private void orderHistory(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);
        if (currentScrollPane != null) frame.getContentPane().remove(currentScrollPane);

        JPanel panel = super.createPanel("Order History", 130, 40, 400, 550);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Order History", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.WHITE));
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
                    orders = vendor.getOrder(usernameField.getText(), false, true);
                    displayOrders(panel, orders);
                }

                case "Daily" -> {
                    ArrayList<String> dailyOrders = new ArrayList<>();
                    for (String order : vendor.getOrder(usernameField.getText(), false, true)) {
                        if (isToday(order.split(", ")[5])) {
                            dailyOrders.add(order);
                        }
                    }
                    displayOrders(panel, dailyOrders);
                }
                case "Monthly" -> {
                    ArrayList<String> monthlyOrders = new ArrayList<>();
                    for (String order : vendor.getOrder(usernameField.getText(), false, true)) {
                        if (isThisMonth(order.split(", ")[5])) {
                            monthlyOrders.add(order);
                        }
                    }
                    displayOrders(panel, monthlyOrders);
                }
                case "Quarterly" -> {
                    ArrayList<String> quarterlyOrders = new ArrayList<>();
                    for (String order : vendor.getOrder(usernameField.getText(), false, true)) {
                        if (isThisQuarter(order.split(", ")[5])) {
                            quarterlyOrders.add(order);
                        }
                    }
                    displayOrders(panel, quarterlyOrders);
                }
                default -> {
                    orders = vendor.getOrder(usernameField.getText(), false, true);
                    displayOrders(panel, orders);
                }
            }
        });

        // Display all orders initially
        ArrayList<String> allOrders = vendor.getOrder(usernameField.getText(), false, true);
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
            JLabel orderLabel = new JLabel("<html><pre>" + count +  ". In " + order.split(", ")[5] + " " + order.split(", ")[0] + " ordered <br>" + "   " + Arrays.toString(order.split(", ")[6].split(";")) +
                    "<br>   for $" + order.split(", ")[2] + ".</pre></html>");
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
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);
        if (currentScrollPane != null) frame.getContentPane().remove(currentScrollPane);

        JPanel panel = super.createPanel("Reviews", 190, 20, 400, 550);

        // Wrap panel inside a scroll pane with conditional scrolling
        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(190, 20, 450, 500);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(frame.getContentPane().getBackground());

        ArrayList<String> reviews = vendor.readReview(usernameField.getText());
        if (reviews == null || reviews.isEmpty()) {
            JLabel noReviewsLabel = new JLabel("No reviews available.");
            noReviewsLabel.setBounds(0, 65, 400, 45);
            noReviewsLabel.setForeground(Color.WHITE);
            panel.add(noReviewsLabel);
        } else {
            int yPosition = 69;
            int maxHeight = 20; // Track max height to adjust scrolling if needed
            int count = 1;

            for (String review : reviews) {
                String[] reviewParts = review.split(" -> ");

                JLabel reviewLabel = new JLabel("<html><pre>" + count + ". Order: " + reviewParts[0].split(", ")[6] + " from " + reviewParts[0].split(", ")[0] + ". <br>   Review: " + reviewParts[1] + "</pre></html>");
                reviewLabel.setBounds(0, yPosition, 400, 30);
                reviewLabel.setForeground(Color.WHITE);
                panel.add(reviewLabel);
                count++;

                yPosition += 40;
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

    private void revenueDashboard(MyFrame frame) {
    }


    public static void main(String[] args) {
        new VendorWindow();
    }
}
