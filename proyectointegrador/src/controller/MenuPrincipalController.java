package controller;

import modelo.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuPrincipalController {

    private Usuario usuario;  // Usuario actual logueado

    @FXML private Button btnGestionSalas;
    @FXML private Button btnGestionEquipos;
    @FXML private Button btnGestionPrestamos;
    @FXML private Button btnGestionUsuarios; // Este es el botón de "Gestionar Usuarios"
    @FXML private Button btnRegistrarUsuarios; // Este es el botón de "Registrar Usuarios"
    @FXML private Button btnGestionSanciones;
    @FXML private Button btnGestionEvaluaciones;
    @FXML private Button btnGestionMantenimiento;
    @FXML private Button btnVerSanciones;  // Asegúrate de que este también esté bien

    // Recibe el usuario desde LoginController
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        configurarPermisos();
    }

    // Configura los permisos según el tipo de usuario
    private void configurarPermisos() {
        String rol = usuario.getTipoUsuario();
        
        if (rol == null) rol = "";

        if (rol.equalsIgnoreCase("Administrador")) {
            // Si es administrador, mostrar todos los botones
            btnGestionSalas.setVisible(true);
            btnGestionEquipos.setVisible(true);
            btnGestionPrestamos.setVisible(true);
            btnRegistrarUsuarios.setVisible(true);  // Mostrar "Registrar Usuarios" solo para administrador
            btnGestionUsuarios.setVisible(true); // Mostrar "Gestionar Usuarios" solo para administrador
            btnGestionSanciones.setVisible(true);
            btnGestionEvaluaciones.setVisible(true);
            btnGestionMantenimiento.setVisible(true);
            btnVerSanciones.setVisible(false); // Ocultar para administradores
        } else {
            // Si no es administrador, solo mostrar ciertos botones
            btnGestionSalas.setVisible(true);
            btnGestionPrestamos.setVisible(true);
            btnRegistrarUsuarios.setVisible(false); // Ocultar "Registrar Usuarios" para usuarios
            btnGestionUsuarios.setVisible(false); // Ocultar "Gestionar Usuarios" para usuarios
            btnGestionSanciones.setVisible(false);
            btnGestionEvaluaciones.setVisible(false);
            btnGestionMantenimiento.setVisible(false);
            btnVerSanciones.setVisible(true); // Mostrar "Ver Sanciones" solo para usuarios
        }
    }

    @FXML
    private void abrirGestionSalas() {
        abrirVentana("GestionSalas.fxml");
    }

    @FXML
    private void abrirGestionEquipos() {
        abrirVentana("GestionEquipos.fxml");
    }

    @FXML
    private void abrirGestionPrestamos() {
        abrirVentana("GestionDePrestamos.fxml");
    }

    @FXML
    private void abrirGestionUsuarios() {
        abrirVentana("GestionUsuarios.fxml"); // Esta es la ventana de "Gestionar Usuarios"
    }

    @FXML
    private void abrirGestionSanciones() {
        abrirVentana("GestionSanciones.fxml");
    }

    @FXML
    private void abrirGestionEvaluaciones() {
        abrirVentana("GestionEvaluaciones.fxml");
    }

    @FXML
    private void abrirGestionMantenimiento() {
        abrirVentana("GestionMantenimiento.fxml");
    }

    @FXML
    private void abrirRegistrarUsuarios() {
        abrirVentana("Registro.fxml"); // Esta es la ventana de "Registrar Usuarios"
    }

    @FXML
    private void abrirVerSanciones() {
        abrirVentana("GestionSanciones.fxml");
    }

    // Método genérico para abrir las ventanas
    private void abrirVentana(String fxml) {
        try {
            // Verifica si la ruta es correcta
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/" + fxml));  // Ruta correcta

            // Si la ubicación del FXML no es válida, lanza un error
            if (loader.getLocation() == null) {
                throw new IOException("No se pudo encontrar el archivo FXML: " + fxml);
            }

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



