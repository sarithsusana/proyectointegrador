package controller;

import data.EvaluacionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.Evaluacion;

import java.sql.SQLException;
import java.util.List;

public class GestionEvaluacionesController {

    @FXML private TextField txtIdEvaluacion;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtCalificacion;
    @FXML private TextField txtIdUsuario;

    @FXML private Button btnAgregar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    @FXML private TableView<Evaluacion> tablaEvaluaciones;
    @FXML private TableColumn<Evaluacion, Number> colIdEvaluacion;
    @FXML private TableColumn<Evaluacion, String> colDescripcion;
    @FXML private TableColumn<Evaluacion, Number> colCalificacion;
    @FXML private TableColumn<Evaluacion, String> colIdUsuario;

    private EvaluacionDAO evaluacionDAO;
    private ObservableList<Evaluacion> listaEvaluaciones;

    @FXML
    public void initialize() {
        evaluacionDAO = new EvaluacionDAO();

        colIdEvaluacion.setCellValueFactory(cell -> cell.getValue().idEvaluacionProperty());
        colDescripcion.setCellValueFactory(cell -> cell.getValue().descripcionProperty());
        colCalificacion.setCellValueFactory(cell -> cell.getValue().calificacionProperty());
        colIdUsuario.setCellValueFactory(cell -> cell.getValue().idUsuarioProperty());

        cargarEvaluaciones();

        tablaEvaluaciones.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, nuevo) -> {
            if (nuevo != null) {
                llenarFormulario(nuevo);
            }
        });
    }

    private void cargarEvaluaciones() {
        try {
            List<Evaluacion> evaluaciones = evaluacionDAO.obtenerTodos();
            listaEvaluaciones = FXCollections.observableArrayList(evaluaciones);
            tablaEvaluaciones.setItems(listaEvaluaciones);
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al cargar evaluaciones: " + e.getMessage());
        }
    }

    private void llenarFormulario(Evaluacion ev) {
        txtIdEvaluacion.setText(String.valueOf(ev.getIdEvaluacion()));
        txtDescripcion.setText(ev.getDescripcion());
        txtCalificacion.setText(String.valueOf(ev.getCalificacion()));
        txtIdUsuario.setText(ev.getIdUsuario());

        txtIdEvaluacion.setDisable(true);
    }

    @FXML
    private void limpiarFormulario() {
        txtIdEvaluacion.clear();
        txtDescripcion.clear();
        txtCalificacion.clear();
        txtIdUsuario.clear();

        txtIdEvaluacion.setDisable(false);
        tablaEvaluaciones.getSelectionModel().clearSelection();
    }

    @FXML
    private void agregarEvaluacion() {
        if (!validarFormulario()) return;

        try {
            Evaluacion nueva = new Evaluacion(
                Integer.parseInt(txtIdEvaluacion.getText()),
                txtDescripcion.getText(),
                Integer.parseInt(txtCalificacion.getText()),
                txtIdUsuario.getText()
            );

            boolean exito = evaluacionDAO.insertar(nueva);
            if (exito) {
                mostrarAlerta("Éxito", "Evaluación agregada correctamente");
                cargarEvaluaciones();
                limpiarFormulario();
            } else {
                mostrarAlerta("Error", "No se pudo agregar la evaluación");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar evaluación: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarEvaluacion() {
        if (!validarFormulario()) return;

        try {
            Evaluacion modificado = new Evaluacion(
                Integer.parseInt(txtIdEvaluacion.getText()),
                txtDescripcion.getText(),
                Integer.parseInt(txtCalificacion.getText()),
                txtIdUsuario.getText()
            );

            boolean exito = evaluacionDAO.actualizar(modificado);
            if (exito) {
                mostrarAlerta("Éxito", "Evaluación actualizada correctamente");
                cargarEvaluaciones();
                limpiarFormulario();
            } else {
                mostrarAlerta("Error", "No se pudo actualizar la evaluación");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar evaluación: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarEvaluacion() {
        Evaluacion seleccionado = tablaEvaluaciones.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Advertencia", "Seleccione una evaluación para eliminar");
            return;
        }

        try {
            boolean exito = evaluacionDAO.eliminar(seleccionado.getIdEvaluacion());
            if (exito) {
                mostrarAlerta("Éxito", "Evaluación eliminada correctamente");
                cargarEvaluaciones();
                limpiarFormulario();
            } else {
                mostrarAlerta("Error", "No se pudo eliminar la evaluación");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al eliminar evaluación: " + e.getMessage());
        }
    }

    private boolean validarFormulario() {
        if (txtIdEvaluacion.getText().isEmpty() ||
            txtDescripcion.getText().isEmpty() ||
            txtCalificacion.getText().isEmpty() ||
            txtIdUsuario.getText().isEmpty()) {
            mostrarAlerta("Validación", "Todos los campos son obligatorios");
            return false;
        }

        try {
            int calif = Integer.parseInt(txtCalificacion.getText());
            if (calif < 1 || calif > 5) {
                mostrarAlerta("Validación", "La calificación debe estar entre 1 y 5");
                return false;
            }
            Integer.parseInt(txtIdEvaluacion.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Validación", "Ingrese valores numéricos válidos");
            return false;
        }

        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
