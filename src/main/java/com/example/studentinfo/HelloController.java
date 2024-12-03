package com.example.studentinfo;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class HelloController {
    @FXML
    private TextField studentIDField, studentNameField, departmentField, totalCreditsField;
    @FXML
    private ListView<String> studentListView;
    @FXML
    private TextField courseIDField, titleField, courseDepartmentField, creditsField;
    @FXML
    private ListView<String> courseListView;
    @FXML
    private TextField enrollStudentIDField, enrollCourseIDField;
    @FXML
    private ListView<String> enrollmentListView;
    private Connection conn;

    @FXML
    private Label welcomeText;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to the Student Info Management System!");
    }

    @FXML
    protected void onAddStudent(ActionEvent event) {
        String selectedStudent = studentListView.getSelectionModel().getSelectedItem();

        int newid = 0;
        TakesData takesData = new TakesData();

        boolean isUpdating = selectedStudent != null;

        try {
            String query;

            if (isUpdating) {
                query = "UPDATE student SET name = ?, dept_name = ?, tot_cred = ? WHERE id = ?";
            } else {
                query = "INSERT INTO student (id, name, dept_name, tot_cred) VALUES (?, ?, ?, ?)";
            }

            PreparedStatement preparedStatement = takesData.getConn().prepareStatement(query);

            if (isUpdating) {
                preparedStatement.setString(1, studentNameField.getText());
                preparedStatement.setString(2, departmentField.getText());
                preparedStatement.setInt(3, Integer.parseInt(totalCreditsField.getText()));
                preparedStatement.setInt(4, Integer.parseInt(studentIDField.getText()));
            } else {
                preparedStatement.setString(1, studentIDField.getText());
                preparedStatement.setString(2, studentNameField.getText());
                preparedStatement.setString(3, departmentField.getText());
                preparedStatement.setInt(4, Integer.parseInt(totalCreditsField.getText()));
            }

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Operation failed");
            }else{
                showAlert1("Success", "Inserted successfully.");
            }

            if (!isUpdating) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newid = generatedKeys.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        String updatedStudent = formatStudentInfo();
        if (isUpdating) {
            int selectedIndex = studentListView.getSelectionModel().getSelectedIndex();
            studentListView.getItems().set(selectedIndex, updatedStudent);
        } else {
            studentListView.getItems().add(updatedStudent);
        }

        clearFields(studentIDField, studentNameField, departmentField, totalCreditsField);
    }

    @FXML
    protected void onUpdateStudent(ActionEvent event) {
        String selectedStudent = studentListView.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showAlert("Error", "Please select a student to update.");
            return;
        }

        if (areFieldsEmpty(studentIDField, studentNameField, departmentField, totalCreditsField)) {
            showAlert("Error", "All fields are required to update the student.");
            return;
        }

        try {
            String query = "UPDATE student SET name = ?, dept_name = ?, tot_cred = ? WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, studentNameField.getText());
                statement.setString(2, departmentField.getText());
                statement.setInt(3, Integer.parseInt(totalCreditsField.getText()));
                statement.setString(4, studentIDField.getText());

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    studentListView.getItems().set(studentListView.getSelectionModel().getSelectedIndex(), formatStudentInfo());
                    clearFields(studentIDField, studentNameField, departmentField, totalCreditsField);
                    showAlert1("Success", "Updated successfully.");
                } else {
                    showAlert("Error", "Failed to update student.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while updating the student: " + e.getMessage());
        }
    }

    @FXML
    protected void onDeleteStudent(ActionEvent event) {
        String selectedStudent = studentListView.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showAlert("Error", "Please select a student to delete.");
            return;
        }

        String studentID = selectedStudent.split(",")[0].split(":")[1].trim();  // Extract the ID from the formatted string
        TakesData takesData=new TakesData();
        try {
            String query = "DELETE FROM student WHERE id = ?";
            try (PreparedStatement statement = takesData.getConn().prepareStatement(query);) {
                statement.setString(1, studentID);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    studentListView.getItems().remove(selectedStudent);
                    clearFields(studentIDField, studentNameField, departmentField, totalCreditsField);
                    showAlert1("Success", "Deleted successfully.");
                } else {
                    showAlert("Error", "Failed to delete student.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while deleting the student: " + e.getMessage());
        }
    }
    private String formatCourseInfo(Course course) {
        return "Course ID: " + course.getCourse_id() +
                ", Title: " + course.getTitle() +
                ", Department: " + course.getDept_name() +
                ", Credits: " + course.getCredits();
    }


    @FXML
    protected void onAddCourse(ActionEvent event) {
        if (areFieldsEmpty(courseIDField, titleField, courseDepartmentField, creditsField)) {
            showAlert("Error", "All fields are required to add a course.");
            return;
        }

        String courseId = courseIDField.getText();
        String title = titleField.getText();
        String dept_name = courseDepartmentField.getText();
        int credits;

        if (!courseId.matches("\\d+")) {
            showAlert("Invalid Input", "Course ID must be a numeric value.");
            return;
        }

        try {
            credits = Integer.parseInt(creditsField.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Credits must be a valid number.");
            return;
        }

        TakesData takesData = new TakesData();

        try {
            String checkQuery = "SELECT 1 FROM course WHERE course_id = ?";
            PreparedStatement checkStmt = takesData.getConn().prepareStatement(checkQuery);
            checkStmt.setString(1, courseId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                showAlert("Error", "Course ID already exists. Please enter a different ID.");
                return;
            }

            String insertQuery = "INSERT INTO course (course_id, title, dept_name, credits) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStmt = takesData.getConn().prepareStatement(insertQuery);
            insertStmt.setString(1, courseId);
            insertStmt.setString(2, title);
            insertStmt.setString(3, dept_name);
            insertStmt.setInt(4, credits);

            int affectedRows = insertStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating course failed, no rows affected.");
            }else{
                showAlert1("Success", "Inserted successfully.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while adding the course: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        Course newCourse = new Course(courseId, title, dept_name, credits);
        courseListView.getItems().add(formatCourseInfo(newCourse));
        clearFields(courseIDField, titleField, courseDepartmentField, creditsField);

    }


    @FXML
    protected void onUpdateCourse(ActionEvent event) {
        String selectedCourse = courseListView.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert("Error", "Please select a course to update.");
            return;
        }

        if (areFieldsEmpty(titleField, courseDepartmentField, creditsField)) {
            showAlert("Error", "All fields are required to update a course.");
            return;
        }

        String title = titleField.getText();
        String dept_name = courseDepartmentField.getText();
        int credits = Integer.parseInt(creditsField.getText());
        String course_id = courseIDField.getText();
        TakesData takesData = new TakesData();
        try {
            String query = "UPDATE course SET title = ?, dept_name = ?, credits = ? WHERE course_id = ?";
            PreparedStatement preparedStatement = takesData.getConn().prepareStatement(query);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, dept_name);
            preparedStatement.setInt(3, credits);
            preparedStatement.setString(4, course_id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating course failed, no rows affected.");
            }else{
                showAlert1("Success", "Updated successfully.");
            }

            Course updatedCourse = new Course(course_id, title, dept_name, credits);
            courseListView.getItems().set(courseListView.getSelectionModel().getSelectedIndex(), formatCourseInfo(updatedCourse));

            clearFields(titleField, courseDepartmentField, creditsField);

        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while updating the course: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid data for the credits and course ID fields.");
            e.printStackTrace();
        }
    }



    @FXML
    protected void onDeleteCourse(ActionEvent event) {
        String selectedCourse = courseListView.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert("Error", "Please select a course to delete.");
            return;
        }

        String course_id = selectedCourse.split(" ")[0].trim();  // Trim any leading/trailing spaces
        TakesData takesData = new TakesData();
        if (takesData.getConn() == null) {
            showAlert("Error", "Database connection failed.");
            return;
        }
        try {
            String checkQuery = "SELECT course_id FROM course WHERE course_id = ?";
            PreparedStatement checkStatement = takesData.getConn().prepareStatement(checkQuery);
            checkStatement.setString(1, course_id);
            ResultSet resultSet = checkStatement.executeQuery();
            if (!resultSet.next()) {
                showAlert("Error", "Course not found.");
                return;
            }

            String query = "DELETE FROM course WHERE course_id = ?";
            PreparedStatement preparedStatement = takesData.getConn().prepareStatement(query);
            preparedStatement.setString(1, course_id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting course failed, no rows affected.");
            } else {
                showAlert1("Success", "Deleted successfully.");
            }

            courseListView.getItems().remove(courseListView.getSelectionModel().getSelectedIndex());
            clearFields(courseIDField);

        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while deleting the course: " + e.getMessage());
            e.printStackTrace();
        }
    }



    @FXML
    protected void onEnrollStudent(ActionEvent event) {
        if (areFieldsEmpty(enrollStudentIDField, enrollCourseIDField)) {
            showAlert("Error", "Both Student ID and Course ID are required to enroll a student.");
            return;
        }

        String studentId = enrollStudentIDField.getText();
        String courseId = enrollCourseIDField.getText();

        if (!studentId.matches("\\d+")) {
            showAlert("Invalid Input", "Student ID must be a numeric value.");
            return;
        }
        if (!courseId.matches("\\d+")) {
            showAlert("Invalid Input", "Course ID must be a numeric value.");
            return;
        }

        String semester = "Fall";
        int year = 2010;
        String secId = "1";
        String grade = "A";

        TakesData takesData = new TakesData();
        try {
            String checkQuery = "SELECT 1 FROM takes WHERE id = ? AND course_id = ?";
            PreparedStatement checkStmt = takesData.getConn().prepareStatement(checkQuery);
            checkStmt.setString(1, studentId);
            checkStmt.setString(2, courseId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                showAlert("Error", "The student is already enrolled in this course.");
                return;
            }

            String insertQuery = "INSERT INTO takes (id, course_id, sec_id, semester, year, grade) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = takesData.getConn().prepareStatement(insertQuery);
            insertStmt.setString(1, studentId);
            insertStmt.setString(2, courseId);
            insertStmt.setString(3, secId);
            insertStmt.setString(4, semester);
            insertStmt.setInt(5, year);
            insertStmt.setString(6, grade);
            int affectedRows = insertStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Enrollment failed, no rows affected.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while enrolling the student: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        Takes takes = new Takes(studentId,courseId,secId, semester,year,grade);
        enrollmentListView.getItems().add(String.valueOf(takes));
        clearFields(enrollStudentIDField, enrollCourseIDField);
        showAlert1("Success", "Student enrolled successfully.");
    }




    @FXML
    protected void onRemoveEnroll(ActionEvent event) {
        String selectedStudent = enrollStudentIDField.getText();
        String selectedCourse = enrollCourseIDField.getText();

        if (selectedStudent.isEmpty() || selectedCourse.isEmpty()) {
            showAlert("Error", "Please provide both Student ID and Course ID.");
            return;
        }
        TakesData takesData = new TakesData();

        String studentId = selectedStudent;
        String courseId = selectedCourse;

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5434/employee_db", "postgres", "silvi")) {
            String sql = "DELETE FROM takes WHERE id = ? AND course_id = ?";
            PreparedStatement preparedStatement = takesData.getConn().prepareStatement(sql);

            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, courseId);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Removing enrollment failed, no rows affected.");
            }

            showAlert1("Success", "Enrollment removed successfully.");
            enrollmentListView.getItems().remove(enrollmentListView.getSelectionModel().getSelectedIndex());
            clearFields(studentIDField, courseIDField);

        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while removing the enrollment: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showAlert1(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private boolean areFieldsEmpty(TextField... fields) {
        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void removeSelectedItem(ListView<String> listView, String errorMessage) {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            listView.getItems().remove(selectedItem);
        }
    }

    private String formatStudentInfo() {
        return "ID: " + studentIDField.getText() + ", Name: " + studentNameField.getText() +
                ", Department: " + departmentField.getText() + ", Credits: " + totalCreditsField.getText();
    }

    private String formatCourseInfo() {
        return "ID: " + courseIDField.getText() + ", Title: " + titleField.getText() +
                ", Department: " + courseDepartmentField.getText() + ", Credits: " + creditsField.getText();
    }

    public void onInst(ActionEvent actionEvent) {
        showAlert1("Instruction", "Instructions on how to use the system: Add, Update, and Delete students, courses, and enrollments.");
    }

    public void StudentSelection(Event event) {
        String selectedStudent = studentListView.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            showAlert("Student Selected", "You selected: " + selectedStudent);
        }
    }

    public void onAddStudents(ActionEvent actionEvent) {
        onAddStudent(actionEvent);
    }

    public void OnUpdate(ActionEvent actionEvent) {
        onUpdateStudent(actionEvent);
    }

    public void onDeleteStudents(ActionEvent actionEvent) {
        onDeleteStudent(actionEvent);
    }

    public void CourseSelection(Event event) {
        String selectedCourse = courseListView.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            showAlert("Course Selected", "You selected: " + selectedCourse);
        }
    }

    public void onAddCourses(ActionEvent actionEvent) {
        onAddCourse(actionEvent);
    }

    public void onUpdateCourses(ActionEvent actionEvent) {
        onUpdateCourse(actionEvent);
    }

    public void onDeleteCourses(ActionEvent actionEvent) {
        onDeleteCourse(actionEvent);
    }

    public void TakesSelection(Event event) {
        String selectedEnrollment = enrollmentListView.getSelectionModel().getSelectedItem();
        if (selectedEnrollment != null) {
            showAlert("Enrollment Selected", "You selected: " + selectedEnrollment);
        }
    }

    public void onEnroll(ActionEvent actionEvent) {
        onEnrollStudent(actionEvent);
    }

    public void onRemove(ActionEvent actionEvent) {
        onRemoveEnroll(actionEvent);
    }
}
