package com.dat.screen;

import com.dat.database.DatabaseManager;
import com.dat.domain.ClassSession;
import com.dat.domain.Student;
import com.dat.domain.StudentClassSession;

import java.util.Scanner;
import java.util.List;

public class ClassSessionScreen {
    public static void showClassSessionScreen() {
        DatabaseManager database = DatabaseManager.getInstance();
        List<ClassSession> classSessions = database.getAllClassSessions();

        System.out.println("=== CLASS SESSIONS ===");
        for (ClassSession cs : classSessions) {
            System.out.println("Session ID: " + cs.getIdClassSession() +
                    ", Shift: " + cs.getShift() +
                    ", Weekday: " + cs.getWeekday() +
                    ", Branch: " + cs.getBranch().getIdBranch() +
                    ", Active: " + cs.isActive());
        }
        System.out.println("__________________________");
        System.out.println("Available Commands:");
        System.out.println("  DETAIL [ID]               : View details of Class Session with ID");
        System.out.println("  ATTENDANCE [ID]           : View attendance for Class Session with ID");
        System.out.println("  ATTENDANCE VIEW [ID]      : View attendance with details for Class Session with ID");
        System.out.println("  EXIT                      : Exit Class Session Screen");
        System.out.println("=======================");

        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.print("\nEnter command in ClassSessionScreen: ");

            String command = sc.nextLine().trim().toUpperCase();

            if (command.equals("EXIT")) {
                System.out.println("Exiting Class Session Screen.");
                break;
            }
            controller(command);
        }
    }

    static void controller(String command) {
        String[] parts = command.trim().toUpperCase().split("\\s+");

        if (parts.length == 0) {
            System.out.println("Invalid command. Please try again.");
            return;
        }

        String action = parts[0];
        String idClassSession = parts.length == 3 ? parts[2]
                : (parts.length == 2 ? parts[1] : "");

        if (idClassSession.isEmpty()) {
            System.out.println("Please provide a Class Session ID.");
            return;
        }

        switch (action) {
            case "DETAIL":
                showClassSessionDetails(idClassSession);
                break;
            case "ATTENDANCE":
                if (parts.length == 3 && parts[1].equals("VIEW")) {
                    System.out.println("Attendance View for Session ID: " + parts[2]);
                } else if (parts.length == 2) {
                    System.out.println("Attendance Only for Session ID: " + idClassSession);
                } else {
                    System.out.println("Invalid attendance command format.");
                }
                break;
            default:
                System.out.println("Unknown command: " + command);
                break;
        }
    }

    static void showClassSessionDetails(String sessionId) {
        DatabaseManager database = DatabaseManager.getInstance();
        ClassSession classSession = database.getClassSessionById(sessionId);

        if (classSession == null) {
            System.out.println("ClassSession not found: " + sessionId);
            return;
        }

        System.out.println("\n=== CLASS SESSION DETAILS ===");
        System.out.println("Session ID: " + classSession.getIdClassSession());
        System.out.println("Shift: " + classSession.getShift());
        System.out.println("Weekday: " + classSession.getWeekday());
        System.out.println("Branch: " + classSession.getBranch().getIdBranch());
        System.out.println("Active: " + classSession.isActive());

        List<StudentClassSession> studentSessions = database.getStudentClassSessionsByClassSession(sessionId);
        System.out.println("\nStudents in this session (" + studentSessions.size() + "):");
        for (StudentClassSession scs : studentSessions) {
            Student student = scs.getStudent();
            System.out.println("- " + student.getNameStudent() +
                    " (ID: " + student.getIdStudent() +
                    ", Belt: " + student.getBeltLevel() + ")");
        }
    }
}
