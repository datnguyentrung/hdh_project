package com.dat.database;

import com.dat.domain.Student;
import com.dat.enums.BeltLevel;
import java.util.ArrayList;
import java.util.List;

public class StudentDb {
    private static StudentDb instance;
    private List<Student> students;

    private StudentDb() {
        initializeData();
    }

    public static StudentDb getInstance() {
        if (instance == null) {
            instance = new StudentDb();
        }
        return instance;
    }

    private void initializeData() {
        students = new ArrayList<>();

        Student student1 = new Student("ST001", "Nguyen Van A", "0123456789", BeltLevel.GREEN, true);
        Student student2 = new Student("ST002", "Tran Thi B", "0987654321", BeltLevel.BLUE, true);
        Student student3 = new Student("ST003", "Le Van C", "0111222333", BeltLevel.BROWN, true);
        Student student4 = new Student("ST004", "Pham Van D", "0444555666", BeltLevel.BLACK, true);
        Student student5 = new Student("ST005", "Hoang Thi E", "0777888999", BeltLevel.WHITE, true);

        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public Student getStudentById(String id) {
        return students.stream()
                .filter(student -> student.getIdStudent().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Student> getActiveStudents() {
        return students.stream()
                .filter(Student::isActive)
                .toList();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public boolean updateStudent(Student updatedStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getIdStudent().equals(updatedStudent.getIdStudent())) {
                students.set(i, updatedStudent);
                return true;
            }
        }
        return false;
    }

    public boolean deleteStudent(String id) {
        return students.removeIf(student -> student.getIdStudent().equals(id));
    }

    public List<Student> searchByName(String name) {
        return students.stream()
                .filter(student -> student.getNameStudent().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public List<Student> getStudentsByBeltLevel(BeltLevel beltLevel) {
        return students.stream()
                .filter(student -> student.getBeltLevel() == beltLevel)
                .toList();
    }
}
