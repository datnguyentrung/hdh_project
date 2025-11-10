package com.dat.domain;

import com.dat.enums.BeltLevel;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String idStudent;
    private String nameStudent;
    private String phoneNumber;
    private BeltLevel beltLevel = BeltLevel.WHITE;
    private boolean isActive = true;
    private List<StudentClassSession> studentClassSessions; // OneToMany với StudentClassSession

    public Student() {
        this.studentClassSessions = new ArrayList<>();
    }

    public Student(String idStudent, String nameStudent, String phoneNumber, BeltLevel beltLevel, boolean isActive) {
        this.idStudent = idStudent;
        this.nameStudent = nameStudent;
        this.phoneNumber = phoneNumber;
        this.beltLevel = beltLevel;
        this.isActive = isActive;
        this.studentClassSessions = new ArrayList<>();
    }

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getNameStudent() {
        return nameStudent;
    }

    public void setNameStudent(String nameStudent) {
        this.nameStudent = nameStudent;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BeltLevel getBeltLevel() {
        return beltLevel;
    }

    public void setBeltLevel(BeltLevel beltLevel) {
        this.beltLevel = beltLevel;
    }

    public List<StudentClassSession> getStudentClassSessions() {
        return studentClassSessions;
    }

    public void setStudentClassSessions(List<StudentClassSession> studentClassSessions) {
        this.studentClassSessions = studentClassSessions;
    }

    // Helper methods để quản lý mối quan hệ
    public void addStudentClassSession(StudentClassSession studentClassSession) {
        this.studentClassSessions.add(studentClassSession);
        studentClassSession.setStudent(this);
    }

    public void removeStudentClassSession(StudentClassSession studentClassSession) {
        this.studentClassSessions.remove(studentClassSession);
        studentClassSession.setStudent(null);
    }
}
