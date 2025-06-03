package controller;

import data.UsuarioDAO;
import modelo.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class RegistroController {

    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtCorreo;
    @FXML private ComboBox<String> cbRolUsuario;
    @FXML private PasswordField txtContraseña;
    @FXML private PasswordField txtConfirmarContraseña;
    @FXML private CheckBox chkTerminos;
    @FXML private Button btnRegistrar;
    @FXML private Button btnVolverLogin;

    @FXML
    public void initialize() {
        cbRolUsuario.getItems().clear();
        cbRolUsuario.getItems().addAll("usuario", "administrador");
    }

    @FXML
    private void registrarUsuario() {
        String id = txtIdentificacion.getText().trim();
        String nombre = txtNombres.getText().trim();
        String apellido = txtApellidos.getText().trim();
        String correo = txtCorreo.getText().trim().toLowerCase();
        String rol = cbRolUsuario.getValue();
        String contraseña = txtContraseña.getText();
        String confirmar = txtConfirmarContraseña.getText();
        boolean acepto = chkTerminos.isSelected();

        if (id.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || rol == null || contraseña.isEmpty() || confirmar.isEmpty()) {
            mostrarAlerta("Validación", "Todos los campos son obligatorios");
            return;
        }

        if (!contraseña.equals(confirmar)) {
            mostrarAlerta("Validación", "Las contraseñas no coinciden");
            return;
        }

        if (!acepto) {
            mostrarAlerta("Validación", "Debe aceptar los términos y condiciones");
            return;
        }

        Usuario usuario = new Usuario(id, nombre, apellido, correo, rol, contraseña);

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        try {
            boolean exito = usuarioDAO.insertar(usuario);
            if (exito) {
                mostrarAlerta("Éxito", "Usuario registrado correctamente. Ahora puede iniciar sesión.");
                regresarALogin();
            } else {
                mostrarAlerta("Error", "No se pudo registrar el usuario. Verifique los datos.");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al registrar usuario: " + e.getMessage());
        }
    }

    @FXML
    private void regresarALogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolverLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar Login.fxml: " + e.getMessage());
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


