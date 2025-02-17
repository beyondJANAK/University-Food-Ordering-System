package com.springProject.View;

import com.springProject.DataHandling.Vendor;

import javax.swing.*;
import java.awt.*;
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
        super.addButtonToFrame(frame, "Revenue DashBoard", 285, e -> revenueDashboard(frame));

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

        ArrayList<String> orders = vendor.getOrder(usernameField.getText());
        int yPosition = 65;
        int maxHeight = 20; // Track max height to adjust scrolling if needed
        int count = 0;

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





    private void updateOrderStatus(MyFrame frame){};

    private void orderHistory(MyFrame frame) {
    }
    private void readReview(MyFrame frame) {
    }
    private void revenueDashboard(MyFrame frame) {
    }


    public static void main(String[] args) {
        new VendorWindow();
    }
}
