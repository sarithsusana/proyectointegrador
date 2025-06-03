package modelo;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Sancion {

    private IntegerProperty idSancion;
    private StringProperty motivo;
    private ObjectProperty<LocalDate> fechaInicio;
    private ObjectProperty<LocalDate> fechaFin;
    private StringProperty idUsuario;

    public Sancion(int idSancion, String motivo, LocalDate fechaInicio, LocalDate fechaFin, String idUsuario) {
        this.idSancion = new SimpleIntegerProperty(idSancion);
        this.motivo = new SimpleStringProperty(motivo);
        this.fechaInicio = new SimpleObjectProperty<>(fechaInicio);
        this.fechaFin = new SimpleObjectProperty<>(fechaFin);
        this.idUsuario = new SimpleStringProperty(idUsuario);
    }

    public int getIdSancion() { return idSancion.get(); }
    public void setIdSancion(int id) { this.idSancion.set(id); }
    public IntegerProperty idSancionProperty() { return idSancion; }

    public String getMotivo() { return motivo.get(); }
    public void setMotivo(String motivo) { this.motivo.set(motivo); }
    public StringProperty motivoProperty() { return motivo; }

    public LocalDate getFechaInicio() { return fechaInicio.get(); }
    public void setFechaInicio(LocalDate fecha) { this.fechaInicio.set(fecha); }
    public ObjectProperty<LocalDate> fechaInicioProperty() { return fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin.get(); }
    public void setFechaFin(LocalDate fecha) { this.fechaFin.set(fecha); }
    public ObjectProperty<LocalDate> fechaFinProperty() { return fechaFin; }

    public String getIdUsuario() { return idUsuario.get(); }
    public void setIdUsuario(String idUsuario) { this.idUsuario.set(idUsuario); }
    public StringProperty idUsuarioProperty() { return idUsuario; }
}

