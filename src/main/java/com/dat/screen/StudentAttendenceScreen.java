package com.dat.screen;

import com.dat.database.DatabaseManager;
import com.dat.domain.StudentAttendance;
import com.dat.enums.Attendance;
import com.dat.enums.Evaluation;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class StudentAttendenceScreen {

    // Sử dụng final cho các trường nếu chúng không thay đổi sau khi khởi tạo đối tượng này
    static class ClassSessionAndDate {
        final String idClassSession;
        final LocalDate attendanceDate;

        public ClassSessionAndDate(String idClassSession, LocalDate attendanceDate) {
            this.idClassSession = idClassSession;
            this.attendanceDate = attendanceDate;
        }

        public String getIdClassSession() { return idClassSession; }
        public LocalDate getAttendanceDate() { return attendanceDate; }
    }

    public static void showStudentAttendenceScreen() {
        Scanner sc = new Scanner(System.in);
        DatabaseManager database = DatabaseManager.getInstance();

        System.out.println("=== STUDENT ATTENDANCE SCREEN ===");
        System.out.println("Available Commands:");
        System.out.println("  [Class-Session <Session ID>]   : Switch to a different class session");
        System.out.println("  [Detail]                       : View attendance details");
        System.out.println("  [Att <Student ID> <Status>]    : Mark attendance (PRESENT, ABSENT, EXCUSED)");
        System.out.println("  [Eva <Student ID> <Evaluation>]: Update evaluation (GOOD, AVERAGE, POOR)");
        System.out.println("  [Exit]                         : Exit screen");
        System.out.println("------------------------------------------");

        String initialId;
        while (true) {
            System.out.print("Enter Initial Class Session ID (or 'exit'): ");
            initialId = sc.nextLine().trim().toUpperCase();
            if (initialId.equalsIgnoreCase("EXIT")) return;
            if (database.existsClassSession(initialId)) break;
            System.out.println("Class Session ID " + initialId + " does not exist. Try again.");
        }

        ClassSessionAndDate sessionAndDate = new ClassSessionAndDate(initialId, LocalDate.now());

        while (true) {
            System.out.printf("\n[Session: %s | Date: %s] > Enter command: ",
                    sessionAndDate.getIdClassSession(),
                    sessionAndDate.getAttendanceDate());

            String command = sc.nextLine().trim();
            if (command.equalsIgnoreCase("EXIT")) {
                System.out.println("Exiting Student Attendance Screen.");
                break;
            }
            sessionAndDate = controller(command, sessionAndDate);
        }
    }

    static ClassSessionAndDate controller(String command, ClassSessionAndDate sessionAndDate) {
        DatabaseManager database = DatabaseManager.getInstance();
        String[] parts = command.trim().toUpperCase().split("\\s+");

        if (parts.length == 0 || parts[0].isEmpty()) return sessionAndDate;

        switch (parts[0]) {
            case "CLASS-SESSION":
                if (parts.length > 1) {
                    String newSessionId = parts[1];
                    if (database.existsClassSession(newSessionId)) {
                        System.out.println("Switched to session " + newSessionId);
                        return new ClassSessionAndDate(newSessionId, sessionAndDate.attendanceDate);
                    } else {
                        System.out.println("Class Session ID " + newSessionId + " does not exist.");
                    }
                } else {
                    System.out.println("Usage: CLASS-SESSION <Session ID>");
                }
                break;

            case "DETAIL":
                List<StudentAttendance> list = database.getStudentAttendancesByClassSessionAndDate(
                        sessionAndDate.idClassSession, sessionAndDate.attendanceDate);
                showStudentAttendanceDetail(list, sessionAndDate);
                break;

            case "ATT":
                handleUpdateCommand(parts, sessionAndDate, Attendance.class,
                        StudentAttendance::setAttendance, // Truyền method reference setter
                        "Attendance marked successfully.",
                        "ATT <Student ID> <Status>");
                break;

            case "EVA":
                handleUpdateCommand(parts, sessionAndDate, Evaluation.class,
                        StudentAttendance::setEvaluation, // Truyền method reference setter
                        "Evaluation updated successfully.",
                        "EVA <Student ID> <Evaluation>");
                break;

            default:
                System.out.println("Unknown command: " + parts[0]);
        }
        return sessionAndDate;
    }

    // === PHƯƠNG THỨC TỐI ƯU GENERIC CHO VIỆC CẬP NHẬT ===
    private static <T extends Enum<T>> void handleUpdateCommand(
            String[] parts,
            ClassSessionAndDate sessionData,
            Class<T> enumType,
            BiConsumer<StudentAttendance, T> updater, // Hàm setter sẽ được truyền vào đây
            String successMessage,
            String usageFormat) {

        if (parts.length != 3) {
            System.out.println("Invalid command format. Usage: " + usageFormat);
            return;
        }

        String studentId = parts[1];
        String valueStr = parts[2]; // Đã toUpperCase ở đầu controller

        try {
            // 1. Parse Enum (Tự động throw IllegalArgumentException nếu sai tên)
            T enumValue = Enum.valueOf(enumType, valueStr);

            DatabaseManager db = DatabaseManager.getInstance();
            StudentAttendance record = db.getStudentAttendance(
                    sessionData.attendanceDate, studentId, sessionData.idClassSession);

            // 2. Kiểm tra tồn tại
            if (record == null) {
                System.out.printf("No record found for Student: %s, Date: %s, Session: %s%n",
                        studentId, sessionData.attendanceDate, sessionData.idClassSession);
                return;
            }

            // 3. Thực hiện update thông qua updater được truyền vào
            updater.accept(record, enumValue);
            db.updateStudentAttendance(record);
            System.out.println(successMessage);

        } catch (IllegalArgumentException e) {
            // 4. Xử lý lỗi nhập sai Enum generic
            String validValues = Arrays.stream(enumType.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            System.out.println("Invalid value: " + valueStr);
            System.out.println("Valid values are: [" + validValues + "]");
        }
    }

    static void showStudentAttendanceDetail(List<StudentAttendance> list, ClassSessionAndDate sessionData) {
        System.out.println("\n==========================================");
        System.out.println("           ATTENDANCE REPORT");
        System.out.println("==========================================");
        System.out.println("Session: " + sessionData.getIdClassSession());
        System.out.println("Date   : " + sessionData.getAttendanceDate());
        System.out.println("Records: " + list.size());
        System.out.println("------------------------------------------");

        if (list.isEmpty()) {
            System.out.println("No attendance records found.");
        } else {
            System.out.printf("%-12s %-18s %-12s %-12s %-10s%n", "Date", "Name", "Student ID", "Attendance", "Evaluation");
            System.out.println("----------------------------------------------------------------------");
            for (StudentAttendance r : list) {
                System.out.printf("%-12s %-18s %-12s %-12s %-10s%n",
                        r.getCreatedDate(),
                        limitLength(r.getStudent().getNameStudent(), 18), // Optional: tránh vỡ layout nếu tên quá dài
                        r.getStudent().getIdStudent(),
                        r.getAttendance(),
                        (r.getEvaluation() != null) ? r.getEvaluation() : "___"
                );
            }
        }
        System.out.println("==========================================");
    }

    // Helper nhỏ để cắt chuỗi nếu quá dài khi in bảng
    private static String limitLength(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }
}