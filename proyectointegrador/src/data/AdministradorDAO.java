package data;

import modelo.Administrador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDAO {

    private Connection connection;

    public AdministradorDAO() {
        this.connection = ConexionDB.conectar();
    }

    // Verifica si un administrador existe dado su identificacion
    public boolean existeAdministrador(String identificacion) throws SQLException {
        String sql = "SELECT 1 FROM ADMINISTRADOR WHERE IDENTIFICACION = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, identificacion);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Obtener todos los administradores
    public List<Administrador> obtenerTodos() throws SQLException {
        List<Administrador> lista = new ArrayList<>();
        String sql = "SELECT IDENTIFICACION, NOMBRE_COMPLETO, EMAIL, CONTRASENA FROM ADMINISTRADOR";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Administrador admin = new Administrador(
                    rs.getString("IDENTIFICACION"),
                    rs.getString("NOMBRE_COMPLETO"),
                    rs.getString("EMAIL"),
                    rs.getString("CONTRASENA")
                );
                lista.add(admin);
            }
        }
        return lista;
    }

    // Insertar un nuevo administrador
    public boolean insertar(Administrador administrador) throws SQLException {
        String sql = "INSERT INTO ADMINISTRADOR (IDENTIFICACION, NOMBRE_COMPLETO, EMAIL, CONTRASENA) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, administrador.getIdentificacion());
            ps.setString(2, administrador.getNombreCompleto());
            ps.setString(3, administrador.getEmail());
            ps.setString(4, administrador.getContrasena());
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    // Actualizar un administrador
    public boolean actualizar(Administrador administrador) throws SQLException {
        String sql = "UPDATE ADMINISTRADOR SET NOMBRE_COMPLETO = ?, EMAIL = ?, CONTRASENA = ? WHERE IDENTIFICACION = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, administrador.getNombreCompleto());
            ps.setString(2, administrador.getEmail());
            ps.setString(3, administrador.getContrasena());
            ps.setString(4, administrador.getIdentificacion());
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    // Eliminar un administrador
    public boolean eliminar(String identificacion) throws SQLException {
        String sql = "DELETE FROM ADMINISTRADOR WHERE IDENTIFICACION = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, identificacion);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }
}
