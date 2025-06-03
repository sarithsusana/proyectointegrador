package modelo;

import javafx.beans.property.*;

public class Evaluacion {

    private IntegerProperty idEvaluacion;
    private StringProperty descripcion;
    private IntegerProperty calificacion;
    private StringProperty idUsuario;

    public Evaluacion(int idEvaluacion, String descripcion, int calificacion, String idUsuario) {
        this.idEvaluacion = new SimpleIntegerProperty(idEvaluacion);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.calificacion = new SimpleIntegerProperty(calificacion);
        this.idUsuario = new SimpleStringProperty(idUsuario);
    }

    public int getIdEvaluacion() { return idEvaluacion.get(); }
    public void setIdEvaluacion(int id) { this.idEvaluacion.set(id); }
    public IntegerProperty idEvaluacionProperty() { return idEvaluacion; }

    public String getDescripcion() { return descripcion.get(); }
    public void setDescripcion(String desc) { this.descripcion.set(desc); }
    public StringProperty descripcionProperty() { return descripcion; }

    public int getCalificacion() { return calificacion.get(); }
    public void setCalificacion(int calif) { this.calificacion.set(calif); }
    public IntegerProperty calificacionProperty() { return calificacion; }

    public String getIdUsuario() { return idUsuario.get(); }
    public void setIdUsuario(String id) { this.idUsuario.set(id); }
    public StringProperty idUsuarioProperty() { return idUsuario; }
}

