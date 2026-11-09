package fidecompro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Reglas del avance; no depende de ventanas Swing. Datos en memoria. */
public class Sistema {
    private final List<Cliente> clientes = new ArrayList<>();
    private final Usuario empleado = new Usuario("1", "Empleado Fidecompro", "admin", "Fide2026".toCharArray());
    private final Inventario inventario = new Inventario();
    public Usuario iniciarSesion(String usuario, char[] clave) throws ValidacionException {
        if (!empleado.validarCredenciales(usuario.trim(), clave))
            throw new ValidacionException("Usuario o contraseña incorrectos.");
        return empleado;
    }
    public void registrarCliente(String id, String nombre, String telefono, String correo)
            throws ValidacionException {
        id = id.trim(); nombre = nombre.trim(); telefono = telefono.trim(); correo = correo.trim();
        if (id.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty())
            throw new ValidacionException("Complete todos los datos del cliente.");
        if (!correo.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"))
            throw new ValidacionException("Escriba un correo válido.");
        if (buscarCliente(id) != null)
            throw new ValidacionException("Ya existe un cliente con esa identificación.");
        clientes.add(new Cliente(id, nombre, telefono, correo));
    }
    public Cliente buscarCliente(String id) {
        for (Cliente c : clientes)
            if (c.getIdentificacion().equalsIgnoreCase(id.trim())) return c;
        return null;
    }
    public List<Cliente> getClientes() { return Collections.unmodifiableList(new ArrayList<>(clientes)); }
    public Inventario getInventario() { return inventario; }
}
