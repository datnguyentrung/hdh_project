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

        // =================== DỮ LIỆU HÔM NAY (11/11/2025) ===================
        LocalDate today = LocalDate.of(2025, 11, 11);

        // Class CS001 - Thứ 2 (Monday) - Shift 1
        createAttendance(today, student1, class1, Attendance.PRESENT, Evaluation.GOOD);
        createAttendance(today, student2, class1, Attendance.PRESENT, Evaluation.GOOD);
        createAttendance(today, student5, class1, Attendance.ABSENT, null);

        // Class CS002 - Thứ 4 (Wednesday) - Shift 2
        createAttendance(today, student1, class2, Attendance.PRESENT, Evaluation.GOOD);

        // Class CS003 - Thứ 6 (Friday) - Shift 1
        createAttendance(today, student2, class3, Attendance.PRESENT, Evaluation.POOR);
        createAttendance(today, student3, class3, Attendance.PRESENT, Evaluation.AVERAGE);

        // Class CS004 - Thứ 3 (Tuesday) - Shift 3
        createAttendance(today, student4, class4, Attendance.EXCUSED, null);

        // Class CS005 - Thứ 5 (Thursday) - Shift 2
        createAttendance(today, student4, class5, Attendance.PRESENT, Evaluation.GOOD);
        createAttendance(today, student3, class5, Attendance.PRESENT, Evaluation.AVERAGE);
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

    // Method để thêm dữ liệu ngẫu nhiên cho tất cả students và classes
    private void addRandomAttendanceData() {
        List<Student> allStudents = StudentDb.getInstance().getAllStudents();
        List<ClassSession> allClasses = ClassSessionDb.getInstance().getAllClassSessions();

        // Thêm dữ liệu cho các tháng 3, 4, 5/2025
        LocalDate[] months = {
            LocalDate.of(2025, 3, 1), LocalDate.of(2025, 4, 1), LocalDate.of(2025, 5, 1),
            LocalDate.of(2025, 6, 1), LocalDate.of(2025, 7, 1), LocalDate.of(2025, 8, 1),
            LocalDate.of(2025, 9, 1), LocalDate.of(2025, 10, 1)
        };

        for (LocalDate month : months) {
            // Tạo 8-12 records mỗi tháng
            for (int week = 0; week < 3; week++) {
                LocalDate weekStart = month.plusDays(week * 7);

                // Random attendance cho mỗi tuần
                for (int i = 0; i < 4; i++) {
                    LocalDate date = weekStart.plusDays(i * 2); // Mỗi 2 ngày

                    // Random chọn student và class
                    Student randomStudent = allStudents.get(i % allStudents.size());
                    ClassSession randomClass = allClasses.get(i % allClasses.size());

                    // Random attendance status (80% present, 15% absent, 5% excused)
                    Attendance attendance;
                    Evaluation evaluation = null;

                    int rand = i % 20;
                    // 80% present
                    attendance = Attendance.PRESENT;
                    // Random evaluation for present students
                    int evalRand = i % 4;
                    evaluation = switch (evalRand) {
                        case 0, 1 -> Evaluation.GOOD;
                        case 2 -> Evaluation.AVERAGE;
                        case 3 -> Evaluation.POOR;
                        default -> evaluation;
                    };

                    // Check if already exists to avoid duplicates
                    if (getStudentAttendance(date, randomStudent.getIdStudent(), randomClass.getIdClassSession()) == null) {
                        createAttendance(date, randomStudent, randomClass, attendance, evaluation);
                    }
                }
            }
        }

        // Thêm dữ liệu đặc biệt cho student3 và student4 ở các classes còn lại
        addSpecialAttendanceForStudents();
    }

    // Method để thêm dữ liệu đặc biệt cho students ít dữ liệu
    private void addSpecialAttendanceForStudents() {
        Student student3 = StudentDb.getInstance().getStudentById("ST003");
        Student student4 = StudentDb.getInstance().getStudentById("ST004");
        Student student5 = StudentDb.getInstance().getStudentById("ST005");

        ClassSession class1 = ClassSessionDb.getInstance().getClassSessionById("CS001");
        ClassSession class2 = ClassSessionDb.getInstance().getClassSessionById("CS002");
        ClassSession class4 = ClassSessionDb.getInstance().getClassSessionById("CS004");
        ClassSession class5 = ClassSessionDb.getInstance().getClassSessionById("CS005");

        // Thêm dữ liệu cho student3 ở class1 và class2
        LocalDate[] dates1 = {
            LocalDate.of(2024, 9, 5), LocalDate.of(2024, 9, 12), LocalDate.of(2024, 9, 19),
            LocalDate.of(2025, 3, 5), LocalDate.of(2025, 3, 12), LocalDate.of(2025, 4, 9),
            LocalDate.of(2025, 5, 7), LocalDate.of(2025, 6, 4)
        };

        for (int i = 0; i < dates1.length; i++) {
            // Student3 ở class1
            if (getStudentAttendance(dates1[i], student3.getIdStudent(), class1.getIdClassSession()) == null) {
                Attendance att = (i % 3 == 0) ? Attendance.ABSENT : Attendance.PRESENT;
                Evaluation eval = (att == Attendance.PRESENT) ?
                    (i % 2 == 0 ? Evaluation.GOOD : Evaluation.AVERAGE) : null;
                createAttendance(dates1[i], student3, class1, att, eval);
            }

            // Student3 ở class2
            if (getStudentAttendance(dates1[i].plusDays(2), student3.getIdStudent(), class2.getIdClassSession()) == null) {
                createAttendance(dates1[i].plusDays(2), student3, class2, Attendance.PRESENT, Evaluation.GOOD);
            }
        }

        // Thêm dữ liệu cho student5 ở các class khác
        LocalDate[] dates2 = {
            LocalDate.of(2024, 8, 15), LocalDate.of(2024, 8, 22), LocalDate.of(2024, 9, 10),
            LocalDate.of(2025, 4, 15), LocalDate.of(2025, 5, 20), LocalDate.of(2025, 6, 18),
            LocalDate.of(2025, 7, 16), LocalDate.of(2025, 8, 20)
        };

        for (int i = 0; i < dates2.length; i++) {
            // Student5 ở class2
            if (getStudentAttendance(dates2[i], student5.getIdStudent(), class2.getIdClassSession()) == null) {
                Attendance att = (i % 4 == 0) ? Attendance.EXCUSED : Attendance.PRESENT;
                Evaluation eval = (att == Attendance.PRESENT) ? Evaluation.AVERAGE : null;
                createAttendance(dates2[i], student5, class2, att, eval);
            }

            // Student5 ở class4
            if (getStudentAttendance(dates2[i].plusDays(1), student5.getIdStudent(), class4.getIdClassSession()) == null) {
                createAttendance(dates2[i].plusDays(1), student5, class4, Attendance.PRESENT, Evaluation.GOOD);
            }
        }

        // Thêm performance tracking data cho tất cả students
        addPerformanceTrackingData();
    }

    // Method để thêm dữ liệu theo dõi hiệu suất
    private void addPerformanceTrackingData() {
        List<Student> students = StudentDb.getInstance().getAllStudents();
        List<ClassSession> classes = ClassSessionDb.getInstance().getAllClassSessions();

        // Tạo pattern attendance cho mỗi student
        for (Student student : students) {
            for (ClassSession classSession : classes) {
                // Chỉ thêm nếu student đã có ít nhất 1 record với class này
                boolean hasRecord = studentAttendances.stream()
                    .anyMatch(sa -> sa.getStudent().getIdStudent().equals(student.getIdStudent()) &&
                                  sa.getClassSession().getIdClassSession().equals(classSession.getIdClassSession()));

                if (hasRecord) {
                    // Thêm 2-3 records gần đây cho mỗi combination
                    LocalDate[] recentDates = {
                        LocalDate.of(2025, 10, 15), LocalDate.of(2025, 10, 22), LocalDate.of(2025, 11, 1)
                    };

                    for (int i = 0; i < recentDates.length; i++) {
                        if (getStudentAttendance(recentDates[i], student.getIdStudent(), classSession.getIdClassSession()) == null) {
                            // Tạo realistic attendance pattern
                            Attendance attendance = Attendance.PRESENT;
                            Evaluation evaluation = Evaluation.GOOD;

                            // Một số students có attendance pattern khác nhau
                            if (student.getIdStudent().equals("ST002")) {
                                // Student 2 có attendance tốt
                                evaluation = (i == 0) ? Evaluation.GOOD : Evaluation.GOOD;
                            } else if (student.getIdStudent().equals("ST005")) {
                                // Student 5 có một số vấn đề attendance
                                if (i == 1) {
                                    attendance = Attendance.ABSENT;
                                    evaluation = null;
                                } else {
                                    evaluation = Evaluation.AVERAGE;
                                }
                            }

                            createAttendance(recentDates[i], student, classSession, attendance, evaluation);
                        }
                    }
                }
            }
        }
    }
}
