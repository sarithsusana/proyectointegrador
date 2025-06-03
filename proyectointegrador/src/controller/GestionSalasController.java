package controller;

import data.PrestamoDAO;
import data.UsuarioDAO;
import data.AdministradorDAO;
import data.SalaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.Prestamo;
import modelo.Sala;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class GestionSalasController {

    @FXML private TextField txtBuscarSala;
    @FXML private Button btnBuscar;
    @FXML private Button btnAgregar;
    @FXML private Button btnPrestar;
    @FXML private DatePicker datePickerFecha;
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraFin;
    @FXML private TableView<Sala> tablaSalas;
    @FXML private TableColumn<Sala, Number> colIdSala;
    @FXML private TableColumn<Sala, String> colNombre;
    @FXML private TableColumn<Sala, Number> colCapacidad;
    @FXML private TableColumn<Sala, String> colUbicacion;

    private SalaDAO salaDAO;
    private PrestamoDAO prestamoDAO;
    private UsuarioDAO usuarioDAO;
    private AdministradorDAO administradorDAO;

    private ObservableList<Sala> listaSalas;

    private String identificacionUsuarioActual;
    private String idAdministradorActual;

    private final DateTimeFormatter formatterHora12ConEspacio = DateTimeFormatter.ofPattern("h:mm a").withLocale(Locale.ENGLISH);
    private final DateTimeFormatter formatterHora12SinEspacio = DateTimeFormatter.ofPattern("h:mma").withLocale(Locale.ENGLISH);

    @FXML
    public void initialize() {
        salaDAO = new SalaDAO();
        prestamoDAO = new PrestamoDAO();
        usuarioDAO = new UsuarioDAO();
        administradorDAO = new AdministradorDAO();

        colIdSala.setCellValueFactory(cell -> cell.getValue().idSalaProperty());
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());
        colCapacidad.setCellValueFactory(cell -> cell.getValue().capacidadProperty());
        colUbicacion.setCellValueFactory(cell -> cell.getValue().ubicacionProperty());

        cargarSalas();
    }

    private void cargarSalas() {
        try {
            List<Sala> salas = salaDAO.obtenerTodos();
            listaSalas = FXCollections.observableArrayList(salas);
            tablaSalas.setItems(listaSalas);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar las salas: " + e.getMessage());
        }
    }

    @FXML
    private void buscarSala() {
        String criterio = txtBuscarSala.getText().trim();
        if (criterio.isEmpty()) {
            cargarSalas();
            return;
        }

        ObservableList<Sala> resultado = FXCollections.observableArrayList();

        for (Sala sala : listaSalas) {
            if (String.valueOf(sala.getIdSala()).contains(criterio) ||
                sala.getNombre().toLowerCase().contains(criterio.toLowerCase())) {
                resultado.add(sala);
            }
        }

        tablaSalas.setItems(resultado);
    }

    @FXML
    private void agregarSala() {
        // Implementa o reutiliza tu código para agregar sala aquí
    }

    private LocalTime parseHora(String textoHora) throws DateTimeParseException {
        textoHora = textoHora.trim().toUpperCase();
        System.out.println("Hora a parsear: '" + textoHora + "'");

        try {
            return LocalTime.parse(textoHora, formatterHora12ConEspacio);
        } catch (DateTimeParseException e1) {
            System.out.println("Error con formato con espacio: " + e1.getMessage());
            try {
                return LocalTime.parse(textoHora, formatterHora12SinEspacio);
            } catch (DateTimeParseException e2) {
                System.out.println("Error con formato sin espacio: " + e2.getMessage());
                try {
                    DateTimeFormatter formatter24h = DateTimeFormatter.ofPattern("HH:mm");
                    return LocalTime.parse(textoHora, formatter24h);
                } catch (DateTimeParseException e3) {
                    System.out.println("Error con formato 24h: " + e3.getMessage());
                    throw e1;
                }
            }
        }
    }

    @FXML
    private void prestarSala() {
        System.out.println("identificacionUsuarioActual = " + identificacionUsuarioActual);
        System.out.println("idAdministradorActual = " + idAdministradorActual);

        Sala salaSeleccionada = tablaSalas.getSelectionModel().getSelectedItem();
        if (salaSeleccionada == null) {
            mostrarAlerta("Advertencia", "Debe seleccionar una sala para prestar.");
            return;
        }

        LocalDate fechaPrestamo = datePickerFecha.getValue();
        if (fechaPrestamo == null) {
            mostrarAlerta("Advertencia", "Debe seleccionar una fecha para el préstamo.");
            return;
        }

        LocalTime horaInicio, horaFin;
        try {
            horaInicio = parseHora(txtHoraInicio.getText());
            horaFin = parseHora(txtHoraFin.getText());
        } catch (DateTimeParseException e) {
            mostrarAlerta("Error", "Formato de hora inválido. Use hh:mm AM/PM (Ej: 12:00 PM) o formato 24h (Ej: 14:30).");
            return;
        }

        if (horaFin.isBefore(horaInicio)) {
            mostrarAlerta("Error", "La hora fin no puede ser anterior a la hora inicio.");
            return;
        }

        String idUsuarioParaInsertar = null;
        String idAdministradorParaInsertar = null;

        try {
            if (identificacionUsuarioActual != null && !identificacionUsuarioActual.trim().isEmpty()) {
                if (!usuarioDAO.existeUsuario(identificacionUsuarioActual.trim())) {
                    mostrarAlerta("Error", "El usuario no existe en la base de datos.");
                    return;
                }
                idUsuarioParaInsertar = identificacionUsuarioActual.trim();
            } else if (idAdministradorActual != null && !idAdministradorActual.trim().isEmpty()) {
                if (!administradorDAO.existeAdministrador(idAdministradorActual.trim())) {
                    mostrarAlerta("Error", "El administrador no existe en la base de datos.");
                    return;
                }
                idAdministradorParaInsertar = idAdministradorActual.trim();
            } else {
                mostrarAlerta("Error", "Debe existir un usuario o administrador válido para prestar la sala.");
                return;
            }

            LocalDateTime fechaHoraInicio = LocalDateTime.of(fechaPrestamo, horaInicio);
            LocalDateTime fechaHoraFin = LocalDateTime.of(fechaPrestamo, horaFin);

            Prestamo nuevoPrestamo = new Prestamo(
                0,
                fechaHoraInicio,
                fechaHoraFin,
                "Activo",
                idUsuarioParaInsertar,
                salaSeleccionada.getIdSala(),
                0,
                idAdministradorParaInsertar
            );

            boolean exito = prestamoDAO.insertar(nuevoPrestamo);

            if (exito) {
                mostrarAlerta("Éxito", "Sala prestada correctamente.");
            } else {
                mostrarAlerta("Error", "No se pudo registrar el préstamo.");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al guardar préstamo: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void setIdentificacionUsuarioActual(String identificacionUsuario) {
        this.identificacionUsuarioActual = identificacionUsuario;
        this.idAdministradorActual = null;
    }

    public void setIdAdministradorActual(String idAdministrador) {
        this.idAdministradorActual = idAdministrador;
        this.identificacionUsuarioActual = null;
    }
}




