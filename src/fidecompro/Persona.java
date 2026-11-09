package fidecompro;

/** Clase base: los clientes y usuarios comparten identificación y nombre. */
public abstract class Persona {
    private final String identificacion;
    private final String nombre;
    protected Persona(String identificacion, String nombre) {
        this.identificacion = identificacion;
        this.nombre = nombre;
    }
    public String getIdentificacion() { return identificacion; }
    public String getNombre() { return nombre; }
    public abstract String getTipo();
    public String descripcion() { return getTipo() + ": " + nombre + " (" + identificacion + ")"; }
}
