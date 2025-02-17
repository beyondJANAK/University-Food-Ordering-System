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
        button.setFocusable(false);
        button.setBackground(new Color(60, 61, 55));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEtchedBorder());
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


//        JFrame frame = new JFrame("User Details Form");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(500, 500);
//        frame.setLayout(null);
//
//        // Creating components
//        JLabel nameLabel = new JLabel("Name: ");
//        nameLabel.setBounds(50, 30, 100, 30);
//        JTextField nameField = new JTextField();
//        nameField.setBounds(200, 30, 200, 30);
//
//        JLabel passwordLabel = new JLabel("Password: ");
//        passwordLabel.setBounds(50, 80, 100, 30);
//        JPasswordField passwordField = new JPasswordField();
//        passwordField.setBounds(200, 80, 200, 30);
//
//        JLabel emailLabel = new JLabel("Email: ");
//        emailLabel.setBounds(50, 130, 100, 30);
//        JTextField emailField = new JTextField();
//        emailField.setBounds(200, 130, 200, 30);
//
//        // Radio Buttons
//        JLabel genderLabel = new JLabel("Gender");
//        genderLabel.setBounds(50, 180, 100, 30);
//        JRadioButton maleButton = new JRadioButton("Male");
//        maleButton.setBounds(200, 180, 100, 30);
//        JRadioButton femaleButton = new JRadioButton("Female");
//        femaleButton.setBounds(300, 180, 100, 30);
//        ButtonGroup genderGroup = new ButtonGroup();
//        genderGroup.add(maleButton);
//        genderGroup.add(femaleButton);
//
//        // CheckBox
//        JLabel interestLabel = new JLabel("Interest: ");
//        interestLabel.setBounds(50, 230, 100, 30);
//        JCheckBox cricket = new JCheckBox("Cricket");
//        cricket.setBounds(200, 230, 100, 30);
//        JCheckBox football = new JCheckBox("Football");
//        football.setBounds(300, 230, 100, 30);
//        JCheckBox basketball = new JCheckBox("Basketball");
//        basketball.setBounds(200, 260, 100, 30);
//
//        // Satisfaction factor -> Slider
//        JLabel rateYourself = new JLabel("Rate Yourself: ");
//        rateYourself.setBounds(50, 300, 150, 30);
//        JSlider ratingSlider = new JSlider(0, 10, 5);
//        ratingSlider.setBounds(200, 300, 200, 50);
//        ratingSlider.setMajorTickSpacing(2);
//        ratingSlider.setMinorTickSpacing(1);
//        ratingSlider.setPaintTicks(true);
//        ratingSlider.setPaintLabels(true);
//
//        // Button
//        JButton submitButton = new JButton("Submit");
//        submitButton.setBounds(200, 370, 100, 30);
//
//        // Adding Components
//        frame.add(nameLabel);
//        frame.add(nameField);
//        frame.add(passwordLabel);
//        frame.add(passwordField);
//        frame.add(emailLabel);
//        frame.add(emailField);
//        frame.add(genderLabel);
//        frame.add(maleButton);
//        frame.add(femaleButton);
//        frame.add(interestLabel);
//        frame.add(cricket);
//        frame.add(football);
//        frame.add(basketball);
//        frame.add(rateYourself);
//        frame.add(ratingSlider);
//        frame.add(submitButton);
//
//        // Make frame visible *after* adding components
//        frame.setVisible(true);
//
//        // Ensure the UI updates correctly
//        frame.revalidate();
//        frame.repaint();
//    }
}
