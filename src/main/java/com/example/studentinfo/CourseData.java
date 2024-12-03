package com.example.studentinfo;

import java.sql.*;
import java.util.ArrayList;

public class CourseData {
    private Connection conn;

    public CourseData() {
        String url = "jdbc:postgresql://localhost:5432/university";
        String username = "postgres";
        String password = "132569313";

        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }
    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        try {
            String sql = "SELECT * FROM course";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String course_id = resultSet.getString("course_id");
                String title = resultSet.getString("title");
                String dept_name = resultSet.getString("dept_name");
                int credits = resultSet.getInt("credits");

                Course course = new Course(course_id, title, dept_name, credits);
                courses.add(course);
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving courses: " + e.getMessage());
        }
        return courses;
    }
    public int insertCourse(String title, String dept_name, int credits) {
        int newId = 0;
        try {
            String sql = "INSERT INTO course (title, dept_name, credits) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, dept_name);
            preparedStatement.setInt(3, credits);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting course failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newId = generatedKeys.getInt(1);
                }
            }
            System.out.println("New course added with ID: " + newId);
        } catch (SQLException e) {
            System.out.println("Error inserting course: " + e.getMessage());
        }
        return newId;
    }
    public void updateCourse(int course_id, String title, String dept_name, int credits) {
        try {
            String sql = "UPDATE course SET title = ?, dept_name = ?, credits = ? WHERE course_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, dept_name);
            preparedStatement.setInt(3, credits);
            preparedStatement.setInt(4, course_id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating course failed, no rows affected.");
            }
            System.out.println("Course updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating course: " + e.getMessage());
        }
    }


    public void deleteCourse(int course_id) {
        try {
            String sql = "DELETE FROM course WHERE course_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, course_id);

            int affectedRows = preparedStatement.executeUpdate();
            System.out.println("Deleted rows: " + affectedRows);
        } catch (SQLException e) {
            System.out.println("Error deleting course: " + e.getMessage());
        }
    }
    public Course getCourse(String course_id) {
        Course course = null;
        try {
            String sql = "SELECT * FROM course WHERE course_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, course_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String dept_name = resultSet.getString("dept_name");
                int credits = resultSet.getInt("credits");

                course = new Course(course_id, title, dept_name, credits);
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving course: " + e.getMessage());
        }
        return course;
    }


}
