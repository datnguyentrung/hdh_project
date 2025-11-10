package com.dat.domain;

import com.dat.enums.Attendance;
import com.dat.enums.Evaluation;

import java.time.LocalDate;
import java.util.Objects;

public class StudentAttendance {
    private final LocalDate createdDate; // Nên đặt là final nếu được thiết lập qua constructor
    private final Student student;       // Nên đặt là final
    private final ClassSession classSession; // Nên đặt là final

    private Attendance attendance = Attendance.ABSENT;
    private Evaluation evaluation;

    public StudentAttendance(LocalDate createdDate, Student student, ClassSession classSession) {
        this.createdDate = createdDate;
        this.student = student;
        this.classSession = classSession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentAttendance that = (StudentAttendance) o;
        return Objects.equals(createdDate, that.createdDate) &&
                Objects.equals(student, that.student) &&
                Objects.equals(classSession, that.classSession);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdDate, student, classSession);
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public ClassSession getClassSession() {
        return classSession;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public Student getStudent() {
        return student;
    }
}
