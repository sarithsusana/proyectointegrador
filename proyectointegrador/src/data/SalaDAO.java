package data;

import modelo.Sala;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaDAO {

    private Connection connection;

    public SalaDAO() {
        this.connection = ConexionDB.conectar();
    }

    public List<Sala> obtenerTodos() throws SQLException {
        List<Sala> lista = new ArrayList<>();
        String sql = "SELECT id_sala, nombre, capacidad, ubicacion FROM SALA";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Sala sala = new Sala(
                    rs.getInt("id_sala"),
                    rs.getString("nombre"),
                    rs.getInt("capacidad"),
                    rs.getString("ubicacion")
                );
                lista.add(sala);
            }
        }
        return lista;
    }

    public boolean insertar(Sala sala) throws SQLException {
        // Validar si existe sala con mismo id o nombre
        String checkSql = "SELECT COUNT(*) FROM SALA WHERE id_sala = ? OR nombre = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setInt(1, sala.getIdSala());
            checkStmt.setString(2, sala.getNombre());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Sala duplicada
                    return false;
                }
            }
        }

        // Insertar si no existe
        String sql = "INSERT INTO SALA (id_sala, nombre, capacidad, ubicacion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sala.getIdSala());
            ps.setString(2, sala.getNombre());
            ps.setInt(3, sala.getCapacidad());
            ps.setString(4, sala.getUbicacion());
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public boolean actualizar(Sala sala) throws SQLException {
        String sql = "UPDATE SALA SET nombre = ?, capacidad = ?, ubicacion = ? WHERE id_sala = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sala.getNombre());
            ps.setInt(2, sala.getCapacidad());
            ps.setString(3, sala.getUbicacion());
            ps.setInt(4, sala.getIdSala());
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public boolean eliminar(int idSala) throws SQLException {
        String sql = "DELETE FROM SALA WHERE id_sala = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idSala);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }
}

