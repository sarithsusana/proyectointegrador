package modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HorarioSala {
    private StringProperty horario;
    private StringProperty sala1;
    private StringProperty sala2;
    private StringProperty sala3;

    public HorarioSala(String horario, String sala1, String sala2, String sala3) {
        this.horario = new SimpleStringProperty(horario);
        this.sala1 = new SimpleStringProperty(sala1);
        this.sala2 = new SimpleStringProperty(sala2);
        this.sala3 = new SimpleStringProperty(sala3);
    }

    public StringProperty horarioProperty() {
        return horario;
    }

    public StringProperty sala1Property() {
        return sala1;
    }

    public StringProperty sala2Property() {
        return sala2;
    }

    public StringProperty sala3Property() {
        return sala3;
    }
    
    public String getHorario() {
        return horario.get();
    }
    
    public void setHorario(String horario) {
        this.horario.set(horario);
    }
    
    public String getSala1() {
        return sala1.get();
    }
    
    public void setSala1(String estado) {
        this.sala1.set(estado);
    }
    
    public String getSala2() {
        return sala2.get();
    }
    
    public void setSala2(String estado) {
        this.sala2.set(estado);
    }
    
    public String getSala3() {
        return sala3.get();
    }
    
    public void setSala3(String estado) {
        this.sala3.set(estado);
    }
}
