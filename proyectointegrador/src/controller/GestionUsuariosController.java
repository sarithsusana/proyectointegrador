package controller;

import data.UsuarioDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.Usuario;

import java.sql.SQLException;
import java.util.List;

public class GestionUsuariosController {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colIdentificacion;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colApellido;
    @FXML private TableColumn<Usuario, String> colCorreo;
    @FXML private TableColumn<Usuario, String> colTipoUsuario;

    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCorreo;
    @FXML private ComboBox<String> comboTipoUsuario;
    @FXML private PasswordField txtContrasena;

    private UsuarioDAO usuarioDAO;
    private ObservableList<Usuario> listaUsuarios;

    @FXML
    public void initialize() {
        usuarioDAO = new UsuarioDAO();

        colIdentificacion.setCellValueFactory(cell -> cell.getValue().identificacionProperty());
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());
        colApellido.setCellValueFactory(cell -> cell.getValue().apellidoProperty());
        colCorreo.setCellValueFactory(cell -> cell.getValue().correoProperty());
        colTipoUsuario.setCellValueFactory(cell -> cell.getValue().tipoUsuarioProperty());

        cargarUsuarios();

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, antiguo, nuevo) -> {
            if (nuevo != null) {
                llenarFormulario(nuevo);
            }
        });

        comboTipoUsuario.setItems(FXCollections.observableArrayList("usuario", "administrador"));
    }

    private void cargarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioDAO.obtenerTodos();
            listaUsuarios = FXCollections.observableArrayList(usuarios);
            tablaUsuarios.setItems(listaUsuarios);
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void llenarFormulario(Usuario usuario) {
        txtIdentificacion.setText(usuario.getIdentificacion());
        txtNombre.setText(usuario.getNombre());
        txtApellido.setText(usuario.getApellido());
        txtCorreo.setText(usuario.getCorreo());
        comboTipoUsuario.setValue(usuario.getTipoUsuario());
        txtContrasena.setText(usuario.getContrasena());
        txtIdentificacion.setDisable(true);  
    }

    @FXML
    private void limpiarFormulario() {
        txtIdentificacion.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtCorreo.clear();
        comboTipoUsuario.getSelectionModel().clearSelection();
        txtContrasena.clear();
        txtIdentificacion.setDisable(false);
        tablaUsuarios.getSelectionModel().clearSelection();
    }

    @FXML
    private void agregarUsuario() {
        if (!validarFormulario()) return;

        Usuario usuario = new Usuario(
            txtIdentificacion.getText(),
            txtNombre.getText(),
            txtApellido.getText(),
            txtCorreo.getText(),
            comboTipoUsuario.getValue(),
            txtContrasena.getText()
        );

        try {
            boolean exito = usuarioDAO.insertar(usuario);
            if (exito) {
                mostrarAlerta("Éxito", "Usuario agregado correctamente");
                cargarUsuarios();
                limpiarFormulario();
            } else {
                mostrarAlerta("Error", "No se pudo agregar el usuario");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al agregar usuario: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarUsuario() {
        if (!validarFormulario()) return;

        Usuario usuario = new Usuario(
            txtIdentificacion.getText(),
            txtNombre.getText(),
            txtApellido.getText(),
            txtCorreo.getText(),
            comboTipoUsuario.getValue(),
            txtContrasena.getText()
        );

        try {
            boolean exito = usuarioDAO.actualizar(usuario);
            if (exito) {
                mostrarAlerta("Éxito", "Usuario actualizado correctamente");
                cargarUsuarios();
                limpiarFormulario();
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el usuario");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al actualizar usuario: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarUsuario() {
        Usuario usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuario == null) {
            mostrarAlerta("Advertencia", "Seleccione un usuario para eliminar");
            return;
        }

        try {
            boolean exito = usuarioDAO.eliminar(usuario.getIdentificacion());
            if (exito) {
                mostrarAlerta("Éxito", "Usuario eliminado correctamente");
                cargarUsuarios();
                limpiarFormulario();
            } else {
                mostrarAlerta("Error", "No se pudo eliminar el usuario");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al eliminar usuario: " + e.getMessage());
        }
    }

    private boolean validarFormulario() {
        if (txtIdentificacion.getText().isEmpty() ||
            txtNombre.getText().isEmpty() ||
            txtApellido.getText().isEmpty() ||
            txtCorreo.getText().isEmpty() ||
            comboTipoUsuario.getValue() == null ||
            txtContrasena.getText().isEmpty()) {
            mostrarAlerta("Validación", "Todos los campos son obligatorios");
            return false;
        }

        // Validar correo simple
        if (!txtCorreo.getText().matches("^\\S+@\\S+\\.\\S+$")) {
            mostrarAlerta("Validación", "Ingrese un correo válido");
            return false;
        }

        // Validar tipo usuario
        String tipo = comboTipoUsuario.getValue();
        if (!tipo.equals("usuario") && !tipo.equals("administrador")) {
            mostrarAlerta("Validación", "Tipo de usuario inválido");
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
