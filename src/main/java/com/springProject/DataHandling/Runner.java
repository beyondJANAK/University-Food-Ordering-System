package com.springProject.DataHandling;

import java.io.*;
import java.util.ArrayList;

public class Runner {

    public boolean authenticate(String username, String password){
        try (FileReader fileReader = new FileReader("src/Database/Runner.txt")) {
            int character;
            StringBuilder line = new StringBuilder();
            while ((character = fileReader.read()) != -1) {
                if (character == '\n') {
                    String[] data = line.toString().split(", ");
                    if (data[0].equals(username) && data[1].equals(password)) {
                        return true;
                    }
                    line.setLength(0);
                } else {
                    line.append((char) character);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
