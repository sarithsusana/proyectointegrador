package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.Sancion;

public class SancionDAO {

    public List<Sancion> obtenerTodos() throws SQLException {
        List<Sancion> lista = new ArrayList<>();
        String sql = "SELECT * FROM SANCION";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sancion sancion = new Sancion(
                    rs.getInt("id_sancion"),
                    rs.getString("motivo"),
                    rs.getDate("fecha_inicio").toLocalDate(),
                    rs.getDate("fecha_fin").toLocalDate(),
                    rs.getString("id_usuario")
                );
                lista.add(sancion);
            }
        }
        return lista;
    }

    public boolean insertar(Sancion sancion) throws SQLException {
        String sql = "INSERT INTO SANCION (id_sancion, motivo, fecha_inicio, fecha_fin, id_usuario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sancion.getIdSancion());
            stmt.setString(2, sancion.getMotivo());
            stmt.setDate(3, Date.valueOf(sancion.getFechaInicio()));
            stmt.setDate(4, Date.valueOf(sancion.getFechaFin()));
            stmt.setString(5, sancion.getIdUsuario());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean actualizar(Sancion sancion) throws SQLException {
        String sql = "UPDATE SANCION SET motivo = ?, fecha_inicio = ?, fecha_fin = ?, id_usuario = ? WHERE id_sancion = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sancion.getMotivo());
            stmt.setDate(2, Date.valueOf(sancion.getFechaInicio()));
            stmt.setDate(3, Date.valueOf(sancion.getFechaFin()));
            stmt.setString(4, sancion.getIdUsuario());
            stmt.setInt(5, sancion.getIdSancion());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idSancion) throws SQLException {
        String sql = "DELETE FROM SANCION WHERE id_sancion = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSancion);
            return stmt.executeUpdate() > 0;
        }
    }
}

