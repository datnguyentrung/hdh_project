package com.dat.domain;

import java.util.ArrayList;
import java.util.List;

public class Branch {
    private Integer idBranch;
    private boolean isActive = true;
    private List<ClassSession> classSessions; // OneToMany với ClassSession

    public Branch() {
        this.classSessions = new ArrayList<>();
    }

    public Branch(Integer idBranch, boolean isActive) {
        this.idBranch = idBranch;
        this.isActive = isActive;
        this.classSessions = new ArrayList<>();
    }

    // Getters and Setters
    public Integer getIdBranch() {
        return idBranch;
    }

    public void setIdBranch(Integer idBranch) {
        this.idBranch = idBranch;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<ClassSession> getClassSessions() {
        return classSessions;
    }

    public void setClassSessions(List<ClassSession> classSessions) {
        this.classSessions = classSessions;
    }

    // Helper methods để quản lý mối quan hệ
    public void addClassSession(ClassSession classSession) {
        this.classSessions.add(classSession);
        classSession.setBranch(this);
    }

    public void removeClassSession(ClassSession classSession) {
        this.classSessions.remove(classSession);
        classSession.setBranch(null);
    }
}
