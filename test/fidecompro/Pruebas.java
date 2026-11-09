package fidecompro;

/** Verificaciones sin frameworks. Lanza AssertionError si falla una regla. */
public class Pruebas {
    private static int total;
    private interface Accion { void ejecutar() throws Exception; }
    private static void comprobar(boolean resultado) {
        total++; if (!resultado) throw new AssertionError("Fallo en comprobación " + total);
    }
    private static void rechazar(Accion accion) throws Exception {
        try { accion.ejecutar(); } catch (ValidacionException ex) { total++; return; }
        throw new AssertionError("Se aceptó un dato inválido");
    }
    public static void main(String[] args) throws Exception {
        Sistema s = new Sistema();
        comprobar(s.iniciarSesion("admin", "Fide2026".toCharArray()) != null);
        rechazar(() -> s.iniciarSesion("admin", "incorrecta".toCharArray()));
        s.registrarCliente("101", "Ana", "88888888", "ana@ejemplo.com");
        comprobar(s.buscarCliente("101").getNombre().equals("Ana"));
        rechazar(() -> s.registrarCliente("101", "Otro", "88888888", "otro@ejemplo.com"));
        rechazar(() -> s.registrarCliente("102", "", "88888888", "ana@ejemplo.com"));
        rechazar(() -> s.registrarCliente("102", "Ana", "88888888", "correo"));
        Inventario i = s.getInventario();
        i.agregarProducto("P01", "Arroz", "Granos", "1500,50", "12");
        comprobar(i.buscarProducto("p01").getCantidadDisponible() == 12);
        comprobar(i.buscarProducto("P01").getPrecio().toPlainString().equals("1500.50"));
        rechazar(() -> i.agregarProducto("p01", "Arroz", "Granos", "1000", "2"));
        rechazar(() -> i.agregarProducto("P02", "Arroz", "Granos", "-1", "2"));
        rechazar(() -> i.agregarProducto("P02", "Arroz", "Granos", "0", "2"));
        rechazar(() -> i.agregarProducto("P02", "Arroz", "Granos", "100", "-1"));
        rechazar(() -> i.agregarProducto("P02", "Arroz", "Granos", "100", "1.5"));
        rechazar(() -> i.agregarProducto("P02", "Arroz", "Granos", "texto", "1"));
        rechazar(() -> i.agregarProducto("P02", "Arroz", "Granos", "1.234", "1"));
        comprobar(i.consultarExistencias().size() == 1);
        i.agregarProducto("P02", "Aceite", "Abarrotes", "2000", "0");
        comprobar(i.buscarProducto("P02").getCantidadDisponible() == 0);
        Persona[] personas = {s.buscarCliente("101"), s.iniciarSesion("admin", "Fide2026".toCharArray())};
        comprobar(personas[0].descripcion().startsWith("Cliente:"));
        comprobar(personas[1].descripcion().startsWith("Empleado:"));
        System.out.println("OK: " + total + " comprobaciones.");
    }
}
