package com.springProject.View;

import com.springProject.DataHandling.Admin;
import com.springProject.DataHandling.Customer;
import com.springProject.DataHandling.Vendor;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class CustomerWindow extends Components {

    MyFrame frame;
    JButton button;
    JTextField usernameField;
    JPanel currentPanel;
    JScrollPane currentScrollPane;
    Customer customer = new Customer();
    Vendor vendor = new Vendor();

    private static String creditMessage;

    public static void setCreditMessage(String creditMessage) {
        CustomerWindow.creditMessage = creditMessage;
    }

    public CustomerWindow() {
        frame = new MyFrame("Customer Login");

        super.backButton(frame);

        JLabel customerLabel = new JLabel("Enter credentials: ");
        customerLabel.setBounds(230, 150, 150, 30);
        customerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        customerLabel.setForeground(Color.WHITE);
        frame.add(customerLabel);

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
        button.addActionListener( e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            boolean success = customer.authenticate(username, password);
            if (success) {
                frame.dispose();
                customerWork();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        frame.add(button);

        frame.setVisible(true);
    }

    private void customerWork() {
        frame = new MyFrame("Customer Dashboard");

        super.backButton(frame);

        super.addButtonToFrame(frame, "Place Order", 85, e -> placeOrder(frame));
        super.addButtonToFrame(frame, "Cancel Order", 135, e -> cancelOrder(frame));
        super.addButtonToFrame(frame, "Check Order Status", 185, e -> orderStatus(frame));
        super.addButtonToFrame(frame, "Check Order History", 235, e -> orderHistory(frame));
        super.addButtonToFrame(frame, "Transaction History", 285, e -> transactionHistory(frame));

        frame.setVisible(true);

        if(creditMessage != null) {
            if(usernameField.getText().equals(creditMessage.split(", ")[0])){
                JOptionPane.showMessageDialog(frame, "<html>You have been credited with $" + creditMessage.split(", ")[1] + ". <br>Your new balance is $" + creditMessage.split(", ")[2] + ".</html>", "Receipt", JOptionPane.INFORMATION_MESSAGE); ;
                creditMessage = null;
            }
        }
    }


    private void placeOrder(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);
        if (currentScrollPane != null) frame.getContentPane().remove(currentScrollPane);

        JPanel panel = super.createPanel(null, 180, 20, 400, 550);
        panel.setBackground(frame.getContentPane().getBackground());

        JScrollPane scrollPane = super.createScrollPane(panel, frame, 215, 20, 455, 500);

        JLabel vendorLabel = new JLabel("Select Vendor:");
        vendorLabel.setBounds(0, 20, 100, 30);
        vendorLabel.setForeground(Color.WHITE);
        panel.add(vendorLabel);

        ArrayList<String> vendors = Admin.getAllVendor();
        if (vendors == null || vendors.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No vendors available.");
            return;
        }

        JComboBox<String> vendorComboBox = new JComboBox<>(new DefaultComboBoxModel<>(vendors.toArray(new String[0])));
        vendorComboBox.setBounds(119, 20, 115, 32);
        panel.add(vendorComboBox);


        JButton loadMenuButton = super.createButton("Load Menu", 253, 20, 90, 32, null);
        panel.add(loadMenuButton);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(null);
        menuPanel.setBackground(frame.getContentPane().getBackground());
        menuPanel.setOpaque(true);

        JScrollPane menuScrollPane = super.createScrollPane(menuPanel, frame, 0, 70, 400, 380);
        menuScrollPane.getViewport().setOpaque(false);
        panel.add(menuScrollPane);

        HashMap<String, Integer> cart = new HashMap<>();
        HashMap<String, Double> prices = new HashMap<>();

        loadMenuButton.addActionListener(e ->  loadMenuActionListener(menuPanel, cart, prices, vendorComboBox));

        JComboBox<String> orderTypeComboBox = new JComboBox<>(new String[]{"Dine-in", "Takeaway", "Request Delivery"});
        orderTypeComboBox.setBounds(10, 470, 100, 32);
        panel.add(orderTypeComboBox);

        JButton placeOrderButton = super.createButton("Place Order", 119, 470, 115, 32, null);
        placeOrderButton.addActionListener(e -> placeOrderActionListener(frame, menuPanel, cart, prices, vendorComboBox, orderTypeComboBox.getSelectedItem().toString()));
        panel.add(placeOrderButton);

        frame.getContentPane().add(scrollPane);
        currentScrollPane = scrollPane;
        currentPanel = panel;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void loadMenuActionListener(JPanel menuPanel, HashMap<String, Integer> cart, HashMap<String, Double> prices, JComboBox<String> vendorComboBox) {
        {
            menuPanel.removeAll();
            String selectedVendor = (String) vendorComboBox.getSelectedItem();
            if (selectedVendor != null) {
                ArrayList<String> menuItems = vendor.getMenu(selectedVendor);
                if (menuItems == null || menuItems.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No items available for this vendor.");
                } else {
                    int yPosition = 10;
                    for (String item : menuItems) {
                        String[] itemDetails = item.split(", ");
                        String itemName = itemDetails[0];
                        double itemPrice = Double.parseDouble(itemDetails[1]);
                        prices.put(itemName, itemPrice);

                        JLabel itemLabel = new JLabel(itemName + " - $" + itemPrice);
                        itemLabel.setBounds(0, yPosition, 200, 30);
                        itemLabel.setForeground(Color.WHITE);
                        menuPanel.add(itemLabel);

                        JButton decrementButton = super.createButton("-", 120, yPosition, 41, 25, null);
                        JButton incrementButton = super.createButton("+", 192, yPosition, 41, 25, null);
                        JLabel quantityLabel = new JLabel("0");
                        quantityLabel.setBounds(175, yPosition, 30, 25); // Adjusted size
                        quantityLabel.setForeground(Color.WHITE);
                        menuPanel.add(decrementButton);
                        menuPanel.add(quantityLabel);
                        menuPanel.add(incrementButton);

                        int[] quantity = {0}; // Array to track quantity

                        decrementButton.addActionListener(ev -> {
                            if (quantity[0] > 0) {
                                quantity[0]--;
                                quantityLabel.setText(String.valueOf(quantity[0]));
                            }
                        });

                        incrementButton.addActionListener(ev -> {
                            quantity[0]++;
                            quantityLabel.setText(String.valueOf(quantity[0]));
                        });

                        JButton addItemButton = super.createButton("Add to Cart", 253, yPosition, 90, 25, null);
                        addItemButton.addActionListener(ev -> {
                            if (quantity[0] > 0) {
                                cart.put(itemName, cart.getOrDefault(itemName, 0) + quantity[0]);
                                JOptionPane.showMessageDialog(frame, "Item added to cart successfully!");
                            } else {
                                cart.remove(itemName);
                            }
                        });
                        menuPanel.add(addItemButton);

                        yPosition += 60; // Adjusted y position to fit new layout
                    }
                    menuPanel.setPreferredSize(new Dimension(400, yPosition));
                    menuPanel.revalidate();
                    menuPanel.repaint();
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a vendor.");
            }
        }
    }

    private void placeOrderActionListener(MyFrame frame, JPanel menuPanel, HashMap<String, Integer> cart, HashMap<String, Double> prices, JComboBox<String> vendorComboBox, String orderType) {
        if (!cart.isEmpty()) {
            StringBuilder orderSummary = new StringBuilder("Your Order:\n");
            double totalPrice = 0;
            for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                String itemName = entry.getKey();
                int quantity = entry.getValue();
                double itemPrice = prices.get(itemName);
                totalPrice += itemPrice * quantity;
                if(orderType.equals("Request Delivery")) {
                    totalPrice += 100;
                }
                orderSummary.append(itemName).append(" - ").append(quantity).append(" x $").append(itemPrice).append("\n");
            }
            if (orderType.equals("Request Delivery")) {
                orderSummary.append("Total Price: $").append(totalPrice).append(" (including $100 delivery fee)");
            } else {
                orderSummary.append("Total Price: $").append(totalPrice);
            }

            double customerCredit = customer.getCredit(usernameField.getText());
            if (customerCredit < totalPrice) {
                JOptionPane.showMessageDialog(frame, "Insufficient credit to place the order. " + String.valueOf(totalPrice - customerCredit) + " more needed.");
                return;
            }

            int option = JOptionPane.showOptionDialog(frame, orderSummary.toString(), "Order Summary",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    new String[]{"OK", "Cancel Order"}, null);

            if (option == JOptionPane.OK_OPTION && orderType.equals("Request Delivery")) {
                customer.placeOrder(usernameField.getText(), (String) vendorComboBox.getSelectedItem(), cart, totalPrice, "pending", "delivery");
                cart.clear();
                menuPanel.removeAll();
                menuPanel.revalidate();
                menuPanel.repaint();
                JOptionPane.showMessageDialog(frame, "Order placed successfully!");
            } else if (option == JOptionPane.OK_OPTION && (orderType.equals("Dine-in") || orderType.equals("Takeaway"))) {
                customer.placeOrder(usernameField.getText(), (String) vendorComboBox.getSelectedItem(), cart, totalPrice, "pending", null);
                cart.clear();
                menuPanel.removeAll();
                menuPanel.revalidate();
                menuPanel.repaint();
                JOptionPane.showMessageDialog(frame, "Order placed successfully!");
            } else if (option == JOptionPane.CANCEL_OPTION) {
                cart.clear();
                menuPanel.removeAll();
                menuPanel.revalidate();
                menuPanel.repaint();
                JOptionPane.showMessageDialog(frame, "Order canceled.");
            } } else {
            JOptionPane.showMessageDialog(frame, "Cart is empty. Please add items to cart.");
        }
    }

    private void orderStatus(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);
        if (currentScrollPane != null) frame.getContentPane().remove(currentScrollPane);

        JPanel panel = super.createPanel(null, 180, 10, 400, 550);
        panel.setBackground(frame.getContentPane().getBackground());

        JLabel statusLabel = new JLabel("Order Status:");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setBounds(80, 15, 200, 30);
        statusLabel.setForeground(Color.WHITE);
        panel.add(statusLabel);

        ArrayList<String> orders = customer.getOrder(usernameField.getText(), false, false);
        if (orders == null || orders.isEmpty()) {
            JLabel noOrderLabel = new JLabel("No orders found!");
            noOrderLabel.setBounds(20, 70, 400, 30);
            noOrderLabel.setForeground(Color.WHITE);
            panel.add(noOrderLabel);

            frame.getContentPane().add(panel);
            currentPanel = panel;
            frame.revalidate();
            frame.repaint();
            frame.setVisible(true);
            return;
        }

        String lastOrder = null;
        for (int i = orders.size() - 1; i >= 0; i--) {
            String[] data = orders.get(i).split(", ");
            if (data.length > 5 && LocalDate.parse(data[5]).equals(LocalDate.now())) {
                lastOrder = orders.get(i);
                break;
            }
        }

        if (lastOrder == null) {
            JLabel noOrderLabel = new JLabel("No orders available for today!");
            noOrderLabel.setBounds(20, 70, 400, 30);
            noOrderLabel.setForeground(Color.WHITE);
            panel.add(noOrderLabel);

            frame.getContentPane().add(panel);
            currentPanel = panel;
            frame.revalidate();
            frame.repaint();
            frame.setVisible(true);
            return;
        }

        JLabel orderDetailsLabel = new JLabel("<html><pre>"  + "You ordered: " + lastOrder.split(", ")[6] + "<br>from " + lastOrder.split(", ")[1] + " at $" + lastOrder.split(", ")[2] + " on " + lastOrder.split(", ")[5] + "<br><br> >> Status: " +  lastOrder.split(", ")[3] +  "</pre></html>");
        orderDetailsLabel.setBounds(20, 7, 400, 200);
        orderDetailsLabel.setForeground(Color.WHITE);
        panel.add(orderDetailsLabel);

        frame.getContentPane().add(panel);
        currentPanel = panel;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void cancelOrder(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);
        if (currentScrollPane != null) frame.getContentPane().remove(currentScrollPane);

        JPanel panel = super.createPanel(null, 180, 10, 400, 550);
        panel.setBackground(frame.getContentPane().getBackground());

        JLabel statusLabel = new JLabel("Cancel Order:");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setBounds(80, 15, 200, 30);
        statusLabel.setForeground(Color.WHITE);
        panel.add(statusLabel);

        ArrayList<String> orders = customer.getOrder(usernameField.getText(), true, false);
        if (orders == null || orders.isEmpty()) {
            JLabel noOrderLabel = new JLabel("No orders available!");
            noOrderLabel.setBounds(20, 70, 400, 30);
            noOrderLabel.setForeground(Color.WHITE);
            panel.add(noOrderLabel);

            frame.getContentPane().add(panel);
            currentPanel = panel;
            frame.revalidate();
            frame.repaint();
            frame.setVisible(true);
            return;
        }

        final String lastOrder = orders.stream()
                .filter(order -> {
                    String[] data = order.split(", ");
                    return data.length > 5 && LocalDate.parse(data[5]).equals(LocalDate.now());
                })
                .reduce((first, second) -> second)
                .orElse(null);

        if (lastOrder == null) {
            JLabel noOrderLabel = new JLabel("No orders available for today");
            noOrderLabel.setBounds(20, 70, 400, 30);
            noOrderLabel.setForeground(Color.WHITE);
            panel.add(noOrderLabel);

            frame.getContentPane().add(panel);
            currentPanel = panel;
            frame.revalidate();
            frame.repaint();
            frame.setVisible(true);
            return;
        }

        JLabel orderDetailsLabel = new JLabel("<html><pre>"  + "You ordered: " + lastOrder.split(", ")[6] + "<br>from " + lastOrder.split(", ")[1] + " at $" + lastOrder.split(", ")[2] + " on " + lastOrder.split(", ")[5] + "<br><br> >> Status: " +  lastOrder.split(", ")[3] +  "</pre></html>");
        orderDetailsLabel.setBounds(20, 7, 400, 200);
        orderDetailsLabel.setForeground(Color.WHITE);
        panel.add(orderDetailsLabel);

        JButton cancelButton = super.createButton("Cancel Order", 25, 170, 90, 32, null);
        cancelButton.addActionListener(e -> {
            customer.cancelOrder(usernameField.getText(), lastOrder);
            JOptionPane.showMessageDialog(frame, "Order canceled successfully.");
            cancelOrder(frame);
        });
        panel.add(cancelButton);

        frame.getContentPane().add(panel);
        currentPanel = panel;

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
                    orders = customer.getOrder(usernameField.getText(), false, true);
                    displayOrders(panel, orders);
                }

                case "Daily" -> {
                    ArrayList<String> dailyOrders = new ArrayList<>();
                    for (String order : customer.getOrder(usernameField.getText(), false, true)) {
                        if (isToday(order.split(", ")[5])) {
                            dailyOrders.add(order);
                        }
                    }
                    displayOrders(panel, dailyOrders);
                }
                case "Monthly" -> {
                    ArrayList<String> monthlyOrders = new ArrayList<>();
                    for (String order : customer.getOrder(usernameField.getText(), false, true)) {
                        if (isThisMonth(order.split(", ")[5])) {
                            monthlyOrders.add(order);
                        }
                    }
                    displayOrders(panel, monthlyOrders);
                }
                case "Quarterly" -> {
                    ArrayList<String> quarterlyOrders = new ArrayList<>();
                    for (String order : customer.getOrder(usernameField.getText(), false, true)) {
                        if (isThisQuarter(order.split(", ")[5])) {
                            quarterlyOrders.add(order);
                        }
                    }
                    displayOrders(panel, quarterlyOrders);
                }
                default -> {
                    orders = customer.getOrder(usernameField.getText(), false, true);
                    displayOrders(panel, orders);
                }
            }
        });

        // Display all orders initially
        ArrayList<String> allOrders = customer.getOrder(usernameField.getText(), false, true);
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
            if (c instanceof JLabel || c instanceof JButton) {
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

            JButton reorderButton = super.createButton("Reorder", 302, yPosition, 80, 20, null);
            reorderButton.addActionListener(e -> {
                String[] orderData = order.split(", ");
                if (orderData.length < 7) {
                    JOptionPane.showMessageDialog(frame, "Invalid order data format", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String vendorName = orderData[1];
                String items = orderData[6];
                HashMap<String, Integer> cart = new HashMap<>();
                try {
                    for (String item : items.split(";")) {
                        if (!item.isEmpty()) {
                            String[] itemParts = item.split(":");
                            if (itemParts.length == 2) {
                                cart.put(itemParts[0], Integer.parseInt(itemParts[1]));
                            }
                        }
                    }
                    double totalPrice = Double.parseDouble(orderData[2]);
                    customer.placeOrder(usernameField.getText(), vendorName, cart, totalPrice, "pending", null);
                    JOptionPane.showMessageDialog(frame, "Order has been reordered successfully!");

                    // Refresh the orders display after reordering
                    ArrayList<String> updatedOrders = customer.getOrder(usernameField.getText(), false, true);
                    displayOrders(panel, updatedOrders);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    JOptionPane.showMessageDialog(frame, "Error processing order data", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            panel.add(reorderButton);

            JButton feedbackButton = super.createButton("Feedback", 302, yPosition + 22, 80, 20, null);
            feedbackButton.addActionListener(e -> {
                String feedback = JOptionPane.showInputDialog(frame, "Please enter your feedback for this order:", "Order Feedback", JOptionPane.PLAIN_MESSAGE);
                if (feedback != null && !feedback.trim().isEmpty()) {
                    customer.addFeedback(order, feedback);
                    JOptionPane.showMessageDialog(frame, "Thank you for your feedback!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            panel.add(feedbackButton);

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

    private void transactionHistory(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);
        if (currentScrollPane != null) frame.getContentPane().remove(currentScrollPane);

        JPanel panel = super.createPanel("Transaction History", 130, 40, 400, 550);
        panel.setLayout(null);

        // Wrap panel inside a scroll pane with conditional scrolling
        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(190, 20, 450, 500);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(frame.getContentPane().getBackground());

        ArrayList<String> transactions = customer.getTransactionHistory(usernameField.getText());
        if (transactions == null || transactions.isEmpty()) {
            JLabel noTransactionLabel = new JLabel("No transactions found!");
            noTransactionLabel.setBounds(80, 40, 400, 30);
            noTransactionLabel.setForeground(Color.WHITE);
            panel.add(noTransactionLabel);

            frame.getContentPane().add(panel);
            currentPanel = panel;
            frame.revalidate();
            frame.repaint();
            frame.setVisible(true);
            return;
        }

        int yPosition = 69; // Start below the combobox
        int maxHeight = yPosition;

        int count = 1;
        for (String transaction : transactions) {
            JLabel transactionLabel = new JLabel("<html><pre>" + count +  ". On " + transaction.split(", ")[5] + ", you ordered <br>   " + transaction.split(", ")[6] + " of $" + transaction.split(", ")[2] + " from " + transaction.split(", ")[1] + ".</pre></html>");
            transactionLabel.setBounds(0, yPosition, 330, 30);
            transactionLabel.setForeground(Color.WHITE);
            panel.add(transactionLabel);
            count++;

            yPosition += 40;
            maxHeight = yPosition;
        }

        panel.setPreferredSize(new Dimension(400, Math.max(400, maxHeight)));
        panel.revalidate();
        panel.repaint();

        frame.getContentPane().add(scrollPane, JLayeredPane.POPUP_LAYER);
        currentPanel = panel;
        currentScrollPane = scrollPane;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

}
