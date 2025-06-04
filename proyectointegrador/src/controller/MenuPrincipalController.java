package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import modelo.Usuario;

import java.io.IOException;

public class MenuPrincipalController {

    private Usuario usuario;  // Usuario actual logueado

    @FXML private Button btnGestionSalas;
    @FXML private Button btnGestionEquipos;
    @FXML private Button btnGestionPrestamos;
    @FXML private Button btnGestionUsuarios;
    @FXML private Button btnGestionSanciones;
    @FXML private Button btnGestionEvaluaciones;
    @FXML private Button btnGestionMantenimiento;

    // Recibe el usuario desde LoginController
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        configurarPermisos();
    }

    private void configurarPermisos() {
        String rol = usuario.getTipoUsuario();
        if (rol == null) rol = "";

        System.out.println("Rol usuario detectado: " + rol);

        if (rol.equalsIgnoreCase("Administrador")) {
            btnGestionSalas.setVisible(true);
            btnGestionEquipos.setVisible(true);
            btnGestionPrestamos.setVisible(true);
            btnGestionUsuarios.setVisible(true);
            btnGestionSanciones.setVisible(true);
            btnGestionEvaluaciones.setVisible(true);
            btnGestionMantenimiento.setVisible(true);
        } else {
            btnGestionSalas.setVisible(true);
            btnGestionPrestamos.setVisible(true);

            btnGestionEquipos.setVisible(false);
            btnGestionUsuarios.setVisible(false);
            btnGestionSanciones.setVisible(false);
            btnGestionEvaluaciones.setVisible(false);
            btnGestionMantenimiento.setVisible(false);
        }
    }

    @FXML
    private void abrirGestionSalas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GestionSalas.fxml"));
            Parent root = loader.load();

            // Obtener controlador para pasar el usuario
            controller.GestionSalasController gestionController = loader.getController();

            if (usuario.getTipoUsuario().equalsIgnoreCase("Administrador")) {
                gestionController.setIdAdministradorActual(usuario.getIdentificacion());
            } else {
                gestionController.setIdentificacionUsuarioActual(usuario.getIdentificacion());
            }

            Stage stage = new Stage();
            stage.setTitle("Gestión de Salas");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir Gestión de Salas: " + e.getMessage());
        }
    }

    @FXML
    private void abrirGestionEquipos() {
        abrirVentana("view/GestionEquipos.fxml");
    }

    @FXML
    private void abrirGestionPrestamos() {
        abrirVentana("view/GestionDePrestamos.fxml");
    }

    @FXML
    private void abrirGestionUsuarios() {
        abrirVentana("view/GestionUsuarios.fxml");
    }

    @FXML
    private void abrirGestionSanciones() {
        abrirVentana("view/GestionSanciones.fxml");
    }

    @FXML
    private void abrirGestionEvaluaciones() {
        abrirVentana("view/GestionEvaluaciones.fxml");
    }

    @FXML
    private void abrirGestionMantenimiento() {
        abrirVentana("view/GestionMantenimiento.fxml");
    }

    private void abrirVentana(String fxml) {
        try {
            String resourcePath = fxml.startsWith("/") ? fxml : "/" + fxml;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Sistema Universitario");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir " + fxml + ": " + e.getMessage());
        }
    }
}