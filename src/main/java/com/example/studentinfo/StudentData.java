package com.example.studentinfo;

import java.sql.*;
import java.util.ArrayList;

public class StudentData {
    private Connection conn;

    public StudentData(){
        String url = "jdbc:postgresql://localhost:5434/university";
        String username = "postgres";
        String password = "silvi";

        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Database is successfully connected...");
        } catch(SQLException e){
            System.out.println(e.toString());
        }
    }
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<Student>();

        try {

            String sql = "SELECT * FROM STUDENT";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String dept_name = resultSet.getString("dept_name");
                int tot_cred = resultSet.getInt("tot_cred");
                Student student = new Student(id, name, dept_name, tot_cred);
                students.add(student);
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return students;
    }

    public int insertStudent(String uname, String udept,int ucred) {
        int newid = 0;
        try{
            String sql = "INSERT INTO student(name, dept_name,tot_cred)"+
                    " VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, uname);
            preparedStatement.setString(2,udept);
            preparedStatement.setInt(3,ucred);
            int affectedRows=preparedStatement.executeUpdate();
            if(affectedRows==0){
                throw new SQLException("Creating task failed");
            }
            try(ResultSet generatedKeys=preparedStatement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    newid=generatedKeys.getInt(1);
                }
            }
            System.out.println("New record is added with id: " + newid);

        }catch(SQLException e){
            System.out.println(e.toString());
        }
        return newid;
    }
    public void deleteStudent(int id){
        try{
            String sql="DELETE FROM student WHERE id=?";
            PreparedStatement preparedStatement= this.conn.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            int affectedRows = preparedStatement.executeUpdate();
            if( affectedRows == 0){
                throw new SQLException("Deleting task failed.");
            }

            System.out.println("Deleted rows: " + affectedRows);
        }catch (SQLException e){
            System.out.println(e.toString());
        }
    }
    public void updateStudent(int id, String uname, String udept,int ucred ){

        try{
            String sql = "UPDATE student SET name = ?,dept_name=?,tot_cred=?WHERE id=?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, uname);
            preparedStatement.setString(2, udept);
            preparedStatement.setInt(3,ucred);
            preparedStatement.setInt(4,id);

            int affectedRows = preparedStatement.executeUpdate();

            if( affectedRows == 0){
                throw new SQLException("Updating task failed.");
            }

            System.out.println("Update is success.");

        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
    }
    public Student getStudent(String id){
        Student student =null;
        try{
            String sql= "SELECT * FROM STUDENT WHERE id=?";
            PreparedStatement preparedStatement=this.conn.prepareStatement(sql);
            preparedStatement.setString(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String dept_name = resultSet.getString("dept_name");
                int tot_cred = resultSet.getInt("tot_cred");
                student = new Student(id, name, dept_name, tot_cred);
            }

        }catch (SQLException e){
            System.out.println(e.toString());
        }
        return student;
    }

}
