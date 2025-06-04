package controller;

import data.EquipoDAO;
import data.PrestamoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.Equipo;
import modelo.Prestamo;
import modelo.Usuario;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class GestionEquiposController {

    @FXML private TextField txtBuscarEquipo;
    @FXML private Button btnBuscar;
    @FXML private Button btnAgregar;
    @FXML private TableView<Equipo> tablaEquipos;
    @FXML private TableColumn<Equipo, Number> colIdEquipo;
    @FXML private TableColumn<Equipo, String> colTipo;
    @FXML private TableColumn<Equipo, String> colMarca;
    @FXML private TableColumn<Equipo, String> colEstado;

    private EquipoDAO equipoDAO;
    private PrestamoDAO prestamoDAO;

    private ObservableList<Equipo> listaEquipos;

    private String identificacionUsuarioActual;
    private String idAdministradorActual;

    // Este método se utiliza para asignar el usuario o administrador logueado
    public void setUsuario(Usuario usuario) {
        if (usuario != null) {
            if (usuario.getTipoUsuario().equals("Administrador")) {
                idAdministradorActual = usuario.getIdentificacion();
            } else {
                identificacionUsuarioActual = usuario.getIdentificacion();
            }
        }
    }

    @FXML
    public void initialize() {
        equipoDAO = new EquipoDAO();
        prestamoDAO = new PrestamoDAO();

        colIdEquipo.setCellValueFactory(cell -> cell.getValue().idEquipoProperty());
        colTipo.setCellValueFactory(cell -> cell.getValue().tipoProperty());
        colMarca.setCellValueFactory(cell -> cell.getValue().marcaProperty());
        colEstado.setCellValueFactory(cell -> cell.getValue().estadoProperty());

        cargarEquipos();
    }

    // Cargar los equipos desde la base de datos
    private void cargarEquipos() {
        try {
            List<Equipo> equipos = equipoDAO.obtenerTodos();
            listaEquipos = FXCollections.observableArrayList(equipos);
            tablaEquipos.setItems(listaEquipos);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los equipos: " + e.getMessage());
        }
    }

    // Buscar un equipo específico en la tabla
    @FXML
    private void buscarEquipo() {
        String criterio = txtBuscarEquipo.getText().trim();
        if (criterio.isEmpty()) {
            cargarEquipos();
            return;
        }

        ObservableList<Equipo> resultado = FXCollections.observableArrayList();

        for (Equipo equipo : listaEquipos) {
            if (equipo.getTipo().toLowerCase().contains(criterio.toLowerCase()) ||
                equipo.getMarca().toLowerCase().contains(criterio.toLowerCase())) {
                resultado.add(equipo);
            }
        }

        tablaEquipos.setItems(resultado);
    }

    // Método para agregar un nuevo equipo (esto puede ser modificado para ajustarse a tu lógica de agregar equipo)
    @FXML
    private void agregarEquipo() {
        // Aquí implementamos lo que necesites para agregar un nuevo equipo
    }

    // Mostrar una alerta con mensaje personalizado
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método para prestar un equipo
    @FXML
    private void prestarEquipo() {
        // Verificamos si hay un usuario logueado
        if (identificacionUsuarioActual == null && idAdministradorActual == null) {
            mostrarAlerta("Error", "Debe estar logueado para realizar un préstamo.");
            return;
        }

        // Seleccionamos el equipo que se va a prestar
        Equipo equipoSeleccionado = tablaEquipos.getSelectionModel().getSelectedItem();
        if (equipoSeleccionado == null) {
            mostrarAlerta("Advertencia", "Debe seleccionar un equipo para prestar.");
            return;
        }

        // Fecha y hora de inicio y fin del préstamo
        LocalDateTime fechaHoraInicio = LocalDateTime.now(); // Asumimos que se empieza ahora, ajusta según lo necesario
        LocalDateTime fechaHoraFin = fechaHoraInicio.plusHours(1); // Ejemplo, el préstamo dura 1 hora, ajusta según lo necesario

        // Si el equipo está disponible, procesamos el préstamo
        Prestamo nuevoPrestamo = new Prestamo(
                0, fechaHoraInicio, fechaHoraFin, "Activo",
                identificacionUsuarioActual != null ? identificacionUsuarioActual : null,
                0, equipoSeleccionado.getIdEquipo(), idAdministradorActual
        );

        try {
            boolean exito = prestamoDAO.insertar(nuevoPrestamo);
            if (exito) {
                mostrarAlerta("Éxito", "Equipo prestado correctamente.");
            } else {
                mostrarAlerta("Error", "No se pudo registrar el préstamo.");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al guardar préstamo: " + e.getMessage());
        }
    }
}