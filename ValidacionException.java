package fidecompro;

/** Error de datos que puede corregir la persona usuaria. */
public class ValidacionException extends Exception {
    public ValidacionException(String mensaje) { super(mensaje); }
}
