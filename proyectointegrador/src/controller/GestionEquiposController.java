package controller;

import data.EquipoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.Equipo;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
    private ObservableList<Equipo> listaEquipos;

    @FXML
    public void initialize() {
        equipoDAO = new EquipoDAO();

        colIdEquipo.setCellValueFactory(cell -> cell.getValue().idEquipoProperty());
        colTipo.setCellValueFactory(cell -> cell.getValue().tipoProperty());
        colMarca.setCellValueFactory(cell -> cell.getValue().marcaProperty());
        colEstado.setCellValueFactory(cell -> cell.getValue().estadoProperty());

        cargarEquipos();

        tablaEquipos.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            // Aquí podrías llenar un formulario para editar o mostrar detalles si tienes uno
        });
    }

    private void cargarEquipos() {
        try {
            List<Equipo> equipos = equipoDAO.obtenerTodos();
            listaEquipos = FXCollections.observableArrayList(equipos);
            tablaEquipos.setItems(listaEquipos);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los equipos: " + e.getMessage());
        }
    }

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

    @FXML
    private void agregarEquipo() {
        TextInputDialog dialogId = new TextInputDialog();
        dialogId.setHeaderText("Ingrese ID del equipo (numérico)");
        Optional<String> idResult = dialogId.showAndWait();
        if (idResult.isEmpty()) return;

        int idEquipo;
        try {
            idEquipo = Integer.parseInt(idResult.get());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "ID debe ser un número entero");
            return;
        }

        TextInputDialog dialogTipo = new TextInputDialog();
        dialogTipo.setHeaderText("Ingrese tipo de equipo");
        Optional<String> tipoResult = dialogTipo.showAndWait();
        if (tipoResult.isEmpty() || tipoResult.get().isBlank()) {
            mostrarAlerta("Error", "Tipo no puede estar vacío");
            return;
        }

        TextInputDialog dialogMarca = new TextInputDialog();
        dialogMarca.setHeaderText("Ingrese marca del equipo");
        Optional<String> marcaResult = dialogMarca.showAndWait();
        if (marcaResult.isEmpty() || marcaResult.get().isBlank()) {
            mostrarAlerta("Error", "Marca no puede estar vacía");
            return;
        }

        TextInputDialog dialogEstado = new TextInputDialog();
        dialogEstado.setHeaderText("Ingrese estado del equipo");
        Optional<String> estadoResult = dialogEstado.showAndWait();
        if (estadoResult.isEmpty() || estadoResult.get().isBlank()) {
            mostrarAlerta("Error", "Estado no puede estar vacío");
            return;
        }

        Equipo nuevoEquipo = new Equipo(idEquipo, tipoResult.get(), marcaResult.get(), estadoResult.get());

        try {
            boolean exito = equipoDAO.insertar(nuevoEquipo);
            if (exito) {
                mostrarAlerta("Éxito", "Equipo agregado correctamente");
                cargarEquipos();
            } else {
                mostrarAlerta("Error", "No se pudo agregar el equipo");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al agregar equipo: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

