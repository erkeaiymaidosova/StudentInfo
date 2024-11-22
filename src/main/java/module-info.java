module com.example.studentinfo {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.studentinfo to javafx.fxml;
    exports com.example.studentinfo;
}