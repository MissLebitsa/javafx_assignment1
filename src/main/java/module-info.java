module com.example.pos {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pos to javafx.fxml;
    exports com.example.pos;
}