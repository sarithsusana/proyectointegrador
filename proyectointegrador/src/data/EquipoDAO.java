package data;

import modelo.Equipo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAO {

    private Connection connection;

    public EquipoDAO() {
        this.connection = ConexionDB.conectar();
    }

    public List<Equipo> obtenerTodos() throws SQLException {
        List<Equipo> lista = new ArrayList<>();
        String sql = "SELECT id_equipo, tipo, marca, estado FROM EQUIPO_AUDIOVISUAL";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Equipo equipo = new Equipo(
                    rs.getInt("id_equipo"),
                    rs.getString("tipo"),
                    rs.getString("marca"),
                    rs.getString("estado")
                );
                lista.add(equipo);
            }
        }
        return lista;
    }

    public boolean insertar(Equipo equipo) throws SQLException {
        String sql = "INSERT INTO EQUIPO_AUDIOVISUAL (id_equipo, tipo, marca, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, equipo.getIdEquipo());
            ps.setString(2, equipo.getTipo());
            ps.setString(3, equipo.getMarca());
            ps.setString(4, equipo.getEstado());
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public boolean actualizar(Equipo equipo) throws SQLException {
        String sql = "UPDATE EQUIPO_AUDIOVISUAL SET tipo = ?, marca = ?, estado = ? WHERE id_equipo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, equipo.getTipo());
            ps.setString(2, equipo.getMarca());
            ps.setString(3, equipo.getEstado());
            ps.setInt(4, equipo.getIdEquipo());
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public boolean eliminar(int idEquipo) throws SQLException {
        String sql = "DELETE FROM EQUIPO_AUDIOVISUAL WHERE id_equipo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }
}

