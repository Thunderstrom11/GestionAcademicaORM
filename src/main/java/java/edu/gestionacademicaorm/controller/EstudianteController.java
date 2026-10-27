package java.edu.gestionacademicaorm.controller;

import java.edu.gestionacademicaorm.model.Estudiante;
import java.edu.gestionacademicaorm.repository.EstudianteRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class EstudianteController {

    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtCorreo;
    @FXML private ComboBox<String> cmbCarrera;
    @FXML private Button btnGuardar;
    @FXML private Button btnConsultar;
    @FXML private Button btnLimpiar;
    @FXML private Label lblEstado;

    @FXML private TableView<Estudiante> tablaEstudiantes;
    @FXML private TableColumn<Estudiante, Number> colId;
    @FXML private TableColumn<Estudiante, String> colNombres;
    @FXML private TableColumn<Estudiante, String> colApellidos;
    @FXML private TableColumn<Estudiante, String> colCorreo;
    @FXML private TableColumn<Estudiante, String> colCarrera;

    private final EstudianteRepository repositorio = new EstudianteRepository();
    private final ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList();

    private static final String[] CARRERAS = {
            "Ingenieria de Sistemas de Informacion",
            "Ingenieria Industrial",
            "Administracion de Empresas",
            "Contaduria Publica",
            "Derecho"
    };

    @FXML
    public void initialize() {
        cmbCarrera.setItems(FXCollections.observableArrayList(CARRERAS));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colCarrera.setCellValueFactory(new PropertyValueFactory<>("carrera"));

        tablaEstudiantes.setItems(estudiantes);

        consultar();
    }

    @FXML
    private void guardar() {
        try {
            Estudiante estudiante = obtenerEstudianteFormulario();

            if (repositorio.existeCorreo(estudiante.getCorreo())) {
                mostrarAlerta(Alert.AlertType.WARNING, "Correo duplicado",
                        "Ya existe un estudiante registrado con el correo "
                                + estudiante.getCorreo());
                return;
            }

            repositorio.guardar(estudiante);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro exitoso",
                    "El estudiante " + estudiante.getNombres() + " "
                            + estudiante.getApellidos()
                            + " fue guardado con el ID " + estudiante.getId());

            limpiar();
            consultar();

        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos invalidos", e.getMessage());

        } catch (RuntimeException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de base de datos",
                    "No fue posible guardar el estudiante.\n" + causaRaiz(e));
        }
    }

    @FXML
    private void consultar() {
        try {
            estudiantes.setAll(repositorio.listar());
            lblEstado.setText(estudiantes.size() + " estudiante(s) cargado(s).");
        } catch (RuntimeException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de consulta",
                    "No fue posible leer los estudiantes.\n" + causaRaiz(e));
        }
    }

    @FXML
    private void limpiar() {
        txtNombres.clear();
        txtApellidos.clear();
        txtCorreo.clear();
        cmbCarrera.getSelectionModel().clearSelection();
        lblEstado.setText("");
        txtNombres.requestFocus();
    }

    /** Valida y construye el objeto. Lanza IllegalArgumentException con el mensaje de usuario. */
    private Estudiante obtenerEstudianteFormulario() {
        String nombres = txtNombres.getText() == null ? "" : txtNombres.getText().trim();
        String apellidos = txtApellidos.getText() == null ? "" : txtApellidos.getText().trim();
        String correo = txtCorreo.getText() == null ? "" : txtCorreo.getText().trim();
        String carrera = cmbCarrera.getValue();

        if (nombres.isEmpty())   throw new IllegalArgumentException("Los nombres son obligatorios.");
        if (apellidos.isEmpty()) throw new IllegalArgumentException("Los apellidos son obligatorios.");
        if (correo.isEmpty())    throw new IllegalArgumentException("El correo es obligatorio.");
        if (carrera == null)     throw new IllegalArgumentException("Debe seleccionar una carrera.");

        if (!correo.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")) {
            throw new IllegalArgumentException("El correo no tiene un formato valido.");
        }
        if (nombres.length() > 100 || apellidos.length() > 100) {
            throw new IllegalArgumentException("Nombres y apellidos admiten maximo 100 caracteres.");
        }
        if (correo.length() > 150) {
            throw new IllegalArgumentException("El correo admite maximo 150 caracteres.");
        }

        return new Estudiante(nombres, apellidos, correo, carrera);
    }

    /** Baja por la cadena de causas hasta el error real de PostgreSQL. */
    private String causaRaiz(Throwable e) {
        Throwable causa = e;
        while (causa.getCause() != null) causa = causa.getCause();
        return causa.getMessage() == null ? e.toString() : causa.getMessage();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
