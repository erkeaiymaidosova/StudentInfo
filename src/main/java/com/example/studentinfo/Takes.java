package com.example.studentinfo;

public class Takes {
    private int id;
    private int course_id;
    private int sec_id;
    private String semester;
    private int year;
    private String grade;

    @Override
    public String toString() {
        return "Takes{" +
                "id=" + id +
                ", course_id=" + course_id +
                ", sec_id=" + sec_id +
                ", semester='" + semester + '\'' +
                ", year=" + year +
                ", grade='" + grade + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getSec_id() {
        return sec_id;
    }

    public void setSec_id(int sec_id) {
        this.sec_id = sec_id;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Takes(int id, int course_id, int sec_id, String semester, int year, String grade) {
        this.id = id;
        this.course_id = course_id;
        this.sec_id = sec_id;
        this.semester = semester;
        this.year = year;
        this.grade = grade;
    }


}
