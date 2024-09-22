/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.cput.timetableproject.domain;

/**
 *
 * @author hloni
 */
public class Subject {
     private int subjectCode;
    private String description;

    // Constructor
    public Subject(int subjectCode, String description) {
        this.subjectCode = subjectCode;
        this.description = description;
    }

    // Getters and Setters
    public int getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(int subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return  subjectCode + " " +
               description 
               ;
    }

}
