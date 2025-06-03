package data;

import modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private Connection connection;

    public UsuarioDAO() {
        this.connection = ConexionDB.conectar();
    }

    public List<Usuario> obtenerTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT identificacion, nombre, apellido, correo, tipo_usuario, contrasena FROM USUARIO";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                    rs.getString("identificacion"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("correo"),
                    rs.getString("tipo_usuario"),
                    rs.getString("contrasena")
                );
                lista.add(usuario);
            }
        }
        return lista;
    }

    public boolean insertar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO USUARIO (identificacion, nombre, apellido, correo, tipo_usuario, contrasena) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, usuario.getIdentificacion());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellido());
            ps.setString(4, usuario.getCorreo());
            ps.setString(5, usuario.getTipoUsuario());
            ps.setString(6, usuario.getContrasena());
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public boolean actualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE USUARIO SET nombre = ?, apellido = ?, correo = ?, tipo_usuario = ?, contrasena = ? WHERE identificacion = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getTipoUsuario());
            ps.setString(5, usuario.getContrasena());
            ps.setString(6, usuario.getIdentificacion());
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public boolean eliminar(String identificacion) throws SQLException {
        String sql = "DELETE FROM USUARIO WHERE identificacion = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, identificacion);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    // Método para verificar si un usuario existe según su identificacion
    public boolean existeUsuario(String identificacion) throws SQLException {
        String sql = "SELECT 1 FROM USUARIO WHERE IDENTIFICACION = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, identificacion);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();  // true si existe, false si no
            }
        }
    }

    // Método validarLogin corregido para buscar en USUARIO y ADMINISTRADOR
    public Usuario validarLogin(String correo, String contrasena) {
        Usuario usuario = null;
        String sqlUsuario = "SELECT identificacion, nombre, apellido, correo, tipo_usuario, contrasena FROM USUARIO WHERE LOWER(correo) = ? AND contrasena = ?";
        String sqlAdmin = "SELECT IDENTIFICACION AS identificacion, nombre_completo AS nombre, '' AS apellido, email AS correo, 'Administrador' AS tipo_usuario, contrasena FROM ADMINISTRADOR WHERE LOWER(email) = ? AND contrasena = ?";

        try {
            String correoTrim = correo.trim().toLowerCase();
            String contrasenaTrim = contrasena.trim();

            System.out.println("Intentando login para correo: '" + correoTrim + "' con contraseña: '" + contrasenaTrim + "'");

            // Buscar en USUARIO
            try (PreparedStatement ps = connection.prepareStatement(sqlUsuario)) {
                ps.setString(1, correoTrim);
                ps.setString(2, contrasenaTrim);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Usuario encontrado en tabla USUARIO");
                        usuario = new Usuario(
                            rs.getString("identificacion"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("correo"),
                            rs.getString("tipo_usuario"),
                            rs.getString("contrasena")
                        );
                        return usuario;
                    } else {
                        System.out.println("No encontrado en USUARIO, buscando en ADMINISTRADOR...");
                    }
                }
            }

            // Buscar en ADMINISTRADOR
            try (PreparedStatement ps = connection.prepareStatement(sqlAdmin)) {
                ps.setString(1, correoTrim);
                ps.setString(2, contrasenaTrim);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Usuario encontrado en tabla ADMINISTRADOR");
                        usuario = new Usuario(
                            rs.getString("identificacion"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("correo"),
                            rs.getString("tipo_usuario"),
                            rs.getString("contrasena")
                        );
                    } else {
                        System.out.println("No encontrado en ADMINISTRADOR tampoco.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error en validarLogin: " + e.getMessage());
        }
        return usuario;
    }

    // Método para listar usuarios y contraseñas en consola para depuración
    public void listarUsuariosParaDepuracion() {
        try {
            System.out.println("Usuarios en USUARIO:");
            String sqlUsuario = "SELECT correo, contrasena FROM USUARIO";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlUsuario)) {
                while (rs.next()) {
                    System.out.println("Correo: '" + rs.getString("correo") + "', Contraseña: '" + rs.getString("contrasena") + "'");
                }
            }

            System.out.println("Usuarios en ADMINISTRADOR:");
            String sqlAdmin = "SELECT email, contrasena FROM ADMINISTRADOR";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlAdmin)) {
                while (rs.next()) {
                    System.out.println("Correo: '" + rs.getString("email") + "', Contraseña: '" + rs.getString("contrasena") + "'");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


