package modelo;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Prestamo {

    private IntegerProperty idPrestamo;
    private ObjectProperty<LocalDateTime> fechaHoraInicio;
    private ObjectProperty<LocalDateTime> fechaHoraFin;
    private StringProperty estado;
    private StringProperty identificacionUsuario; // antes idUsuario
    private IntegerProperty idSala;
    private ObjectProperty<Integer> idEquipo;  // Cambiado a ObjectProperty<Integer>
    private StringProperty idAdministrador;

    public Prestamo(int idPrestamo, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin,
                    String estado, String identificacionUsuario, int idSala, Integer idEquipo, String idAdministrador) {
        this.idPrestamo = new SimpleIntegerProperty(idPrestamo);
        this.fechaHoraInicio = new SimpleObjectProperty<>(fechaHoraInicio);
        this.fechaHoraFin = new SimpleObjectProperty<>(fechaHoraFin);
        this.estado = new SimpleStringProperty(estado);
        this.identificacionUsuario = new SimpleStringProperty(identificacionUsuario);
        this.idSala = new SimpleIntegerProperty(idSala);
        this.idEquipo = new SimpleObjectProperty<>(idEquipo);  // Puede ser null
        this.idAdministrador = new SimpleStringProperty(idAdministrador);
    }

    // Getters y setters

    public int getIdPrestamo() { return idPrestamo.get(); }
    public void setIdPrestamo(int id) { this.idPrestamo.set(id); }
    public IntegerProperty idPrestamoProperty() { return idPrestamo; }

    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio.get(); }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) { this.fechaHoraInicio.set(fechaHoraInicio); }
    public ObjectProperty<LocalDateTime> fechaHoraInicioProperty() { return fechaHoraInicio; }

    public LocalDateTime getFechaHoraFin() { return fechaHoraFin.get(); }
    public void setFechaHoraFin(LocalDateTime fechaHoraFin) { this.fechaHoraFin.set(fechaHoraFin); }
    public ObjectProperty<LocalDateTime> fechaHoraFinProperty() { return fechaHoraFin; }

    public String getEstado() { return estado.get(); }
    public void setEstado(String estado) { this.estado.set(estado); }
    public StringProperty estadoProperty() { return estado; }

    public String getIdentificacionUsuario() { return identificacionUsuario.get(); }
    public void setIdentificacionUsuario(String id) { this.identificacionUsuario.set(id); }
    public StringProperty identificacionUsuarioProperty() { return identificacionUsuario; }

    public int getIdSala() { return idSala.get(); }
    public void setIdSala(int id) { this.idSala.set(id); }
    public IntegerProperty idSalaProperty() { return idSala; }

    public Integer getIdEquipo() { return idEquipo.get(); }  // Cambiado a Integer
    public void setIdEquipo(Integer id) { this.idEquipo.set(id); }
    public ObjectProperty<Integer> idEquipoProperty() { return idEquipo; }

    public String getIdAdministrador() { return idAdministrador.get(); }
    public void setIdAdministrador(String id) { this.idAdministrador.set(id); }
    public StringProperty idAdministradorProperty() { return idAdministrador; }
}


