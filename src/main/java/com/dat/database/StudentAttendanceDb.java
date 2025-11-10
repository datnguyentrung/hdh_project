package com.dat.database;

import com.dat.domain.Student;
import com.dat.domain.ClassSession;
import com.dat.domain.StudentAttendance;
import com.dat.enums.Attendance;
import com.dat.enums.Evaluation;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentAttendanceDb {
    private static StudentAttendanceDb instance;
    private List<StudentAttendance> studentAttendances;

    private StudentAttendanceDb() {
        studentAttendances = new ArrayList<>();
    }

    public static StudentAttendanceDb getInstance() {
        if (instance == null) {
            instance = new StudentAttendanceDb();
        }
        return instance;
    }

    public void initializeData() {
        if (!studentAttendances.isEmpty()) {
            return; // Already initialized
        }

        Student student1 = StudentDb.getInstance().getStudentById("ST001");
        Student student2 = StudentDb.getInstance().getStudentById("ST002");
        Student student3 = StudentDb.getInstance().getStudentById("ST003");
        Student student4 = StudentDb.getInstance().getStudentById("ST004");
        Student student5 = StudentDb.getInstance().getStudentById("ST005");

        ClassSession class1 = ClassSessionDb.getInstance().getClassSessionById("CS001");
        ClassSession class2 = ClassSessionDb.getInstance().getClassSessionById("CS002");
        ClassSession class3 = ClassSessionDb.getInstance().getClassSessionById("CS003");
        ClassSession class4 = ClassSessionDb.getInstance().getClassSessionById("CS004");
        ClassSession class5 = ClassSessionDb.getInstance().getClassSessionById("CS005");

        // Tạo fake data cho StudentAttendance với các ngày khác nhau
        // Week 1 (2024-11-04 to 2024-11-10)
        createAttendance(LocalDate.of(2024, 11, 4), student1, class1, Attendance.PRESENT, Evaluation.GOOD);
        createAttendance(LocalDate.of(2024, 11, 4), student2, class1, Attendance.PRESENT, Evaluation.AVERAGE);
        createAttendance(LocalDate.of(2024, 11, 4), student5, class1, Attendance.ABSENT, null);

        createAttendance(LocalDate.of(2024, 11, 6), student1, class2, Attendance.PRESENT, Evaluation.GOOD);
        createAttendance(LocalDate.of(2024, 11, 8), student2, class3, Attendance.EXCUSED, null);
        createAttendance(LocalDate.of(2024, 11, 8), student3, class3, Attendance.PRESENT, Evaluation.AVERAGE);

        createAttendance(LocalDate.of(2024, 11, 5), student4, class4, Attendance.PRESENT, Evaluation.GOOD);
        createAttendance(LocalDate.of(2024, 11, 7), student4, class5, Attendance.PRESENT, Evaluation.POOR);

        // Week 2 (2024-11-11 to 2024-11-17)
        createAttendance(LocalDate.of(2024, 11, 11), student1, class1, Attendance.PRESENT, Evaluation.AVERAGE);
        createAttendance(LocalDate.of(2024, 11, 11), student2, class1, Attendance.PRESENT, Evaluation.GOOD);
        createAttendance(LocalDate.of(2024, 11, 11), student5, class1, Attendance.PRESENT, Evaluation.POOR);

        createAttendance(LocalDate.of(2024, 11, 13), student1, class2, Attendance.ABSENT, null);
        createAttendance(LocalDate.of(2024, 11, 15), student2, class3, Attendance.PRESENT, Evaluation.GOOD);
        createAttendance(LocalDate.of(2024, 11, 15), student3, class3, Attendance.PRESENT, Evaluation.GOOD);

        createAttendance(LocalDate.of(2024, 11, 12), student4, class4, Attendance.PRESENT, Evaluation.AVERAGE);
        createAttendance(LocalDate.of(2024, 11, 14), student4, class5, Attendance.EXCUSED, null);

        // Week 3 (2024-11-18 to 2024-11-24)
        createAttendance(LocalDate.of(2024, 11, 18), student1, class1, Attendance.PRESENT, Evaluation.GOOD);
        createAttendance(LocalDate.of(2024, 11, 18), student2, class1, Attendance.ABSENT, null);
        createAttendance(LocalDate.of(2024, 11, 18), student5, class1, Attendance.PRESENT, Evaluation.AVERAGE);

        createAttendance(LocalDate.of(2024, 11, 20), student1, class2, Attendance.PRESENT, Evaluation.AVERAGE);
        createAttendance(LocalDate.of(2024, 11, 22), student2, class3, Attendance.PRESENT, Evaluation.AVERAGE);
        createAttendance(LocalDate.of(2024, 11, 22), student3, class3, Attendance.ABSENT, null);

        createAttendance(LocalDate.of(2024, 11, 19), student4, class4, Attendance.PRESENT, Evaluation.GOOD);
        createAttendance(LocalDate.of(2024, 11, 21), student4, class5, Attendance.PRESENT, Evaluation.GOOD);
    }

    private void createAttendance(LocalDate date, Student student, ClassSession classSession,
                                 Attendance attendance, Evaluation evaluation) {
        StudentAttendance sa = new StudentAttendance(date, student, classSession);
        sa.setAttendance(attendance);
        if (evaluation != null) {
            sa.setEvaluation(evaluation);
        }
        studentAttendances.add(sa);
    }

    // CRUD Operations
    public List<StudentAttendance> getAllStudentAttendances() {
        return new ArrayList<>(studentAttendances);
    }

    public void addStudentAttendance(StudentAttendance studentAttendance) {
        studentAttendances.add(studentAttendance);
    }

    public boolean updateStudentAttendance(StudentAttendance updatedAttendance) {
        for (int i = 0; i < studentAttendances.size(); i++) {
            StudentAttendance sa = studentAttendances.get(i);
            if (sa.getCreatedDate().equals(updatedAttendance.getCreatedDate()) &&
                sa.getStudent().getIdStudent().equals(updatedAttendance.getStudent().getIdStudent()) &&
                sa.getClassSession().getIdClassSession().equals(updatedAttendance.getClassSession().getIdClassSession())) {
                studentAttendances.set(i, updatedAttendance);
                return true;
            }
        }
        return false;
    }

    public boolean deleteStudentAttendance(LocalDate date, String studentId, String classSessionId) {
        return studentAttendances.removeIf(sa ->
                sa.getCreatedDate().equals(date) &&
                sa.getStudent().getIdStudent().equals(studentId) &&
                sa.getClassSession().getIdClassSession().equals(classSessionId));
    }

    // Search and Filter Operations
    public StudentAttendance getStudentAttendance(LocalDate date, String studentId, String classSessionId) {
        return studentAttendances.stream()
                .filter(sa -> sa.getCreatedDate().equals(date) &&
                             sa.getStudent().getIdStudent().equals(studentId) &&
                             sa.getClassSession().getIdClassSession().equals(classSessionId))
                .findFirst()
                .orElse(null);
    }

    public List<StudentAttendance> getAttendancesByStudent(String studentId) {
        return studentAttendances.stream()
                .filter(sa -> sa.getStudent().getIdStudent().equals(studentId))
                .collect(Collectors.toList());
    }

    public List<StudentAttendance> getAttendancesByClassSession(String classSessionId) {
        return studentAttendances.stream()
                .filter(sa -> sa.getClassSession().getIdClassSession().equals(classSessionId))
                .collect(Collectors.toList());
    }

    public List<StudentAttendance> getAttendancesByDate(LocalDate date) {
        return studentAttendances.stream()
                .filter(sa -> sa.getCreatedDate().equals(date))
                .collect(Collectors.toList());
    }

    public List<StudentAttendance> getAttendancesByDateRange(LocalDate startDate, LocalDate endDate) {
        return studentAttendances.stream()
                .filter(sa -> !sa.getCreatedDate().isBefore(startDate) &&
                             !sa.getCreatedDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<StudentAttendance> getAttendancesByStatus(Attendance attendance) {
        return studentAttendances.stream()
                .filter(sa -> sa.getAttendance() == attendance)
                .collect(Collectors.toList());
    }

    public List<StudentAttendance> getAttendancesByEvaluation(Evaluation evaluation) {
        return studentAttendances.stream()
                .filter(sa -> sa.getEvaluation() == evaluation)
                .collect(Collectors.toList());
    }

    public List<StudentAttendance> getAttendancesByClassSessionAndDate(String classSessionId, LocalDate date) {
        return studentAttendances.stream()
                .filter(sa -> sa.getClassSession().getIdClassSession().equals(classSessionId) &&
                             sa.getCreatedDate().equals(date))
                .collect(Collectors.toList());
    }

    public List<StudentAttendance> getAttendancesByStudentAndDateRange(String studentId, LocalDate startDate, LocalDate endDate) {
        return studentAttendances.stream()
                .filter(sa -> sa.getStudent().getIdStudent().equals(studentId) &&
                             !sa.getCreatedDate().isBefore(startDate) &&
                             !sa.getCreatedDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<StudentAttendance> getAttendancesByClassAndDateRange(String classSessionId, LocalDate startDate, LocalDate endDate) {
        return studentAttendances.stream()
                .filter(sa -> sa.getClassSession().getIdClassSession().equals(classSessionId) &&
                             !sa.getCreatedDate().isBefore(startDate) &&
                             !sa.getCreatedDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    // Statistical Methods
    public long countAttendancesByStatus(Attendance attendance) {
        return studentAttendances.stream()
                .filter(sa -> sa.getAttendance() == attendance)
                .count();
    }

    public long countAttendancesByStudent(String studentId) {
        return studentAttendances.stream()
                .filter(sa -> sa.getStudent().getIdStudent().equals(studentId))
                .count();
    }

    public double getAttendanceRateByStudent(String studentId) {
        List<StudentAttendance> studentAttendanceList = getAttendancesByStudent(studentId);
        if (studentAttendanceList.isEmpty()) {
            return 0.0;
        }

        long presentCount = studentAttendanceList.stream()
                .filter(sa -> sa.getAttendance() == Attendance.PRESENT)
                .count();

        return (double) presentCount / studentAttendanceList.size() * 100;
    }

    public void printStatistics() {
        System.out.println("=== STUDENT ATTENDANCE STATISTICS ===");
        System.out.println("Total attendance records: " + studentAttendances.size());
        System.out.println("Present: " + countAttendancesByStatus(Attendance.PRESENT));
        System.out.println("Absent: " + countAttendancesByStatus(Attendance.ABSENT));
        System.out.println("Excused: " + countAttendancesByStatus(Attendance.EXCUSED));

        System.out.println("\nAttendance rate by student:");
        for (Student student : StudentDb.getInstance().getAllStudents()) {
            double rate = getAttendanceRateByStudent(student.getIdStudent());
            System.out.printf("%s (%s): %.1f%%\n",
                    student.getNameStudent(), student.getIdStudent(), rate);
        }
    }
}
