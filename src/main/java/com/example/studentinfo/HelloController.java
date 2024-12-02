package com.example.studentinfo;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HelloController {
    // --- FXML элементы для вкладки Students ---
    @FXML
    private TextField studentIDField, studentNameField, departmentField, totalCreditsField;
    @FXML
    private ListView<String> studentListView;

    // --- FXML элементы для вкладки Courses ---
    @FXML
    private TextField courseIDField, titleField, courseDepartmentField, creditsField;
    @FXML
    private ListView<String> courseListView;

    // --- FXML элементы для вкладки Takes ---
    @FXML
    private TextField enrollStudentIDField, enrollCourseIDField;
    @FXML
    private ListView<String> enrollmentListView;

    // --- Приветственное сообщение ---
    @FXML
    private Label welcomeText;

    // --- Обработчики событий ---
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to the Student Info Management System!");
    }

    // --- Методы для работы со студентами ---
    @FXML
    protected void onAddStudent(ActionEvent event) {
        if (areFieldsEmpty(studentIDField, studentNameField, departmentField, totalCreditsField)) {
            showAlert("Error", "All fields are required to add a student.");
            return;
        }

        String student = formatStudentInfo();
        studentListView.getItems().add(student);
        clearFields(studentIDField, studentNameField, departmentField, totalCreditsField);
    }

    @FXML
    protected void onUpdateStudent(ActionEvent event) {
        String selectedStudent = studentListView.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showAlert("Error", "Please select a student to update.");
            return;
        }

        String updatedStudent = formatStudentInfo();
        studentListView.getItems().set(studentListView.getSelectionModel().getSelectedIndex(), updatedStudent);
        clearFields(studentIDField, studentNameField, departmentField, totalCreditsField);
    }

    @FXML
    protected void onDeleteStudent(ActionEvent event) {
        removeSelectedItem(studentListView, "Please select a student to delete.");
    }

    // --- Методы для работы с курсами ---
    @FXML
    protected void onAddCourse(ActionEvent event) {
        if (areFieldsEmpty(courseIDField, titleField, courseDepartmentField, creditsField)) {
            showAlert("Error", "All fields are required to add a course.");
            return;
        }

        String course = formatCourseInfo();
        courseListView.getItems().add(course);
        clearFields(courseIDField, titleField, courseDepartmentField, creditsField);
    }

    @FXML
    protected void onUpdateCourse(ActionEvent event) {
        String selectedCourse = courseListView.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert("Error", "Please select a course to update.");
            return;
        }

        String updatedCourse = formatCourseInfo();
        courseListView.getItems().set(courseListView.getSelectionModel().getSelectedIndex(), updatedCourse);
        clearFields(courseIDField, titleField, courseDepartmentField, creditsField);
    }

    @FXML
    protected void onDeleteCourse(ActionEvent event) {
        removeSelectedItem(courseListView, "Please select a course to delete.");
    }

    // --- Методы для работы с зачислениями ---
    @FXML
    protected void onEnrollStudent(ActionEvent event) {
        if (areFieldsEmpty(enrollStudentIDField, enrollCourseIDField)) {
            showAlert("Error", "Both Student ID and Course ID are required to enroll.");
            return;
        }

        String enrollment = "Student: " + enrollStudentIDField.getText() + " -> Course: " + enrollCourseIDField.getText();
        enrollmentListView.getItems().add(enrollment);
        clearFields(enrollStudentIDField, enrollCourseIDField);
    }

    @FXML
    protected void onRemoveEnrollment(ActionEvent event) {
        removeSelectedItem(enrollmentListView, "Please select an enrollment to remove.");
    }

    // --- Утилитарные методы ---
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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

    // --- Пустые методы с логикой для кнопок ---
    public void onInst(ActionEvent actionEvent) {
        showAlert("Instruction", "Instructions on how to use the system: Add, Update, and Delete students, courses, and enrollments.");
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
        onRemoveEnrollment(actionEvent);
    }
}
