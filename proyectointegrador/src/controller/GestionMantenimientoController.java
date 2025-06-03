package controller;

import data.MantenimientoDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.Mantenimiento;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class GestionMantenimientoController {

    @FXML private TextField txtIdMantenimiento;
    @FXML private TextField txtIdEquipo;
    @FXML private ComboBox<String> comboEstado;
    @FXML private TextField txtDescripcion;
    @FXML private DatePicker dateFechaInicio;
    @FXML private DatePicker dateFechaFin;

    @FXML private Button btnAgregar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    @FXML private TableView<Mantenimiento> tablaMantenimientos;
    @FXML private TableColumn<Mantenimiento, Number> colIdMantenimiento;
    @FXML private TableColumn<Mantenimiento, Number> colIdEquipo;
    @FXML private TableColumn<Mantenimiento, String> colEstado;
    @FXML private TableColumn<Mantenimiento, String> colDescripcion;
    @FXML private TableColumn<Mantenimiento, LocalDate> colFechaInicio;
    @FXML private TableColumn<Mantenimiento, LocalDate> colFechaFin;

    private MantenimientoDAO mantenimientoDAO;
    private ObservableList<Mantenimiento> listaMantenimientos;

    @FXML
    public void initialize() {
        mantenimientoDAO = new MantenimientoDAO();

        colIdMantenimiento.setCellValueFactory(cell -> 
            new ReadOnlyObjectWrapper<>(cell.getValue().getIdMantenimiento())
        );

        colIdEquipo.setCellValueFactory(cell -> 
            new ReadOnlyObjectWrapper<>(cell.getValue().getIdEquipo())
        );

        colEstado.setCellValueFactory(cell -> cell.getValue().estadoProperty());
        colDescripcion.setCellValueFactory(cell -> cell.getValue().descripcionProperty());
        colFechaInicio.setCellValueFactory(cell -> cell.getValue().fechaInicioProperty());
        colFechaFin.setCellValueFactory(cell -> cell.getValue().fechaFinProperty());

        comboEstado.setItems(FXCollections.observableArrayList(
            "Disponible",
            "En mantenimiento",
            "Reparado",
            "Dañado"
        ));

        cargarMantenimientos();

        tablaMantenimientos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                llenarFormulario(newSelection);
            }
        });
    }

    private void cargarMantenimientos() {
        try {
            List<Mantenimiento> lista = mantenimientoDAO.obtenerTodos();
            listaMantenimientos = FXCollections.observableArrayList(lista);
            tablaMantenimientos.setItems(listaMantenimientos);
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al cargar mantenimientos: " + e.getMessage());
        }
    }

    private void llenarFormulario(Mantenimiento m) {
        txtIdMantenimiento.setText(String.valueOf(m.getIdMantenimiento()));
        txtIdEquipo.setText(String.valueOf(m.getIdEquipo()));
        comboEstado.setValue(m.getEstado());
        txtDescripcion.setText(m.getDescripcion());
        dateFechaInicio.setValue(m.getFechaInicio());
        dateFechaFin.setValue(m.getFechaFin());

        txtIdMantenimiento.setDisable(true);
    }

    @FXML
    private void limpiarFormulario() {
        txtIdMantenimiento.clear();
        txtIdEquipo.clear();
        comboEstado.getSelectionModel().clearSelection();
        txtDescripcion.clear();
        dateFechaInicio.setValue(null);
        dateFechaFin.setValue(null);

        txtIdMantenimiento.setDisable(false);
        tablaMantenimientos.getSelectionModel().clearSelection();
    }

    @FXML
    private void agregarMantenimiento() {
        if (!validarFormulario()) return;

        try {
            Mantenimiento m = new Mantenimiento(
                Integer.parseInt(txtIdMantenimiento.getText()),
                Integer.parseInt(txtIdEquipo.getText()),
                txtDescripcion.getText(),
                dateFechaInicio.getValue(),
                dateFechaFin.getValue(),
                comboEstado.getValue()
            );

            boolean exito = mantenimientoDAO.insertar(m);
            if (exito) {
                mostrarAlerta("Éxito", "Mantenimiento agregado correctamente");
                cargarMantenimientos();
                limpiarFormulario();
            } else {
                mostrarAlerta("Error", "No se pudo agregar el mantenimiento");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar mantenimiento: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarMantenimiento() {
        if (!validarFormulario()) return;

        try {
            Mantenimiento m = new Mantenimiento(
                Integer.parseInt(txtIdMantenimiento.getText()),
                Integer.parseInt(txtIdEquipo.getText()),
                txtDescripcion.getText(),
                dateFechaInicio.getValue(),
                dateFechaFin.getValue(),
                comboEstado.getValue()
            );

            boolean exito = mantenimientoDAO.actualizar(m);
            if (exito) {
                mostrarAlerta("Éxito", "Mantenimiento actualizado correctamente");
                cargarMantenimientos();
                limpiarFormulario();
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el mantenimiento");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar mantenimiento: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarMantenimiento() {
        Mantenimiento seleccionado = tablaMantenimientos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Advertencia", "Seleccione un mantenimiento para eliminar");
            return;
        }

        try {
            boolean exito = mantenimientoDAO.eliminar(seleccionado.getIdMantenimiento());
            if (exito) {
                mostrarAlerta("Éxito", "Mantenimiento eliminado correctamente");
                cargarMantenimientos();
                limpiarFormulario();
            } else {
                mostrarAlerta("Error", "No se pudo eliminar el mantenimiento");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al eliminar mantenimiento: " + e.getMessage());
        }
    }

    private boolean validarFormulario() {
        if (txtIdMantenimiento.getText().isEmpty() ||
            txtIdEquipo.getText().isEmpty() ||
            comboEstado.getValue() == null ||
            txtDescripcion.getText().isEmpty() ||
            dateFechaInicio.getValue() == null ||
            dateFechaFin.getValue() == null) {

            mostrarAlerta("Validación", "Todos los campos son obligatorios");
            return false;
        }

        try {
            Integer.parseInt(txtIdMantenimiento.getText());
            Integer.parseInt(txtIdEquipo.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Validación", "ID de mantenimiento y equipo deben ser numéricos");
            return false;
        }

        if (dateFechaFin.getValue().isBefore(dateFechaInicio.getValue())) {
            mostrarAlerta("Validación", "La fecha fin no puede ser anterior a la fecha inicio");
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

