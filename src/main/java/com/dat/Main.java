package com.dat;

import com.dat.database.DatabaseManager;
import com.dat.screen.MainScreen;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("System is starting...");

        // Khởi tạo DatabaseManager với hard data
        DatabaseManager database = DatabaseManager.getInstance();

        System.out.println("Database initialized successfully!");

        // Start the main screen
        MainScreen.showMainScreen();
    }
}