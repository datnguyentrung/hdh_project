package com.dat.database;

import com.dat.domain.Branch;
import com.dat.domain.ClassSession;
import java.util.ArrayList;
import java.util.List;

public class ClassSessionDb {
    private static ClassSessionDb instance;
    private List<ClassSession> classSessions;

    private ClassSessionDb() {
        initializeData();
    }

    public static ClassSessionDb getInstance() {
        if (instance == null) {
            instance = new ClassSessionDb();
        }
        return instance;
    }

    private void initializeData() {
        classSessions = new ArrayList<>();
        BranchDb branchDb = BranchDb.getInstance();

        Branch branch1 = branchDb.getBranchById(1);
        Branch branch2 = branchDb.getBranchById(2);
        Branch branch3 = branchDb.getBranchById(3);

        ClassSession class1 = new ClassSession("CS001", 1, 2, branch1, true); // Shift 1, Monday
        ClassSession class2 = new ClassSession("CS002", 2, 4, branch1, true); // Shift 2, Wednesday
        ClassSession class3 = new ClassSession("CS003", 1, 6, branch2, true); // Shift 1, Friday
        ClassSession class4 = new ClassSession("CS004", 3, 3, branch2, true); // Shift 3, Tuesday
        ClassSession class5 = new ClassSession("CS005", 2, 5, branch3, true); // Shift 2, Thursday

        classSessions.add(class1);
        classSessions.add(class2);
        classSessions.add(class3);
        classSessions.add(class4);
        classSessions.add(class5);

        // Establish Branch -> ClassSession relationship
        branch1.addClassSession(class1);
        branch1.addClassSession(class2);
        branch2.addClassSession(class3);
        branch2.addClassSession(class4);
        branch3.addClassSession(class5);
    }

    public List<ClassSession> getAllClassSessions() {
        return new ArrayList<>(classSessions);
    }

    public boolean existsClassSession(String id) {
        return classSessions.stream()
                .anyMatch(classSession -> classSession.getIdClassSession().equals(id));
    }

    public ClassSession getClassSessionById(String id) {
        return classSessions.stream()
                .filter(classSession -> classSession.getIdClassSession().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<ClassSession> getClassSessionsByBranch(Integer branchId) {
        return classSessions.stream()
                .filter(cs -> cs.getBranch().getIdBranch().equals(branchId))
                .toList();
    }

    public List<ClassSession> getClassSessionsByShift(Integer shift) {
        return classSessions.stream()
                .filter(cs -> cs.getShift().equals(shift))
                .toList();
    }

    public List<ClassSession> getClassSessionsByWeekday(Integer weekday) {
        return classSessions.stream()
                .filter(cs -> cs.getWeekday().equals(weekday))
                .toList();
    }

    public void addClassSession(ClassSession classSession) {
        classSessions.add(classSession);
    }

    public boolean updateClassSession(ClassSession updatedClassSession) {
        for (int i = 0; i < classSessions.size(); i++) {
            if (classSessions.get(i).getIdClassSession().equals(updatedClassSession.getIdClassSession())) {
                classSessions.set(i, updatedClassSession);
                return true;
            }
        }
        return false;
    }

    public boolean deleteClassSession(String id) {
        return classSessions.removeIf(cs -> cs.getIdClassSession().equals(id));
    }
}
