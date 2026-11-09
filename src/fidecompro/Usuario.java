package fidecompro;

import java.util.Arrays;

public class Usuario extends Persona {
    private final String nombreUsuario;
    private final char[] contrasena;
    public Usuario(String id, String nombre, String nombreUsuario, char[] contrasena) {
        super(id, nombre);
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena.clone();
    }
    @Override public String getTipo() { return "Empleado"; }
    public boolean validarCredenciales(String usuario, char[] clave) {
        return nombreUsuario.equals(usuario) && Arrays.equals(contrasena, clave);
    }
}
