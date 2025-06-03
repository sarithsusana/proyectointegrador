package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.Mantenimiento;

public class MantenimientoDAO {

    public List<Mantenimiento> obtenerTodos() throws SQLException {
        List<Mantenimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM MANTENIMIENTO";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Mantenimiento m = new Mantenimiento(
                    rs.getInt("id_mantenimiento"),
                    rs.getInt("id_equipo"),
                    rs.getString("descripcion"),
                    rs.getDate("fecha_inicio").toLocalDate(),
                    rs.getDate("fecha_fin").toLocalDate(),
                    rs.getString("estado")
                );
                lista.add(m);
            }
        }
        return lista;
    }

    public boolean insertar(Mantenimiento m) throws SQLException {
        String sql = "INSERT INTO MANTENIMIENTO (id_mantenimiento, id_equipo, descripcion, fecha_inicio, fecha_fin, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, m.getIdMantenimiento());
            stmt.setInt(2, m.getIdEquipo());
            stmt.setString(3, m.getDescripcion());
            stmt.setDate(4, Date.valueOf(m.getFechaInicio()));
            stmt.setDate(5, Date.valueOf(m.getFechaFin()));
            stmt.setString(6, m.getEstado());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean actualizar(Mantenimiento m) throws SQLException {
        String sql = "UPDATE MANTENIMIENTO SET id_equipo = ?, descripcion = ?, fecha_inicio = ?, fecha_fin = ?, estado = ? WHERE id_mantenimiento = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, m.getIdEquipo());
            stmt.setString(2, m.getDescripcion());
            stmt.setDate(3, Date.valueOf(m.getFechaInicio()));
            stmt.setDate(4, Date.valueOf(m.getFechaFin()));
            stmt.setString(5, m.getEstado());
            stmt.setInt(6, m.getIdMantenimiento());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idMantenimiento) throws SQLException {
        String sql = "DELETE FROM MANTENIMIENTO WHERE id_mantenimiento = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMantenimiento);
            return stmt.executeUpdate() > 0;
        }
    }
}
