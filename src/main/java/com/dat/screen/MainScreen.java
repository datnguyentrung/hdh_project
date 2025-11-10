package com.dat.screen;

import com.dat.database.DatabaseManager;
import java.util.Scanner;

public class MainScreen {
    public static void showMainScreen() {
        Scanner sc = new Scanner(System.in);
        DatabaseManager database = DatabaseManager.getInstance();

        showMainDetails();

        while (true) {
            System.out.print("\nEnter command: ");

            String command = sc.nextLine().trim();

            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }
            controller(command);
        }
        sc.close();
    }

    private static void showMainDetails(){
        System.out.println("\n==========================================");
        System.out.println("      STUDENT MANAGEMENT SYSTEM");
        System.out.println("==========================================");
        System.out.println("Available Commands:");
        System.out.println("  [List]    : View Student List");
        System.out.println("  [History] : View Attendance Logs");
        System.out.println("  [Attendance] : Manage Student Attendance");
        System.out.println("  [Class-Session] : View specific session list");
        System.out.println("  [Exit]    : Exit the program");
        System.out.println("------------------------------------------");
        System.out.println("Note: Type 'Menu' to return to this screen.");
    }

    public static void controller(String command){
        switch (command.toLowerCase()) {
            case "exit":
                System.out.println("Goodbye!");
                break;
            case "list":
                StudentListScreen.showStudentList();
                break;
            case "history":
                System.out.println("History feature will be implemented soon!");
                break;
            case "attendance":
                StudentAttendenceScreen.showStudentAttendenceScreen();
                break;
            case "class-session":
                ClassSessionScreen.showClassSessionScreen();
                break;
            case "branch":
                System.out.println("Branch feature will be implemented soon!");
                break;
            case "menu":
                showMainDetails();
                break;
            default:
                System.out.println("Unknown command: " + command);
                break;
        }
    }
}