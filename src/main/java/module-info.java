module java.edu.gestionacademicaorm {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;


    opens java.edu.gestionacademicaorm to javafx.fxml;
    exports java.edu.gestionacademicaorm;
}