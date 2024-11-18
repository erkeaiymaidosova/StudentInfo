module com.example.studentinfo {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.studentinfo to javafx.fxml;
    exports com.example.studentinfo;
}