module java.edu.gestionacademicaorm {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;


    opens java.edu.gestionacademicaorm to javafx.fxml;
    exports java.edu.gestionacademicaorm;
}