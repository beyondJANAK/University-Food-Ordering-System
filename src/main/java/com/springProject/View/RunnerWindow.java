package com.springProject.View;

import com.springProject.DataHandling.Runner;

import javax.swing.*;
import java.awt.*;

public class RunnerWindow extends Components {

    MyFrame frame;
    JButton button;
    Runner runner = new Runner();

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


        frame.setVisible(true);
    }
}


