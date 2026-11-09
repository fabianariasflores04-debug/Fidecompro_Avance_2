package fidecompro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventario {
    private final List<Producto> productos = new ArrayList<>();
    public void agregarProducto(String codigo, String nombre, String categoria,
                                String precioTexto, String cantidadTexto) throws ValidacionException {
        codigo = codigo.trim(); nombre = nombre.trim(); categoria = categoria.trim();
        if (codigo.isEmpty() || nombre.isEmpty() || categoria.isEmpty())
            throw new ValidacionException("Complete código, nombre y categoría.");
        if (buscarProducto(codigo) != null)
            throw new ValidacionException("Ya existe un producto con ese código.");
        BigDecimal precio;
        int cantidad;
        try {
            if (!precioTexto.trim().matches("[0-9]+([.,][0-9]{1,2})?"))
                throw new NumberFormatException();
            precio = new BigDecimal(precioTexto.trim().replace(',', '.'));
            cantidad = Integer.parseInt(cantidadTexto.trim());
        } catch (NumberFormatException ex) {
            throw new ValidacionException("Precio: número sin separadores de miles y hasta 2 decimales. Cantidad: entero.");
        }
        if (precio.signum() <= 0 || cantidad < 0)
            throw new ValidacionException("El precio debe ser mayor que cero y la cantidad no puede ser negativa.");
        productos.add(new Producto(codigo, nombre, categoria, precio, cantidad));
    }
    public Producto buscarProducto(String codigo) {
        for (Producto p : productos)
            if (p.getCodigo().equalsIgnoreCase(codigo.trim())) return p;
        return null;
    }
    public List<Producto> consultarExistencias() {
        return Collections.unmodifiableList(new ArrayList<>(productos));
    }
}
