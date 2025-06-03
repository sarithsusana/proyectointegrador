package controller;

import data.SancionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.Sancion;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class GestionSancionesController {

    @FXML private TextField txtIdSancion;
    @FXML private TextField txtMotivo;
    @FXML private DatePicker dateFechaInicio;
    @FXML private DatePicker dateFechaFin;
    @FXML private TextField txtIdUsuario;

    @FXML private Button btnAgregar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    @FXML private TableView<Sancion> tablaSanciones;
    @FXML private TableColumn<Sancion, Number> colIdSancion;
    @FXML private TableColumn<Sancion, String> colMotivo;
    @FXML private TableColumn<Sancion, LocalDate> colFechaInicio;
    @FXML private TableColumn<Sancion, LocalDate> colFechaFin;
    @FXML private TableColumn<Sancion, String> colIdUsuario;

    private SancionDAO sancionDAO;
    private ObservableList<Sancion> listaSanciones;

    @FXML
    public void initialize() {
        sancionDAO = new SancionDAO();

        colIdSancion.setCellValueFactory(cell -> cell.getValue().idSancionProperty());
        colMotivo.setCellValueFactory(cell -> cell.getValue().motivoProperty());
        colFechaInicio.setCellValueFactory(cell -> cell.getValue().fechaInicioProperty());
        colFechaFin.setCellValueFactory(cell -> cell.getValue().fechaFinProperty());
        colIdUsuario.setCellValueFactory(cell -> cell.getValue().idUsuarioProperty());

        cargarSanciones();

        tablaSanciones.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, nuevo) -> {
            if (nuevo != null) {
                llenarFormulario(nuevo);
            }
        });
    }

    private void cargarSanciones() {
        try {
            List<Sancion> sanciones = sancionDAO.obtenerTodos();
            listaSanciones = FXCollections.observableArrayList(sanciones);
            tablaSanciones.setItems(listaSanciones);
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al cargar sanciones: " + e.getMessage());
        }
    }

    private void llenarFormulario(Sancion sancion) {
        txtIdSancion.setText(String.valueOf(sancion.getIdSancion()));
        txtMotivo.setText(sancion.getMotivo());
        dateFechaInicio.setValue(sancion.getFechaInicio());
        dateFechaFin.setValue(sancion.getFechaFin());
        txtIdUsuario.setText(sancion.getIdUsuario());

        txtIdSancion.setDisable(true);
    }

    @FXML
    private void limpiarFormulario() {
        txtIdSancion.clear();
        txtMotivo.clear();
        dateFechaInicio.setValue(null);
        dateFechaFin.setValue(null);
        txtIdUsuario.clear();

        txtIdSancion.setDisable(false);
        tablaSanciones.getSelectionModel().clearSelection();
    }

    @FXML
    private void agregarSancion() {
        if (!validarFormulario()) return;

        try {
            Sancion nueva = new Sancion(
                Integer.parseInt(txtIdSancion.getText()),
                txtMotivo.getText(),
                dateFechaInicio.getValue(),
                dateFechaFin.getValue(),
                txtIdUsuario.getText()
            );

            boolean exito = sancionDAO.insertar(nueva);
            if (exito) {
                mostrarAlerta("Éxito", "Sanción agregada correctamente");
                cargarSanciones();
                limpiarFormulario();
            } else {
                mostrarAlerta("Error", "No se pudo agregar la sanción");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar sanción: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarSancion() {
        if (!validarFormulario()) return;

        try {
            Sancion modificado = new Sancion(
                Integer.parseInt(txtIdSancion.getText()),
                txtMotivo.getText(),
                dateFechaInicio.getValue(),
                dateFechaFin.getValue(),
                txtIdUsuario.getText()
            );

            boolean exito = sancionDAO.actualizar(modificado);
            if (exito) {
                mostrarAlerta("Éxito", "Sanción actualizada correctamente");
                cargarSanciones();
                limpiarFormulario();
            } else {
                mostrarAlerta("Error", "No se pudo actualizar la sanción");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar sanción: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarSancion() {
        Sancion seleccionado = tablaSanciones.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Advertencia", "Seleccione una sanción para eliminar");
            return;
        }

        try {
            boolean exito = sancionDAO.eliminar(seleccionado.getIdSancion());
            if (exito) {
                mostrarAlerta("Éxito", "Sanción eliminada correctamente");
                cargarSanciones();
                limpiarFormulario();
            } else {
                mostrarAlerta("Error", "No se pudo eliminar la sanción");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al eliminar sanción: " + e.getMessage());
        }
    }

    private boolean validarFormulario() {
        if (txtIdSancion.getText().isEmpty() ||
            txtMotivo.getText().isEmpty() ||
            dateFechaInicio.getValue() == null ||
            dateFechaFin.getValue() == null ||
            txtIdUsuario.getText().isEmpty()) {
            mostrarAlerta("Validación", "Todos los campos son obligatorios");
            return false;
        }

        try {
            Integer.parseInt(txtIdSancion.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Validación", "ID Sanción debe ser un número válido");
            return false;
        }

        if (dateFechaFin.getValue().isBefore(dateFechaInicio.getValue())) {
            mostrarAlerta("Validación", "Fecha Fin no puede ser anterior a Fecha Inicio");
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
