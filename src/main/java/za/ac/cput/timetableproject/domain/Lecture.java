/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.cput.timetableproject.domain;

/**
 *
 * @author hloni
 */
public class Lecture {
      private int LectureID;
    private String LectureName;
    private String LectureSurname;
    private String LectureIntials;
    public Lecture(String lectureName, String lectureSurname, String lectureIntials) {
        this(-1, lectureName, lectureSurname, lectureIntials); 
    }

    public Lecture(int LectureID ,String LectureName, String LectureSurname, String LectureIntials) {
        this.LectureID = LectureID;
        this.LectureName = LectureName;
        this.LectureSurname = LectureSurname;
        this.LectureIntials = LectureIntials;
    }

    public String getLectureName() {
        return LectureName;
    }

    public void setLectureName(String LectureName) {
        this.LectureName = LectureName;
    }

    public String getLectureSurname() {
        return LectureSurname;
    }

    public void setLectureSurname(String LectureSurname) {
        this.LectureSurname = LectureSurname;
    }

    public String getLectureIntials() {
        return LectureIntials;
    }

    public void setLectureIntials(String LectureIntials) {
        this.LectureIntials = LectureIntials;
    }

    public int getLectureID() {
        return LectureID;
    }

    public void setLectureID(int LectureID) {
        this.LectureID = LectureID;
    }
    

    @Override
    public String toString() {
        return LectureName +" "+ LectureSurname +" "+ LectureIntials ;
    }
}
