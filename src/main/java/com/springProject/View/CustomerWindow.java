package com.springProject.View;

import com.springProject.DataHandling.Admin;
import com.springProject.DataHandling.Customer;
import com.springProject.DataHandling.Vendor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
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
        button.setBounds(235, 325, 130, 50);
        button.setFocusable(false);
        button.setBackground(new Color(60, 61, 55));
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

        JButton placeOrderButton = super.createButton("Place Order", 119, 470, 115, 32, null);
        placeOrderButton.addActionListener(e -> placeOrderActionListener(frame, menuPanel, cart, prices, vendorComboBox));
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

    private void placeOrderActionListener(MyFrame frame, JPanel menuPanel, HashMap<String, Integer> cart, HashMap<String, Double> prices, JComboBox<String> vendorComboBox) {
        if (!cart.isEmpty()) {
            StringBuilder orderSummary = new StringBuilder("Your Order:\n");
            double totalPrice = 0;
            for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                String itemName = entry.getKey();
                int quantity = entry.getValue();
                double itemPrice = prices.get(itemName);
                totalPrice += itemPrice * quantity;
                orderSummary.append(itemName).append(" - ").append(quantity).append(" x $").append(itemPrice).append("\n");
            }
            orderSummary.append("Total Price: $").append(totalPrice);

            double customerCredit = customer.getCredit(usernameField.getText());
            if (customerCredit < totalPrice) {
                JOptionPane.showMessageDialog(frame, "Insufficient credit to place the order. " + String.valueOf(totalPrice - customerCredit) + " more needed.");
                return;
            }

            int option = JOptionPane.showOptionDialog(frame, orderSummary.toString(), "Order Summary",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    new String[]{"OK", "Cancel Order"}, null);

            if (option == JOptionPane.OK_OPTION) {
                customer.placeOrder(usernameField.getText(), (String) vendorComboBox.getSelectedItem(), cart, totalPrice, "pending");
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
            }
        } else {
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

        ArrayList<String> orders = customer.getOrder(usernameField.getText());
        if (orders == null || orders.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No orders found.");
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
            JOptionPane.showMessageDialog(frame, "No orders found for today.");
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
    }

    private void orderHistory(MyFrame frame) {
    }

    private void transactionHistory(MyFrame frame) {
    }


    public static void main(String[] args) {
        new CustomerWindow();
    }
}
