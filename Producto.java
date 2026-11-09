package fidecompro;

import java.math.BigDecimal;

public class Producto {
    private final String codigo;
    private final String nombre;
    private final String categoria;
    private final BigDecimal precio;
    private final int cantidadDisponible;
    public Producto(String codigo, String nombre, String categoria, BigDecimal precio, int cantidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidadDisponible = cantidad;
    }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public BigDecimal getPrecio() { return precio; }
    public int getCantidadDisponible() { return cantidadDisponible; }
}
