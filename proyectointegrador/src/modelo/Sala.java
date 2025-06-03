package modelo;

import javafx.beans.property.*;

public class Sala {

    private IntegerProperty idSala;
    private StringProperty nombre;
    private IntegerProperty capacidad;
    private StringProperty ubicacion;

    public Sala(int idSala, String nombre, int capacidad, String ubicacion) {
        this.idSala = new SimpleIntegerProperty(idSala);
        this.nombre = new SimpleStringProperty(nombre);
        this.capacidad = new SimpleIntegerProperty(capacidad);
        this.ubicacion = new SimpleStringProperty(ubicacion);
    }

    // Getters y setters con propiedades

    public int getIdSala() { return idSala.get(); }
    public void setIdSala(int idSala) { this.idSala.set(idSala); }
    public IntegerProperty idSalaProperty() { return idSala; }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public StringProperty nombreProperty() { return nombre; }

    public int getCapacidad() { return capacidad.get(); }
    public void setCapacidad(int capacidad) { this.capacidad.set(capacidad); }
    public IntegerProperty capacidadProperty() { return capacidad; }

    public String getUbicacion() { return ubicacion.get(); }
    public void setUbicacion(String ubicacion) { this.ubicacion.set(ubicacion); }
    public StringProperty ubicacionProperty() { return ubicacion; }
}
