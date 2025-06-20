package data;

import modelo.Prestamo;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    private Connection connection;

    public PrestamoDAO() {
        this.connection = ConexionDB.conectar();
    }

    public List<Prestamo> obtenerTodos() throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT id_prestamo, fecha, hora_inicio, hora_fin, estado, id_usuario, id_sala, id_equipo, id_administrador FROM PRESTAMO";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Prestamo prestamo = new Prestamo(
                    rs.getInt("id_prestamo"),
                    rs.getTimestamp("hora_inicio").toLocalDateTime(),
                    rs.getTimestamp("hora_fin").toLocalDateTime(),
                    rs.getString("estado"),
                    rs.getString("id_usuario"),
                    rs.getInt("id_sala"),
                    rs.getInt("id_equipo"),
                    rs.getString("id_administrador")
                );
                lista.add(prestamo);
            }
        }
        return lista;
    }

    public List<Prestamo> obtenerPorFecha(LocalDate fecha) throws SQLException {
        List<Prestamo> lista = new ArrayList<>();

        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.atTime(23, 59, 59);

        String sql = "SELECT id_prestamo, fecha, hora_inicio, hora_fin, estado, id_usuario, id_sala, id_equipo, id_administrador " +
                     "FROM PRESTAMO WHERE hora_inicio BETWEEN ? AND ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(inicioDia));
            ps.setTimestamp(2, Timestamp.valueOf(finDia));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo prestamo = new Prestamo(
                        rs.getInt("id_prestamo"),
                        rs.getTimestamp("hora_inicio").toLocalDateTime(),
                        rs.getTimestamp("hora_fin").toLocalDateTime(),
                        rs.getString("estado"),
                        rs.getString("id_usuario"),
                        rs.getInt("id_sala"),
                        rs.getInt("id_equipo"),
                        rs.getString("id_administrador")
                    );
                    lista.add(prestamo);
                }
            }
        }
        return lista;
    }

    public boolean insertar(Prestamo prestamo) throws SQLException {
        System.out.println("Insertando préstamo con los siguientes valores:");
        System.out.println("identificacionUsuario: " + prestamo.getIdentificacionUsuario());
        System.out.println("idSala: " + prestamo.getIdSala());
        System.out.println("idEquipo: " + prestamo.getIdEquipo());
        System.out.println("idAdministrador: " + prestamo.getIdAdministrador());

        String sql = "INSERT INTO PRESTAMO (id_prestamo, fecha, hora_inicio, hora_fin, estado, id_usuario, id_sala, id_equipo, id_administrador) " +
                     "VALUES (SEQ_PRESTAMO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            LocalDateTime fechaHoraInicio = prestamo.getFechaHoraInicio();
            LocalDateTime fechaHoraFin = prestamo.getFechaHoraFin();

            ps.setDate(1, Date.valueOf(fechaHoraInicio.toLocalDate()));
            ps.setTimestamp(2, Timestamp.valueOf(fechaHoraInicio));
            ps.setTimestamp(3, Timestamp.valueOf(fechaHoraFin));

            ps.setString(4, prestamo.getEstado());

            if (prestamo.getIdentificacionUsuario() == null || prestamo.getIdentificacionUsuario().isEmpty()) {
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(5, prestamo.getIdentificacionUsuario());
            }

            ps.setInt(6, prestamo.getIdSala());

            if (prestamo.getIdEquipo() == 0) {
                ps.setNull(7, Types.INTEGER);
            } else {
                ps.setInt(7, prestamo.getIdEquipo());
            }

            if (prestamo.getIdAdministrador() == null || prestamo.getIdAdministrador().isEmpty()) {
                ps.setNull(8, Types.VARCHAR);
            } else {
                ps.setString(8, prestamo.getIdAdministrador());
            }

            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public boolean actualizar(Prestamo prestamo) throws SQLException {
        String sql = "UPDATE PRESTAMO SET fecha = ?, hora_inicio = ?, hora_fin = ?, estado = ?, id_usuario = ?, id_sala = ?, id_equipo = ?, id_administrador = ? WHERE id_prestamo = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            LocalDateTime fechaHoraInicio = prestamo.getFechaHoraInicio();
            LocalDateTime fechaHoraFin = prestamo.getFechaHoraFin();

            ps.setDate(1, Date.valueOf(fechaHoraInicio.toLocalDate()));
            ps.setTimestamp(2, Timestamp.valueOf(fechaHoraInicio));
            ps.setTimestamp(3, Timestamp.valueOf(fechaHoraFin));

            ps.setString(4, prestamo.getEstado());

            if (prestamo.getIdentificacionUsuario() == null || prestamo.getIdentificacionUsuario().isEmpty()) {
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(5, prestamo.getIdentificacionUsuario());
            }

            ps.setInt(6, prestamo.getIdSala());

            if (prestamo.getIdEquipo() == 0) {
                ps.setNull(7, Types.INTEGER);
            } else {
                ps.setInt(7, prestamo.getIdEquipo());
            }

            if (prestamo.getIdAdministrador() == null || prestamo.getIdAdministrador().isEmpty()) {
                ps.setNull(8, Types.VARCHAR);
            } else {
                ps.setString(8, prestamo.getIdAdministrador());
            }

            ps.setInt(9, prestamo.getIdPrestamo());

            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public boolean eliminar(int idPrestamo) throws SQLException {
        String sql = "DELETE FROM PRESTAMO WHERE id_prestamo = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }
}
