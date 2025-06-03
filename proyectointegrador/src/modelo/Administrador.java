package modelo;

public class Administrador {
    private String identificacion;
    private String nombreCompleto;
    private String email;
    private String contrasena;

    public Administrador(String identificacion, String nombreCompleto, String email, String contrasena) {
        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.contrasena = contrasena;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
