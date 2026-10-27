package java.edu.gestionacademicaorm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/java/edu/gestionacademicaorm/estudiante-view.fxml"));

        stage.setTitle("Gestion Academica - Hibernate ORM");
        stage.setScene(new Scene(loader.load(), 820, 580));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
