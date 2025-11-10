package com.dat.screen;

import com.dat.database.DatabaseManager;
import com.dat.domain.StudentAttendance;
import com.dat.enums.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class StudentAttendenceScreen {
    static class ClassSessionAndDate {
        String idClassSession;
        LocalDate attendanceDate;

        public ClassSessionAndDate(String idClassSession, LocalDate attendanceDate) {
            this.idClassSession = idClassSession;
            this.attendanceDate = attendanceDate;
        }

        public LocalDate getAttendanceDate() {
            return attendanceDate;
        }

        public void setAttendanceDate(LocalDate attendanceDate) {
            this.attendanceDate = attendanceDate;
        }

        public String getIdClassSession() {
            return idClassSession;
        }

        public void setIdClassSession(String idClassSession) {
            this.idClassSession = idClassSession;
        }
    }

    public static void showStudentAttendenceScreen() {
        Scanner sc = new Scanner(System.in);
        String idClassSession = "";

        System.out.println("=== STUDENT ATTENDANCE SCREEN ===");
        System.out.print("Enter Class Session ID to manage attendance or type 'exit' to return to the main menu: ");
        String initialId = sc.nextLine().trim(); // Đổi tên biến tạm để tránh nhầm lẫn

        if (initialId.equalsIgnoreCase("exit")) {
            System.out.println("Exiting Student Attendance Screen.");
            return;
        }

        // Khởi tạo đối tượng sau khi chắc chắn không phải là "exit"
        ClassSessionAndDate sessionAndDate = new ClassSessionAndDate(initialId, LocalDate.now());

        while (true) {
            System.out.printf("\nEnter command [Session: %s | Date: %s]: ",
                    sessionAndDate.getIdClassSession(),
                    sessionAndDate.getAttendanceDate().toString());

            String command = sc.nextLine().trim();

            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting Student Attendance Screen.");
                break;
            }

            sessionAndDate = controller(command, sessionAndDate);
        }
    }

    static ClassSessionAndDate controller(String command, ClassSessionAndDate sessionAndDate) {
        DatabaseManager database = DatabaseManager.getInstance();
        String idClassSession = sessionAndDate.idClassSession;
        LocalDate attendanceDate = sessionAndDate.attendanceDate;

        String[] parts = command.trim().toUpperCase().split("\\s+");

        if (parts.length == 0) {
            System.out.println("Invalid command. Please try again.");
            return sessionAndDate;
        }

        String action = parts[0];

        switch (action) {
            case "CLASS-SESSION":
                if (parts.length > 1) {
                    System.out.println("Switched to session " + parts[1]);
                    return new ClassSessionAndDate(
                            parts[1],
                            attendanceDate
                    ); // <--- TRẢ VỀ ID MỚI
                }
                break;
            case "DETAIL":
                List<StudentAttendance> studentAttendance = database
                        .getStudentAttendancesByClassSessionAndDate(idClassSession, attendanceDate);
                showStudentAttendanceDetail(studentAttendance, sessionAndDate);
                break;
            case "ATT": // Example command: ATT SV001 PRESENT
                if (parts.length == 3) {
                    String studentId = parts[1];
                    String statusStr = parts[2].toUpperCase(); // Normalize to uppercase

                    try {
                        // Attempt to convert the input string to an Attendance Enum value
                        Attendance status = Attendance.valueOf(statusStr);

                        // LOGIC SUCCESSFUL HERE
                        // System.out.println("Marking " + status + " for student " + studentId);
                        // database.markAttendance(currentSessionId, studentId, status);
                        System.out.println("Attendance marked successfully.");

                    } catch (IllegalArgumentException e) {
                        // Caught invalid enum name (e.g., ATT SV001 LATE)
                        System.out.println("Invalid attendance status: " + parts[2]);
                        System.out.println("Valid statuses are: PRESENT, ABSENT, EXCUSED.");
                    }
                } else {
                    System.out.println("Invalid command format. Usage: ATT <Student ID> <Status>");
                }
                break;
            case "EVA": // Evaluation
                // Xử lý lệnh Evaluation ở đây nếu cần
                break;
            default:
                System.out.println("Unknown command: " + action);
        }
        return sessionAndDate;
    }

    static void showStudentAttendanceDetail(
            List<StudentAttendance> attendanceList,
            ClassSessionAndDate classSessionAndDate) {
        System.out.println("\n==========================================");
        System.out.println("      ATTENDANCE REPORT"); // Thay thế chi tiết bằng "Báo cáo"
        System.out.println("==========================================");

        // THÔNG TIN CHUYÊN NGHIỆP VỀ BẢN GHI
        System.out.println("Session ID: " + classSessionAndDate.getIdClassSession());
        System.out.println("Date: " + classSessionAndDate.getAttendanceDate());
        System.out.println("------------------------------------------");

        System.out.println("Total Records: " + attendanceList.size());
        System.out.println("------------------------------------------");

        if (attendanceList.isEmpty()) {
            System.out.println("No attendance records found.");
            return;
        } else {
            System.out.printf("%-12s %-10s %-12s %-12s%n",
                    "Date", "Student Name", "Attendance", "Evaluation");
            System.out.println("------------------------------------------");

            for (StudentAttendance record : attendanceList) {
                System.out.printf("%-12s %-20s %-12s %-12s%n",
                        record.getCreatedDate().toString(),
                        record.getStudent().getNameStudent(),
                        // Sử dụng trực tiếp toString() hoặc name() của Enum Attendance
                        record.getAttendance(),
                        // Kiểm tra null cho Evaluation trước khi in để tránh NullPointerException
                        (record.getEvaluation() != null) ? record.getEvaluation() : "___"
                );
            }
        }
        System.out.println("===================================");
    }
}