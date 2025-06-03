package modelo;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Mantenimiento {
    private IntegerProperty idMantenimiento;
    private IntegerProperty idEquipo;
    private StringProperty descripcion;
    private ObjectProperty<LocalDate> fechaInicio;
    private ObjectProperty<LocalDate> fechaFin;
    private StringProperty estado; // Estado actual del equipo (Ej: En mantenimiento, Reparado, etc)

    public Mantenimiento(int idMantenimiento, int idEquipo, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, String estado) {
        this.idMantenimiento = new SimpleIntegerProperty(idMantenimiento);
        this.idEquipo = new SimpleIntegerProperty(idEquipo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.fechaInicio = new SimpleObjectProperty<>(fechaInicio);
        this.fechaFin = new SimpleObjectProperty<>(fechaFin);
        this.estado = new SimpleStringProperty(estado);
    }

    public int getIdMantenimiento() { return idMantenimiento.get(); }
    public void setIdMantenimiento(int id) { this.idMantenimiento.set(id); }
    public IntegerProperty idMantenimientoProperty() { return idMantenimiento; }

    public int getIdEquipo() { return idEquipo.get(); }
    public void setIdEquipo(int idEquipo) { this.idEquipo.set(idEquipo); }
    public IntegerProperty idEquipoProperty() { return idEquipo; }

    public String getDescripcion() { return descripcion.get(); }
    public void setDescripcion(String descripcion) { this.descripcion.set(descripcion); }
    public StringProperty descripcionProperty() { return descripcion; }

    public LocalDate getFechaInicio() { return fechaInicio.get(); }
    public void setFechaInicio(LocalDate fecha) { this.fechaInicio.set(fecha); }
    public ObjectProperty<LocalDate> fechaInicioProperty() { return fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin.get(); }
    public void setFechaFin(LocalDate fecha) { this.fechaFin.set(fecha); }
    public ObjectProperty<LocalDate> fechaFinProperty() { return fechaFin; }

    public String getEstado() { return estado.get(); }
    public void setEstado(String estado) { this.estado.set(estado); }
    public StringProperty estadoProperty() { return estado; }
}

