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
        String correo = txtUsuario.getText().trim().toLowerCase();
        String contraseña = txtContraseña.getText().trim();

        if (correo.isEmpty() || contraseña.isEmpty()) {
            lblError.setText("Por favor, ingrese usuario y contraseña.");
            lblError.setVisible(true);
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.validarLogin(correo, contraseña);

        if (usuario != null) {
            lblError.setVisible(false);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuPrincipal.fxml"));
                Parent root = loader.load();

                // Pasar el usuario al controlador del menú principal
                controller.MenuPrincipalController menuController = loader.getController();
                menuController.setUsuario(usuario);

                Stage stage = (Stage) btnIngresar.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al cargar MenuPrincipal.fxml: " + e.getMessage());
            }
        } else {
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

