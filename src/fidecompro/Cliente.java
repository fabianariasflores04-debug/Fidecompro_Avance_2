package fidecompro;

public class Cliente extends Persona {
    private final String telefono;
    private final String correo;
    public Cliente(String identificacion, String nombre, String telefono, String correo) {
        super(identificacion, nombre);
        this.telefono = telefono;
        this.correo = correo;
    }
    @Override public String getTipo() { return "Cliente"; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
}
