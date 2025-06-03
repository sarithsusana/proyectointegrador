package data;

import modelo.Evaluacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvaluacionDAO {

    public List<Evaluacion> obtenerTodos() throws SQLException {
        List<Evaluacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM EVALUACION";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Evaluacion ev = new Evaluacion(
                    rs.getInt("id_evaluacion"),
                    rs.getString("descripcion"),
                    rs.getInt("calificacion"),
                    rs.getString("id_usuario")
                );
                lista.add(ev);
            }
        }
        return lista;
    }

    public boolean insertar(Evaluacion ev) throws SQLException {
        String sql = "INSERT INTO EVALUACION (id_evaluacion, descripcion, calificacion, id_usuario) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ev.getIdEvaluacion());
            stmt.setString(2, ev.getDescripcion());
            stmt.setInt(3, ev.getCalificacion());
            stmt.setString(4, ev.getIdUsuario());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean actualizar(Evaluacion ev) throws SQLException {
        String sql = "UPDATE EVALUACION SET descripcion = ?, calificacion = ?, id_usuario = ? WHERE id_evaluacion = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ev.getDescripcion());
            stmt.setInt(2, ev.getCalificacion());
            stmt.setString(3, ev.getIdUsuario());
            stmt.setInt(4, ev.getIdEvaluacion());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idEvaluacion) throws SQLException {
        String sql = "DELETE FROM EVALUACION WHERE id_evaluacion = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEvaluacion);
            return stmt.executeUpdate() > 0;
        }
    }
}
