module java.edu.gestionacademicaorm {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.postgresql.jdbc;
    requires java.sql;
    requires java.naming;

    // FXMLLoader instancia el controlador por reflexion: debe abrirse SU paquete
    opens java.edu.gestionacademicaorm.controller to javafx.fxml;
    // Hibernate accede a Estudiante por reflexion.
    opens java.edu.gestionacademicaorm.model to org.hibernate.orm.core;
    exports java.edu.gestionacademicaorm;
}
