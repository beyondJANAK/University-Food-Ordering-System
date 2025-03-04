package com.springProject.View;

import javax.swing.*;
import java.awt.*;

 class MyFrame extends JFrame {
    public MyFrame(String title) {
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setBounds(350, 100, 600, 600);
        this.getContentPane().setBackground(Color.BLACK);
        this.setLocationRelativeTo(null);
        this.setLayout(null);


        ImageIcon image = new ImageIcon("logo.png");
        this.setIconImage(image.getImage());
    }
}
