/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author elie
 */
public class Performance implements Serializable {


    private static final long serialVersionUID = 1L;

    private int performanceId;
    private int studentId;   // store only student ID on client-side
    private int subjectId;   // store only subject ID on client-side
    private double score;
    private String grade;
    private String subjectName; // optional for GUI display

    public Performance() { }

    public Performance(int performanceId) {
        this.performanceId = performanceId;
    }

    public Performance(int performanceId, int studentId, int subjectId, double score, String grade, String subjectName) {
        this.performanceId = performanceId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.score = score;
        this.grade = grade;
        this.subjectName = subjectName;
    }

    // ================= GETTERS & SETTERS =================
    public int getPerformanceId() { return performanceId; }
    public void setPerformanceId(int performanceId) { this.performanceId = performanceId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

}
