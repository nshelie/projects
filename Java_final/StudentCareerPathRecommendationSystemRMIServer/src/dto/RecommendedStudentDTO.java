package dto;

import java.io.Serializable;

public class RecommendedStudentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int studentId;
    private String fullName;
    private String email;
    private String department;

    // Ranking score (typically average score across required subjects)
    private double score;

    public RecommendedStudentDTO() {
    }

    public RecommendedStudentDTO(int studentId, String fullName, String email, String department, double score) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.department = department;
        this.score = score;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
