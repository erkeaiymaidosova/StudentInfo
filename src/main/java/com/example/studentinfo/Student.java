package com.example.studentinfo;

public class Student {
    private String id;
    private String name;
    private String dept_name;
    private int tot_cred;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dept_name='" + dept_name + '\'' +
                ", tot_cred=" + tot_cred +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public int getTot_cred() {
        return tot_cred;
    }

    public void setTot_cred(int tot_cred) {
        this.tot_cred = tot_cred;
    }

    public Student(String id, String name, String dept_name, int tot_cred) {
        this.id = id;
        this.name = name;
        this.dept_name = dept_name;
        this.tot_cred = tot_cred;
    }



}
