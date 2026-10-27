module java.edu.gestionacademicaorm {
    requires javafx.controls;
    requires javafx.fxml;


    opens java.edu.gestionacademicaorm to javafx.fxml;
    exports java.edu.gestionacademicaorm;
}