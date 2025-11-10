package com.dat.domain;

import java.util.ArrayList;
import java.util.List;

public class ClassSession {
    private String idClassSession;
    private Integer shift;
    private Integer weekday;
    private Branch branch; // ManyToOne với Branch
    private boolean isActive = true;
    private List<StudentClassSession> studentClassSessions; // OneToMany với StudentClassSession

    public ClassSession() {
        this.studentClassSessions = new ArrayList<>();
    }

    public ClassSession(String idClassSession, Integer shift, Integer weekday, Branch branch, boolean isActive) {
        this.idClassSession = idClassSession;
        this.shift = shift;
        this.weekday = weekday;
        this.branch = branch;
        this.isActive = isActive;
        this.studentClassSessions = new ArrayList<>();
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getIdClassSession() {
        return idClassSession;
    }

    public void setIdClassSession(String idClassSession) {
        this.idClassSession = idClassSession;
    }

    public Integer getShift() {
        return shift;
    }

    public void setShift(Integer shift) {
        this.shift = shift;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
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
        studentClassSession.setClassSession(this);
    }

    public void removeStudentClassSession(StudentClassSession studentClassSession) {
        this.studentClassSessions.remove(studentClassSession);
        studentClassSession.setClassSession(null);
    }
}
