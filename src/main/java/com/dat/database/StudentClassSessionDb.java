package com.dat.database;

import com.dat.domain.Student;
import com.dat.domain.ClassSession;
import com.dat.domain.StudentClassSession;
import java.util.ArrayList;
import java.util.List;

public class StudentClassSessionDb {
    private static StudentClassSessionDb instance;
    private List<StudentClassSession> studentClassSessions;

    private StudentClassSessionDb() {
        studentClassSessions = new ArrayList<>();
    }

    public static StudentClassSessionDb getInstance() {
        if (instance == null) {
            instance = new StudentClassSessionDb();
        }
        return instance;
    }

    public void initializeData() {
        if (!studentClassSessions.isEmpty()) {
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

        // Create StudentClassSession data (Many-to-Many relationship)
        StudentClassSession scs1 = new StudentClassSession(student1, class1, true);
        StudentClassSession scs2 = new StudentClassSession(student1, class2, true);
        StudentClassSession scs3 = new StudentClassSession(student2, class1, true);
        StudentClassSession scs4 = new StudentClassSession(student2, class3, true);
        StudentClassSession scs5 = new StudentClassSession(student3, class3, true);
        StudentClassSession scs6 = new StudentClassSession(student4, class4, true);
        StudentClassSession scs7 = new StudentClassSession(student4, class5, true);
        StudentClassSession scs8 = new StudentClassSession(student5, class1, true);

        studentClassSessions.add(scs1);
        studentClassSessions.add(scs2);
        studentClassSessions.add(scs3);
        studentClassSessions.add(scs4);
        studentClassSessions.add(scs5);
        studentClassSessions.add(scs6);
        studentClassSessions.add(scs7);
        studentClassSessions.add(scs8);

        // Establish relationships
        student1.addStudentClassSession(scs1);
        student1.addStudentClassSession(scs2);
        student2.addStudentClassSession(scs3);
        student2.addStudentClassSession(scs4);
        student3.addStudentClassSession(scs5);
        student4.addStudentClassSession(scs6);
        student4.addStudentClassSession(scs7);
        student5.addStudentClassSession(scs8);

        class1.addStudentClassSession(scs1);
        class1.addStudentClassSession(scs3);
        class1.addStudentClassSession(scs8);
        class2.addStudentClassSession(scs2);
        class3.addStudentClassSession(scs4);
        class3.addStudentClassSession(scs5);
        class4.addStudentClassSession(scs6);
        class5.addStudentClassSession(scs7);
    }

    public List<StudentClassSession> getAllStudentClassSessions() {
        return new ArrayList<>(studentClassSessions);
    }

    public List<StudentClassSession> getStudentClassSessionsByStudent(String studentId) {
        return studentClassSessions.stream()
                .filter(scs -> scs.getStudent().getIdStudent().equals(studentId))
                .toList();
    }

    public List<StudentClassSession> getStudentClassSessionsByClassSession(String classSessionId) {
        return studentClassSessions.stream()
                .filter(scs -> scs.getClassSession().getIdClassSession().equals(classSessionId))
                .toList();
    }

    public void addStudentClassSession(StudentClassSession studentClassSession) {
        studentClassSessions.add(studentClassSession);
    }

    public boolean deleteStudentClassSession(String studentId, String classSessionId) {
        return studentClassSessions.removeIf(scs ->
                scs.getStudent().getIdStudent().equals(studentId) &&
                scs.getClassSession().getIdClassSession().equals(classSessionId));
    }

    public StudentClassSession getStudentClassSession(String studentId, String classSessionId) {
        return studentClassSessions.stream()
                .filter(scs -> scs.getStudent().getIdStudent().equals(studentId) &&
                              scs.getClassSession().getIdClassSession().equals(classSessionId))
                .findFirst()
                .orElse(null);
    }
}
