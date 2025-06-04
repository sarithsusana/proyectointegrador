package modelo;

import javafx.beans.property.*;

public class Equipo {

    private IntegerProperty idEquipo;
    private StringProperty tipo;
    private StringProperty marca;
    private StringProperty estado;

    public Equipo(int idEquipo, String tipo, String marca, String estado) {
        this.idEquipo = new SimpleIntegerProperty(idEquipo);
        this.tipo = new SimpleStringProperty(tipo);
        this.marca = new SimpleStringProperty(marca);
        this.estado = new SimpleStringProperty(estado);
    }

    public int getIdEquipo() { return idEquipo.get(); }
    public void setIdEquipo(int idEquipo) { this.idEquipo.set(idEquipo); }
    public IntegerProperty idEquipoProperty() { return idEquipo; }

    public String getTipo() { return tipo.get(); }
    public void setTipo(String tipo) { this.tipo.set(tipo); }
    public StringProperty tipoProperty() { return tipo; }

    public String getMarca() { return marca.get(); }
    public void setMarca(String marca) { this.marca.set(marca); }
    public StringProperty marcaProperty() { return marca; }

    public String getEstado() { return estado.get(); }
    public void setEstado(String estado) { this.estado.set(estado); }
    public StringProperty estadoProperty() { return estado; }
}