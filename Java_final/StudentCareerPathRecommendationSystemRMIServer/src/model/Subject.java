/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author elie
 */
@Entity
public class Subject implements Serializable {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int subjectId;

    @Column(nullable = false, unique = true)
    private String subjectName;

    private String description;

    @Column(nullable = false)
    private String level;   // Beginner, Intermediate, Advanced

    @Column(nullable = false)
    private int credits;

    public Subject() {
    }

    public Subject(int subjectId) {
        this.subjectId = subjectId;
    }

    public Subject(int subjectId, String subjectName, String description, String level, int credits) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.description = description;
        this.level = level;
        this.credits = credits;
    }

    // ============ GETTERS & SETTERS ===============

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

}
