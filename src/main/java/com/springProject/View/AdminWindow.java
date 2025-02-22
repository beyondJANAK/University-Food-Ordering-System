package com.springProject.View;

import com.springProject.DataHandling.Admin;
import com.springProject.DataHandling.Customer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AdminWindow extends Components {
    MyFrame frame;
    JButton button;
    Admin admin = new Admin();
    Customer customer = new Customer();

    private JPanel currentPanel;

    public AdminWindow() {
        frame = new MyFrame("Admin Login");

        super.backButton(frame);

        JLabel adminLabel = new JLabel("Enter credentials: ");
        adminLabel.setBounds(230, 150, 150, 30);
        adminLabel.setFont(new Font("Arial", Font.BOLD, 16));
        adminLabel.setForeground(Color.WHITE);
        frame.add(adminLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(110, 230, 100, 30);
        frame.add(usernameLabel);

        JTextField usernameField = new JTextField();
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
            boolean success = admin.authenticate(username, password);
            if (success) {
                frame.dispose();
                adminWork();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        frame.add(button);

        frame.setVisible(true);
    }

    private void adminWork() {
        frame = new MyFrame("Admin Dashboard");

        super.backButton(frame);

        super.addButtonToFrame(frame, "Register Customer", 85, e -> registerCustomer(frame));
        super.addButtonToFrame(frame, "Delete Customer", 135, e -> deleteCustomer(frame));
        super.addButtonToFrame(frame, "Register Vendor", 185, e -> registerVendor(frame));
        super.addButtonToFrame(frame, "Delete Vendor", 235, e -> deleteVendor(frame));
        super.addButtonToFrame(frame, "Register Runner", 285, e -> registerRunner(frame));
        super.addButtonToFrame(frame, "Delete Runner", 335, e -> deleteRunner(frame));
        super.addButtonToFrame(frame, "Topup", 385, e -> topupCustomerCredit(frame));

        frame.setVisible(true);
    }


    private void registerCustomer(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);

        JPanel panel = super.createPanel("Enter credentials:", 200, 100, 400, 300);

        JTextField newUsernameField = super.createTextField(panel, "Username:", 50, 60, 150, 60);
        JPasswordField newPasswordField = super.createPasswordField(panel, "Password:", 50, 110, 150, 110);
        JTextField newEmailField = super.createTextField(panel, "Email:", 50, 160, 150, 160);

        JButton submitButton = super.createButton("Submit", 150, 210, 100, 40, e -> {
            String username = newUsernameField.getText();
            String password = new String(newPasswordField.getPassword());
            String email = newEmailField.getText();
            admin.saveCustomer(username, password, email);
            JOptionPane.showMessageDialog(frame, "Customer added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            newUsernameField.setText("");
            newPasswordField.setText("");
            newEmailField.setText("");
        });
        panel.add(submitButton);

        frame.getContentPane().add(panel, "registerCustomer");
        currentPanel = panel;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void deleteCustomer(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);

        JPanel panel = super.createPanel("Delete Customer:", 200, 100, 400, 300);

        ArrayList<String> customers = admin.getAllCustomer();
        int yPosition = 60;

        for (String username : customers) {
            JLabel usernameLabel = new JLabel(username);
            usernameLabel.setForeground(Color.WHITE);
            usernameLabel.setBounds(50, yPosition, 100, 30);
            panel.add(usernameLabel);

            JButton deleteButton = new JButton("Delete");
            deleteButton.setBounds(200, yPosition, 100, 30);
            deleteButton.setFocusable(false);
            deleteButton.addActionListener(e -> {
                boolean success = admin.deleteCustomer(username);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Vendor deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    panel.remove(usernameLabel);
                    panel.remove(deleteButton);
                    frame.revalidate();
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to delete Vendor", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            panel.add(deleteButton);

            yPosition += 40;
        }

        frame.getContentPane().add(panel, JLayeredPane.POPUP_LAYER);
        currentPanel = panel;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void registerVendor(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);

        JPanel panel = super.createPanel("Enter credentials:", 200, 100, 400, 300);

        JTextField newUsernameField = super.createTextField(panel, "Username:", 50, 60, 150, 60);
        JPasswordField newPasswordField = super.createPasswordField(panel, "Password:", 50, 110, 150, 110);
        JTextField newEmailField = super.createTextField(panel, "Email:", 50, 160, 150, 160);

        JButton submitButton = super.createButton("Submit", 150, 210, 100, 40, e -> {
            String username = newUsernameField.getText();
            String password = new String(newPasswordField.getPassword());
            String email = newEmailField.getText();
            admin.saveVendor(username, password, email);
            JOptionPane.showMessageDialog(frame, "Vendor added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            newUsernameField.setText("");
            newPasswordField.setText("");
            newEmailField.setText("");
        });
        panel.add(submitButton);

        frame.getContentPane().add(panel, JLayeredPane.POPUP_LAYER);
        currentPanel = panel;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void deleteVendor(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);

        JPanel panel = super.createPanel("Delete Vendor:", 200, 100, 400, 300);

        ArrayList<String> customers = admin.getAllVendor();
        int yPosition = 60;

        for (String username : customers) {
            JLabel usernameLabel = new JLabel(username);
            usernameLabel.setForeground(Color.WHITE);
            usernameLabel.setBounds(50, yPosition, 100, 30);
            panel.add(usernameLabel);

            JButton deleteButton = new JButton("Delete");
            deleteButton.setBounds(200, yPosition, 100, 30);
            deleteButton.setFocusable(false);
            deleteButton.addActionListener(e -> {
                boolean success = admin.deleteVendor(username);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Customer deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    panel.remove(usernameLabel);
                    panel.remove(deleteButton);
                    frame.revalidate();
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to delete customer", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            panel.add(deleteButton);

            yPosition += 40;
        }

        frame.getContentPane().add(panel, JLayeredPane.POPUP_LAYER);
        currentPanel = panel;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void registerRunner(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);

        JPanel panel = super.createPanel("Enter credentials:", 200, 100, 400, 300);

        JTextField newUsernameField = super.createTextField(panel, "Username:", 50, 60, 150, 60);
        JPasswordField newPasswordField = super.createPasswordField(panel, "Password:", 50, 110, 150, 110);
        JTextField newEmailField = super.createTextField(panel, "Email:", 50, 160, 150, 160);

        JButton submitButton = super.createButton("Submit", 150, 210, 100, 40, e -> {
            String username = newUsernameField.getText();
            String password = new String(newPasswordField.getPassword());
            String email = newEmailField.getText();
            admin.saveRunner(username, password, email);
            JOptionPane.showMessageDialog(frame, "Runner added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            newUsernameField.setText("");
            newPasswordField.setText("");
            newEmailField.setText("");
        });
        panel.add(submitButton);

        frame.getContentPane().add(panel, JLayeredPane.POPUP_LAYER);
        currentPanel = panel;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void deleteRunner(MyFrame frame) {
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);

        JPanel panel = super.createPanel("Delete Runner:", 200, 100, 400, 300);

        ArrayList<String> customers = Admin.getAllRunner();
        int yPosition = 60;

        for (String username : customers) {
            JLabel usernameLabel = new JLabel(username);
            usernameLabel.setForeground(Color.WHITE);
            usernameLabel.setBounds(50, yPosition, 100, 30);
            panel.add(usernameLabel);

            JButton deleteButton = new JButton("Delete");
            deleteButton.setBounds(200, yPosition, 100, 30);
            deleteButton.setFocusable(false);
            deleteButton.addActionListener(e -> {
                boolean success = admin.deleteRunner(username);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Runner deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    panel.remove(usernameLabel);
                    panel.remove(deleteButton);
                    frame.revalidate();
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to delete customer", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            panel.add(deleteButton);

            yPosition += 40;
        }

        frame.getContentPane().add(panel, JLayeredPane.POPUP_LAYER);
        currentPanel = panel;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void topupCustomerCredit (MyFrame frame){
        if (currentPanel != null) frame.getContentPane().remove(currentPanel);

        JPanel panel = super.createPanel("Topup Customer Credit:", 200, 100, 400, 300);

        JLabel adminLabel = new JLabel("Topup Customer Credit: ");
        adminLabel.setFont(new Font("Arial", Font.BOLD, 16));
        adminLabel.setForeground(Color.WHITE);
        adminLabel.setBounds(100, 10, 200, 30);
        panel.add(adminLabel);

        ArrayList<String> customers = admin.getAllCustomer();
        int yPosition = 60;

        for (String username : customers) {
            JLabel usernameLabel = new JLabel(username);
            usernameLabel.setForeground(Color.WHITE);
            usernameLabel.setBounds(30, yPosition, 100, 30);
            panel.add(usernameLabel);

            JTextField creditField = new JTextField();
            creditField.setBounds(140, yPosition, 100, 30);
            panel.add(creditField);

            JButton topupButton = new JButton("Topup");
            topupButton.setBounds(260, yPosition, 100, 30);
            topupButton.setFocusable(false);
            topupButton.addActionListener(e -> {
                String credit = creditField.getText();
                boolean success = admin.topupCredit(username, credit);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "<html>Credit topped up successfully.<br>Receipt sent to: " + username + ". </html>", "Receipt Generated", JOptionPane.INFORMATION_MESSAGE);
                    CustomerWindow.setCreditMessage(username + ", " + credit + ", " + customer.getCredit(username));
                    creditField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to topup credit", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            panel.add(topupButton);

            yPosition += 40;
        }

        frame.getContentPane().add(panel, JLayeredPane.POPUP_LAYER);
        currentPanel = panel;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }
}
