package com.dat.database;

import com.dat.domain.*;
import java.time.LocalDate;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager instance;

    private BranchDb branchDb;
    private StudentDb studentDb;
    private ClassSessionDb classSessionDb;
    private StudentClassSessionDb studentClassSessionDb;
    private StudentAttendanceDb studentAttendanceDb;

    private DatabaseManager() {
        initializeDatabases();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDatabases() {
        // Khởi tạo theo thứ tự dependencies
        branchDb = BranchDb.getInstance();
        studentDb = StudentDb.getInstance();
        classSessionDb = ClassSessionDb.getInstance();

        // Initialize relationships after all basic databases are ready
        studentClassSessionDb = StudentClassSessionDb.getInstance();
        studentClassSessionDb.initializeData();

        studentAttendanceDb = StudentAttendanceDb.getInstance();
        studentAttendanceDb.initializeData();
    }

    // Branch operations
    public List<Branch> getAllBranches() {
        return branchDb.getAllBranches();
    }

    public Branch getBranchById(Integer id) {
        return branchDb.getBranchById(id);
    }

    public void addBranch(Branch branch) {
        branchDb.addBranch(branch);
    }

    public boolean updateBranch(Branch branch) {
        return branchDb.updateBranch(branch);
    }

    public boolean deleteBranch(Integer id) {
        return branchDb.deleteBranch(id);
    }

    // Student operations
    public List<Student> getAllStudents() {
        return studentDb.getAllStudents();
    }

    public Student getStudentById(String id) {
        return studentDb.getStudentById(id);
    }

    public List<Student> getActiveStudents() {
        return studentDb.getActiveStudents();
    }

    public void addStudent(Student student) {
        studentDb.addStudent(student);
    }

    public boolean updateStudent(Student student) {
        return studentDb.updateStudent(student);
    }

    public boolean deleteStudent(String id) {
        return studentDb.deleteStudent(id);
    }

    // ClassSession operations
    public List<ClassSession> getAllClassSessions() {
        return classSessionDb.getAllClassSessions();
    }

    public ClassSession getClassSessionById(String id) {
        return classSessionDb.getClassSessionById(id);
    }

    public List<ClassSession> getClassSessionsByBranch(Integer branchId) {
        return classSessionDb.getClassSessionsByBranch(branchId);
    }

    public boolean existsClassSession(String id) {
        return classSessionDb.existsClassSession(id);
    }

    public void addClassSession(ClassSession classSession) {
        classSessionDb.addClassSession(classSession);
    }

    public boolean updateClassSession(ClassSession classSession) {
        return classSessionDb.updateClassSession(classSession);
    }

    public boolean deleteClassSession(String id) {
        return classSessionDb.deleteClassSession(id);
    }

    // StudentClassSession operations
    public List<StudentClassSession> getAllStudentClassSessions() {
        return studentClassSessionDb.getAllStudentClassSessions();
    }

    public List<StudentClassSession> getStudentClassSessionsByStudent(String studentId) {
        return studentClassSessionDb.getStudentClassSessionsByStudent(studentId);
    }

    public List<StudentClassSession> getStudentClassSessionsByClassSession(String classSessionId) {
        return studentClassSessionDb.getStudentClassSessionsByClassSession(classSessionId);
    }

    public void addStudentClassSession(StudentClassSession studentClassSession) {
        studentClassSessionDb.addStudentClassSession(studentClassSession);
    }

    public boolean deleteStudentClassSession(String studentId, String classSessionId) {
        return studentClassSessionDb.deleteStudentClassSession(studentId, classSessionId);
    }

    // StudentAttendance operations
    public List<StudentAttendance> getAllStudentAttendances() {
        return studentAttendanceDb.getAllStudentAttendances();
    }

    public List<StudentAttendance> getStudentAttendancesByClassSessionAndDate(String classSessionId, LocalDate date) {
        return studentAttendanceDb.getAttendancesByClassSessionAndDate(classSessionId, date);
    }

    public StudentAttendance getStudentAttendance(LocalDate date, String studentId, String classSessionId) {
        return studentAttendanceDb.getStudentAttendance(date, studentId, classSessionId);
    }

    public List<StudentAttendance> getAttendancesByStudent(String studentId) {
        return studentAttendanceDb.getAttendancesByStudent(studentId);
    }

    public List<StudentAttendance> getAttendancesByClassSession(String classSessionId) {
        return studentAttendanceDb.getAttendancesByClassSession(classSessionId);
    }

    public List<StudentAttendance> getAttendancesByDate(LocalDate date) {
        return studentAttendanceDb.getAttendancesByDate(date);
    }

    public List<StudentAttendance> getAttendancesByDateRange(LocalDate startDate, LocalDate endDate) {
        return studentAttendanceDb.getAttendancesByDateRange(startDate, endDate);
    }

    public void addStudentAttendance(StudentAttendance studentAttendance) {
        studentAttendanceDb.addStudentAttendance(studentAttendance);
    }

    public void updateStudentAttendance(StudentAttendance studentAttendance) {
        studentAttendanceDb.updateStudentAttendance(studentAttendance);
    }

    public boolean deleteStudentAttendance(LocalDate date, String studentId, String classSessionId) {
        return studentAttendanceDb.deleteStudentAttendance(date, studentId, classSessionId);
    }

    public double getAttendanceRateByStudent(String studentId) {
        return studentAttendanceDb.getAttendanceRateByStudent(studentId);
    }

    // Utility methods
    public void printAllStatistics() {
        System.out.println("=== DATABASE STATISTICS ===");
        System.out.println("Total branches: " + branchDb.getAllBranches().size());
        System.out.println("Total students: " + studentDb.getAllStudents().size());
        System.out.println("Total class sessions: " + classSessionDb.getAllClassSessions().size());
        System.out.println("Total student-class enrollments: " + studentClassSessionDb.getAllStudentClassSessions().size());
        System.out.println("Total attendance records: " + studentAttendanceDb.getAllStudentAttendances().size());

        System.out.println("\n");
        studentAttendanceDb.printStatistics();
    }

    public void printAllRelationships() {
        System.out.println("=== RELATIONSHIP INFORMATION ===");

        // OneToMany: Branch -> ClassSession
        System.out.println("\n1. Branch -> ClassSessions (OneToMany):");
        for (Branch branch : branchDb.getAllBranches()) {
            System.out.println("Branch " + branch.getIdBranch() + " has " + branch.getClassSessions().size() + " class sessions:");
            for (ClassSession cs : branch.getClassSessions()) {
                System.out.println("  - Class " + cs.getIdClassSession() + " (Shift " + cs.getShift() + ", Weekday " + cs.getWeekday() + ")");
            }
        }

        // OneToMany: Student -> StudentClassSessions
        System.out.println("\n2. Student -> StudentClassSessions (OneToMany):");
        for (Student student : studentDb.getAllStudents()) {
            if (!student.getStudentClassSessions().isEmpty()) {
                System.out.println("Student " + student.getNameStudent() + " enrolled in " + student.getStudentClassSessions().size() + " classes:");
                for (StudentClassSession scs : student.getStudentClassSessions()) {
                    System.out.println("  - Class " + scs.getClassSession().getIdClassSession());
                }
            }
        }

        // OneToMany: ClassSession -> StudentClassSessions
        System.out.println("\n3. ClassSession -> StudentClassSessions (OneToMany):");
        for (ClassSession classSession : classSessionDb.getAllClassSessions()) {
            if (!classSession.getStudentClassSessions().isEmpty()) {
                System.out.println("Class " + classSession.getIdClassSession() + " has " + classSession.getStudentClassSessions().size() + " students:");
                for (StudentClassSession scs : classSession.getStudentClassSessions()) {
                    System.out.println("  - " + scs.getStudent().getNameStudent() + " (" + scs.getStudent().getBeltLevel() + ")");
                }
            }
        }
    }
}
