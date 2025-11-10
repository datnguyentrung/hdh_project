package com.dat.domain;

public class StudentClassSession {
    private Student student;
    private ClassSession classSession;
    private boolean isActive;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ClassSession getClassSession() {
        return classSession;
    }

    public void setClassSession(ClassSession classSession) {
        this.classSession = classSession;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public StudentClassSession() {}

    public StudentClassSession(Student student, ClassSession classSession, boolean isActive) {
        this.student = student;
        this.classSession = classSession;
        this.isActive = isActive;
    }
}
