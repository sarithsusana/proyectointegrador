package controller;

import data.UsuarioDAO;
import modelo.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContraseña;
    @FXML private Button btnIngresar;
    @FXML private Button btnRegistrar;
    @FXML private Label lblError;

    @FXML
    private void validarLogin() {
        // Obtener los datos del formulario de login
        String correo = txtUsuario.getText().trim().toLowerCase();
        String contraseña = txtContraseña.getText().trim();

        // Validar que no estén vacíos
        if (correo.isEmpty() || contraseña.isEmpty()) {
            lblError.setText("Por favor, ingrese usuario y contraseña.");
            lblError.setVisible(true);
            return;
        }

        // Crear instancia del DAO para validar el login
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.validarLogin(correo, contraseña);

        // Si el usuario existe, pasamos al siguiente paso
        if (usuario != null) {
            lblError.setVisible(false);  // Ocultamos el error

            try {
                // Cargamos el archivo FXML del menú principal
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuPrincipal.fxml"));
                Parent root = loader.load();

                // Pasar el usuario al controlador del menú principal
                controller.MenuPrincipalController menuController = loader.getController();
                menuController.setUsuario(usuario);  // Asegúrate de que este método esté correctamente implementado

                // Cambiar la escena
                Stage stage = (Stage) btnIngresar.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al cargar MenuPrincipal.fxml: " + e.getMessage());
            }
        } else {
            // Si las credenciales son incorrectas, mostramos un mensaje
            lblError.setText("Credenciales incorrectas.");
            lblError.setVisible(true);
            txtUsuario.setStyle("-fx-border-color: red;");
            txtContraseña.setStyle("-fx-border-color: red;");
        }
    }

    @FXML
    private void irARegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Registro.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRegistrar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar Registro.fxml: " + e.getMessage());
        }
    }
}

