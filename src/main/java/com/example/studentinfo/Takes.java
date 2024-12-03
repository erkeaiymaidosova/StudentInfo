package com.example.studentinfo;

public class Takes {
    private String id;
    private String course_id;
    private String sec_id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getSec_id() {
        return sec_id;
    }

    public void setSec_id(String sec_id) {
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

    public Takes(String id, String course_id, String sec_id, String semester, int year, String grade) {
        this.id = id;
        this.course_id = course_id;
        this.sec_id = sec_id;
        this.semester = semester;
        this.year = year;
        this.grade = grade;
    }


}
