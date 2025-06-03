package controller;

import data.PrestamoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.Prestamo;
import utils.AlertaUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GestionPrestamosController {

    @FXML private DatePicker datePickerFecha;
    @FXML private ListView<String> listaHorariosOcupados;

    @FXML private TableView<Prestamo> tablaPrestamos;
    @FXML private TableColumn<Prestamo, Number> colId;
    @FXML private TableColumn<Prestamo, LocalDate> colFecha;
    @FXML private TableColumn<Prestamo, String> colHoraInicio;
    @FXML private TableColumn<Prestamo, String> colHoraFin;
    @FXML private TableColumn<Prestamo, String> colEstado;
    @FXML private TableColumn<Prestamo, String> colUsuario;
    @FXML private TableColumn<Prestamo, Number> colSala;
    @FXML private TableColumn<Prestamo, Number> colEquipo;
    @FXML private TableColumn<Prestamo, String> colAdministrador;

    @FXML private TextField txtId;
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraFin;
    @FXML private TextField txtEstado;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtSala;
    @FXML private TextField txtEquipo;
    @FXML private TextField txtAdministrador;

    private PrestamoDAO prestamoDAO;
    private ObservableList<Prestamo> listaPrestamos;

    private final DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("hh:mm a");

    @FXML
    public void initialize() {
        prestamoDAO = new PrestamoDAO();

        colId.setCellValueFactory(cell -> cell.getValue().idPrestamoProperty());
        colFecha.setCellValueFactory(cell -> 
            new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getFechaHoraInicio().toLocalDate()));
        colHoraInicio.setCellValueFactory(cell -> 
            new javafx.beans.property.SimpleStringProperty(cell.getValue().getFechaHoraInicio().toLocalTime().format(formatoHora)));
        colHoraFin.setCellValueFactory(cell -> 
            new javafx.beans.property.SimpleStringProperty(cell.getValue().getFechaHoraFin().toLocalTime().format(formatoHora)));

        colEstado.setCellValueFactory(cell -> cell.getValue().estadoProperty());
        colUsuario.setCellValueFactory(cell -> cell.getValue().identificacionUsuarioProperty());
        colSala.setCellValueFactory(cell -> cell.getValue().idSalaProperty());

        // Manejo null para idEquipo en la tabla:
        colEquipo.setCellValueFactory(cell -> {
            Integer equipo = cell.getValue().getIdEquipo();
            if (equipo == null) {
                return new javafx.beans.property.SimpleIntegerProperty(0);
            } else {
                return new javafx.beans.property.SimpleIntegerProperty(equipo);
            }
        });

        colAdministrador.setCellValueFactory(cell -> cell.getValue().idAdministradorProperty());

        cargarPrestamos();

        datePickerFecha.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                cargarHorariosOcupados(newDate);
            }
        });

        tablaPrestamos.getSelectionModel().selectedItemProperty().addListener((obs, oldPrestamo, nuevoPrestamo) -> {
            if (nuevoPrestamo != null) {
                llenarFormulario(nuevoPrestamo);
            }
        });
    }

    private void cargarPrestamos() {
        try {
            List<Prestamo> prestamos = prestamoDAO.obtenerTodos();
            listaPrestamos = FXCollections.observableArrayList(prestamos);
            tablaPrestamos.setItems(listaPrestamos);
        } catch (SQLException e) {
            AlertaUtil.mostrarError("Error", "Error al cargar préstamos: " + e.getMessage());
        }
    }

    private void cargarHorariosOcupados(LocalDate fecha) {
        try {
            List<Prestamo> prestamosDelDia = prestamoDAO.obtenerPorFecha(fecha);
            ObservableList<String> horarios = FXCollections.observableArrayList();

            for (Prestamo p : prestamosDelDia) {
                horarios.add(p.getFechaHoraInicio().toLocalTime().format(formatoHora) + " - " +
                             p.getFechaHoraFin().toLocalTime().format(formatoHora) +
                             " Sala: " + p.getIdSala() +
                             " Equipo: " + (p.getIdEquipo() == null ? "N/A" : p.getIdEquipo()));
            }

            listaHorariosOcupados.setItems(horarios);

        } catch (SQLException e) {
            AlertaUtil.mostrarError("Error", "No se pudo cargar horarios ocupados: " + e.getMessage());
        }
    }

    private void llenarFormulario(Prestamo p) {
        txtId.setText(String.valueOf(p.getIdPrestamo()));
        datePickerFecha.setValue(p.getFechaHoraInicio().toLocalDate());
        txtHoraInicio.setText(p.getFechaHoraInicio().toLocalTime().format(formatoHora));
        txtHoraFin.setText(p.getFechaHoraFin().toLocalTime().format(formatoHora));
        txtEstado.setText(p.getEstado());
        txtUsuario.setText(p.getIdentificacionUsuario());
        txtSala.setText(String.valueOf(p.getIdSala()));
        txtEquipo.setText(p.getIdEquipo() == null ? "" : String.valueOf(p.getIdEquipo()));
        txtAdministrador.setText(p.getIdAdministrador());
    }

    @FXML
    private void agregarPrestamo() {
        Prestamo nuevoPrestamo = crearPrestamoDesdeFormulario();
        if (nuevoPrestamo == null) return;

        if (!validarDisponibilidad(nuevoPrestamo)) return;

        try {
            if (prestamoDAO.insertar(nuevoPrestamo)) {
                AlertaUtil.mostrarInformacion("Éxito", "Préstamo agregado correctamente");
                cargarPrestamos();
                limpiarFormulario();
            } else {
                AlertaUtil.mostrarError("Error", "No se pudo agregar el préstamo");
            }
        } catch (SQLException e) {
            AlertaUtil.mostrarError("Error", "Error al agregar préstamo: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarPrestamo() {
        Prestamo prestamo = crearPrestamoDesdeFormulario();
        if (prestamo == null) return;

        if (!validarDisponibilidad(prestamo)) return;

        try {
            if (prestamoDAO.actualizar(prestamo)) {
                AlertaUtil.mostrarInformacion("Éxito", "Préstamo actualizado correctamente");
                cargarPrestamos();
                limpiarFormulario();
            } else {
                AlertaUtil.mostrarError("Error", "No se pudo actualizar el préstamo");
            }
        } catch (SQLException e) {
            AlertaUtil.mostrarError("Error", "Error al actualizar préstamo: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarPrestamo() {
        Prestamo prestamo = tablaPrestamos.getSelectionModel().getSelectedItem();
        if (prestamo == null) {
            AlertaUtil.mostrarAdvertencia("Advertencia", "Seleccione un préstamo para eliminar");
            return;
        }

        try {
            if (prestamoDAO.eliminar(prestamo.getIdPrestamo())) {
                AlertaUtil.mostrarInformacion("Éxito", "Préstamo eliminado correctamente");
                cargarPrestamos();
                limpiarFormulario();
            } else {
                AlertaUtil.mostrarError("Error", "No se pudo eliminar el préstamo");
            }
        } catch (SQLException e) {
            AlertaUtil.mostrarError("Error", "Error al eliminar préstamo: " + e.getMessage());
        }
    }

    private Prestamo crearPrestamoDesdeFormulario() {
        try {
            int id = txtId.getText().isEmpty() ? 0 : Integer.parseInt(txtId.getText());
            LocalDate fecha = datePickerFecha.getValue();
            LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText());
            LocalTime horaFin = LocalTime.parse(txtHoraFin.getText());
            String estado = txtEstado.getText();
            String identificacionUsuario = txtUsuario.getText();
            int idSala = Integer.parseInt(txtSala.getText());

            // Para idEquipo permite vacío o nulo:
            Integer idEquipo = null;
            if (txtEquipo.getText() != null && !txtEquipo.getText().trim().isEmpty()) {
                idEquipo = Integer.parseInt(txtEquipo.getText().trim());
            }

            String idAdministrador = txtAdministrador.getText();

            if (fecha == null || estado.isEmpty() || identificacionUsuario.isEmpty() || idAdministrador.isEmpty()) {
                AlertaUtil.mostrarAdvertencia("Validación", "Complete todos los campos obligatorios");
                return null;
            }

            java.time.LocalDateTime fechaHoraInicio = java.time.LocalDateTime.of(fecha, horaInicio);
            java.time.LocalDateTime fechaHoraFin = java.time.LocalDateTime.of(fecha, horaFin);

            return new Prestamo(id, fechaHoraInicio, fechaHoraFin, estado, identificacionUsuario, idSala, idEquipo, idAdministrador);

        } catch (Exception e) {
            AlertaUtil.mostrarError("Error", "Datos inválidos: " + e.getMessage());
            return null;
        }
    }

    private boolean validarDisponibilidad(Prestamo nuevoPrestamo) {
        try {
            List<Prestamo> prestamos = prestamoDAO.obtenerPorFecha(nuevoPrestamo.getFechaHoraInicio().toLocalDate());
            for (Prestamo p : prestamos) {
                if (p.getIdPrestamo() == nuevoPrestamo.getIdPrestamo()) continue;

                if (p.getIdSala() == nuevoPrestamo.getIdSala() || 
                    (p.getIdEquipo() != null && nuevoPrestamo.getIdEquipo() != null && p.getIdEquipo().equals(nuevoPrestamo.getIdEquipo()))) {
                    if (horariosSeSolapan(p.getFechaHoraInicio().toLocalTime(), p.getFechaHoraFin().toLocalTime(),
                            nuevoPrestamo.getFechaHoraInicio().toLocalTime(), nuevoPrestamo.getFechaHoraFin().toLocalTime())) {
                        AlertaUtil.mostrarAdvertencia("Horario ocupado", "La sala o equipo ya está reservado en ese horario.");
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            AlertaUtil.mostrarError("Error", "Error al validar disponibilidad: " + e.getMessage());
            return false;
        }
        return true;
    }

    private boolean horariosSeSolapan(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return inicio1.isBefore(fin2) && inicio2.isBefore(fin1);
    }

    @FXML
    private void limpiarFormulario() {
        txtId.clear();
        datePickerFecha.setValue(null);
        txtHoraInicio.clear();
        txtHoraFin.clear();
        txtEstado.clear();
        txtUsuario.clear();
        txtSala.clear();
        txtEquipo.clear();
        txtAdministrador.clear();
        tablaPrestamos.getSelectionModel().clearSelection();
        listaHorariosOcupados.getItems().clear();
    }
}


