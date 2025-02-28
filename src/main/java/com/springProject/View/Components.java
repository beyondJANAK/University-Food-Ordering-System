package com.springProject.View;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Components {

    void backButton(MyFrame frame) {
        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 10, 45, 25);
        backButton.setFocusable(false);
        backButton.setBackground(new Color(60, 61, 55));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBorder(BorderFactory.createEtchedBorder());
        backButton.addActionListener(e -> {
            frame.dispose();
            new MainWindow();
        });
        frame.add(backButton);
    }

    void addButtonToFrame(MyFrame frame, String text, int yPosition, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setBounds(10, yPosition, 160, 40);
        button.setBackground(new Color(60, 61, 55));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEtchedBorder());
        button.setFocusable(false);
        button.addActionListener(actionListener);
        frame.add(button);
    }

    JPanel createPanel(String labelText, int x, int y, int width, int height) {
        JPanel panel = new JPanel(null);
        panel.setBounds(x, y, width, height);
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setBounds(100, 10, 200, 30);
        panel.add(label);

        return panel;
    }

    public JScrollPane createScrollPane(JPanel panel, JFrame frame, int x, int y, int width, int height) {
        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(x, y, width, height);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(frame.getContentPane().getBackground());
        return scrollPane;
    }

    JTextField createTextField(JPanel panel, String labelText, int labelX, int labelY, int fieldX, int fieldY) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setBounds(labelX, labelY, 100, 30);
        panel.add(label);

        JTextField textField = new JTextField();
        textField.setBounds(fieldX, fieldY, 200, 30);
        panel.add(textField);

        return textField;
    }

    JPasswordField createPasswordField(JPanel panel, String labelText, int labelX, int labelY, int fieldX, int fieldY) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setBounds(labelX, labelY, 100, 30);
        panel.add(label);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(fieldX, fieldY, 200, 30);
        panel.add(passwordField);

        return passwordField;
    }

    JButton createButton(String text, int x, int y, int width, int height, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEtchedBorder());
        button.addActionListener(actionListener);
        return button;
    }

}
