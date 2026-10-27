module java.edu.gestionacademicaorm {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.postgresql.jdbc;

    opens java.edu.gestionacademicaorm to javafx.fxml;
    // Hibernate accede a Estudiante por reflexion.
    opens java.edu.gestionacademicaorm.model to org.hibernate.orm.core;
    exports java.edu.gestionacademicaorm;
}