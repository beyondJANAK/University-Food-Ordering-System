package com.springProject.View;

import javax.swing.*;
import java.awt.*;

public class MainWindow {
    JButton button;

    public MainWindow() {
        MyFrame frame = new MyFrame("Food Ordering System");

        JLabel loginLabel = new JLabel("Login as:");
        loginLabel.setBounds(250, 200, 150, 30);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 16));
        loginLabel.setForeground(Color.WHITE);
        frame.add(loginLabel);

        String[] roles = {"Admin", "Vendor", "Runner", "Customer"};
        JComboBox<String> roleDropdown = new JComboBox<>(roles);
        roleDropdown.setBounds(200, 250, 200, 40);
        roleDropdown.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.add(roleDropdown);


        button = new JButton("Submit");
        button.setBounds(225, 310, 130, 50);
        button.addActionListener(e -> buttonAction(roleDropdown, frame));
        button.setFocusable(false);
        button.setBackground(new Color(17, 72, 125));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEtchedBorder());

        frame.add(button);

        frame.setVisible(true);
    }

    private static void buttonAction(JComboBox<String> roleDropdown, MyFrame frame) {
        if (roleDropdown.getSelectedItem().equals("Admin")) {
            frame.dispose();
            new AdminWindow();
        } else if (roleDropdown.getSelectedItem().equals("Vendor")) {
            frame.dispose();
            new VendorWindow();
        } else if (roleDropdown.getSelectedItem().equals("Customer")) {
            frame.dispose();
            new CustomerWindow();
        } else if (roleDropdown.getSelectedItem().equals("Runner")) {
            frame.dispose();
            new RunnerWindow();
        }
    }

}
