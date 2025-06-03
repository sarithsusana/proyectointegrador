package modelo;

import javafx.beans.property.*;

public class Usuario {
    private StringProperty identificacion;
    private StringProperty nombre;
    private StringProperty apellido;
    private StringProperty correo;
    private StringProperty tipoUsuario;
    private StringProperty contrasena;

    public Usuario(String identificacion, String nombre, String apellido, String correo, String tipoUsuario, String contrasena) {
        this.identificacion = new SimpleStringProperty(identificacion);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.correo = new SimpleStringProperty(correo);
        this.tipoUsuario = new SimpleStringProperty(tipoUsuario);
        this.contrasena = new SimpleStringProperty(contrasena);
    }

    public String getIdentificacion() { return identificacion.get(); }
    public void setIdentificacion(String identificacion) { this.identificacion.set(identificacion); }
    public StringProperty identificacionProperty() { return identificacion; }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public StringProperty nombreProperty() { return nombre; }

    public String getApellido() { return apellido.get(); }
    public void setApellido(String apellido) { this.apellido.set(apellido); }
    public StringProperty apellidoProperty() { return apellido; }

    public String getCorreo() { return correo.get(); }
    public void setCorreo(String correo) { this.correo.set(correo); }
    public StringProperty correoProperty() { return correo; }

    public String getTipoUsuario() { return tipoUsuario.get(); }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario.set(tipoUsuario); }
    public StringProperty tipoUsuarioProperty() { return tipoUsuario; }

    public String getContrasena() { return contrasena.get(); }
    public void setContrasena(String contrasena) { this.contrasena.set(contrasena); }
    public StringProperty contrasenaProperty() { return contrasena; }

    // Método para obtener rol (alias de tipoUsuario)
    public String getRol() {
        return getTipoUsuario();
    }

}
