package com.example.studentinfo;

import java.sql.*;
import java.util.ArrayList;

public class TakesData {
    private Connection conn;

    public TakesData() {
        String url = "jdbc:postgresql://localhost:5432/university";
        String username = "postgres";
        String password = "132569313";

        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Database is successfully connected...");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

    }

    public Connection getConn() {
        return conn;
    }
    public ArrayList<Takes> getAllTakes() {
        ArrayList<Takes> takes = new ArrayList<Takes>();
        try {
            String sql = "SELECT * FROM TAKES";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("Id");
                String courseId = resultSet.getString("course_id");
                String secId = resultSet.getString("sec_id");
                String semester = resultSet.getString("semester");
                int year = resultSet.getInt("year");
                String grade = resultSet.getString("grade");
                Takes take = new Takes(id, courseId, secId, semester, year, grade);
                takes.add(take);

            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return takes;
    }
    public int insertTakes(int courseId, int secId, String semester, int year, String grade) {
        int newId = 0;
        try {
            String sql = "INSERT INTO takes(course_id, sec_id, semester, year, grade) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, courseId);
            preparedStatement.setInt(2, secId);
            preparedStatement.setString(3, semester);
            preparedStatement.setInt(4, year);
            preparedStatement.setString(5, grade);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating record failed, no rows affected.");
            }
            if (grade == null || grade.isEmpty()) {
                preparedStatement.setNull(5, Types.VARCHAR);
            } else {
                preparedStatement.setString(5, grade);
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return newId;
    }
    public void updateTakes(int id, int courseId, int secId, String semester, int year, String grade) {
        try {
            String sql = "UPDATE takes SET course_id = ?, sec_id = ?, semester = ?, year = ?, grade = ? WHERE id = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            preparedStatement.setInt(2, secId);
            preparedStatement.setString(3, semester);
            preparedStatement.setInt(4, year);
            preparedStatement.setString(5, grade);
            preparedStatement.setInt(6, id);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating record failed, no rows affected.");
            }

            System.out.println("Record updated successfully.");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
    public void deleteTakes(int id) {
        try {
            String sql = "DELETE FROM takes WHERE id = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            int affectedRows = preparedStatement.executeUpdate();
            System.out.println("Deleted rows: " + affectedRows);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }


}