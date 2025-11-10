package com.dat.screen;

import com.dat.database.DatabaseManager;
import com.dat.domain.Student;
import com.dat.domain.StudentClassSession;
import java.util.List;

public class StudentListScreen {

    public static void showStudentList() {
        DatabaseManager database = DatabaseManager.getInstance();
        var students = database.getAllStudents();

        System.out.println("\n==========================================");
        System.out.println("            STUDENT LIST SCREEN");
        System.out.println("==========================================");
        System.out.println("Total Active Students: " + students.size());
        System.out.println("------------------------------------------");

        if (students.isEmpty()) {
            System.out.println("No active students found.");
        } else {
            System.out.printf("%-8s %-20s %-12s %-10s %-8s%n",
                            "ID", "Name", "Phone", "Belt", "Classes");
            System.out.println("------------------------------------------");

            for (Student student : students) {
                List<StudentClassSession> studentSessions = database.getStudentClassSessionsByStudent(student.getIdStudent());
                int classCount = studentSessions.size();

                System.out.printf("%-8s %-20s %-12s %-10s %-8d%n",
                    student.getIdStudent(),
                    student.getNameStudent(),
                    student.getPhoneNumber(),
                    student.getBeltLevel(),
                    classCount);
            }
        }

        System.out.println("------------------------------------------");
        System.out.println("Commands:");
        System.out.println("  [Detail <StudentID>] : View student details");
        System.out.println("  [Menu] : Return to main screen");
    }

    public static void showStudentDetail(String studentId) {
        DatabaseManager database = DatabaseManager.getInstance();
        Student student = database.getStudentById(studentId);

        if (student == null) {
            System.out.println("Student not found: " + studentId);
            return;
        }

        System.out.println("\n=== STUDENT DETAILS ===");
        System.out.println("ID: " + student.getIdStudent());
        System.out.println("Name: " + student.getNameStudent());
        System.out.println("Phone: " + student.getPhoneNumber());
        System.out.println("Belt Level: " + student.getBeltLevel());
        System.out.println("Active: " + student.isActive());

        List<StudentClassSession> studentSessions = database.getStudentClassSessionsByStudent(studentId);
        System.out.println("\nRegistered Classes (" + studentSessions.size() + "):");

        if (studentSessions.isEmpty()) {
            System.out.println("No classes registered.");
        } else {
            for (StudentClassSession scs : studentSessions) {
                var classSession = scs.getClassSession();
                System.out.println("- " + classSession.getIdClassSession() +
                                 " (Shift " + classSession.getShift() +
                                 ", Weekday " + classSession.getWeekday() +
                                 ", Branch " + classSession.getBranch().getIdBranch() + ")");
            }
        }
    }
}
