package fidecompro;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Interfaz del segundo avance: HU01, HU02, HU03 y HU04. */
public class Main extends JFrame {
    private final Sistema sistema = new Sistema();
    private final CardLayout tarjetas = new CardLayout();
    private final JPanel contenido = new JPanel(tarjetas);
    private final JLabel sesion = new JLabel();
    private final JTextField usuario = new JTextField(18);
    private final JPasswordField clave = new JPasswordField(18);
    private final DefaultTableModel clientes = modelo("Identificación", "Nombre", "Teléfono", "Correo");
    private final DefaultTableModel productos = modelo("Código", "Nombre", "Categoría", "Precio (CRC)", "Existencias");
    private final DefaultTableModel existencias = modelo("Código", "Nombre", "Categoría", "Precio (CRC)", "Existencias");
    private final NumberFormat moneda = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CR"));

    public Main() {
        super("Fidecompro — Avance 2");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        moneda.setMinimumFractionDigits(2); moneda.setMaximumFractionDigits(2);
        contenido.add(login(), "login"); contenido.add(menu(), "menu");
        contenido.add(pantallaClientes(), "clientes");
        contenido.add(pantallaProductos(), "productos");
        contenido.add(pantallaInventario(), "inventario");
        setContentPane(contenido); setSize(1000, 680); setMinimumSize(new Dimension(820, 580));
        setLocationRelativeTo(null); tarjetas.show(contenido, "login");
    }
    private static DefaultTableModel modelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
    }
    private JPanel base(String titulo) {
        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        JLabel h = new JLabel("FIDECOMPRO  |  " + titulo);
        h.setFont(new Font("SansSerif", Font.BOLD, 23)); h.setForeground(new Color(24, 67, 98));
        p.add(h, BorderLayout.NORTH); return p;
    }
    private JButton boton(String texto, Runnable accion) {
        JButton b = new JButton(texto); b.addActionListener(e -> accion.run()); return b;
    }
    private JScrollPane tabla(DefaultTableModel m) {
        JTable t = new JTable(m); t.setRowHeight(28); t.setAutoCreateRowSorter(true);
        t.setFillsViewportHeight(true); t.getTableHeader().setReorderingAllowed(false);
        return new JScrollPane(t);
    }
    private JPanel formulario(String[] etiquetas, JTextField[] campos) {
        JPanel p = new JPanel(new GridLayout(0, 2, 12, 9));
        for (int i = 0; i < etiquetas.length; i++) { p.add(new JLabel(etiquetas[i])); p.add(campos[i]); }
        return p;
    }
    private JTextField[] campos(int n) {
        JTextField[] a = new JTextField[n]; for (int i = 0; i < n; i++) a[i] = new JTextField(20); return a;
    }
    private JPanel login() {
        JPanel p = base("Inicio de sesión");
        JPanel centro = new JPanel(new GridBagLayout());
        JPanel f = new JPanel(new BorderLayout(10, 18));
        f.add(formulario(new String[]{"Usuario", "Contraseña"}, new JTextField[]{usuario, clave}), BorderLayout.CENTER);
        JButton ingresar = boton("Ingresar", () -> {
            char[] password = clave.getPassword();
            try {
                Persona persona = sistema.iniciarSesion(usuario.getText(), password);
                // La referencia base ejecuta el getTipo de Usuario: polimorfismo.
                sesion.setText(persona.descripcion()); clave.setText(""); tarjetas.show(contenido, "menu");
            } catch (ValidacionException ex) { error(ex.getMessage()); }
            finally { Arrays.fill(password, '\0'); clave.setText(""); }
        });
        clave.addActionListener(e -> ingresar.doClick());
        f.add(ingresar, BorderLayout.SOUTH); centro.add(f); p.add(centro, BorderLayout.CENTER);
        p.add(new JLabel("Prototipo académico • Datos temporales durante esta ejecución"), BorderLayout.SOUTH);
        return p;
    }
    private JPanel menu() {
        JPanel p = base("Menú principal"); JPanel opciones = new JPanel(new GridLayout(0, 1, 15, 15));
        opciones.setBorder(BorderFactory.createEmptyBorder(35, 160, 35, 160));
        opciones.add(sesion);
        opciones.add(boton("Clientes", () -> { actualizarClientes(""); tarjetas.show(contenido, "clientes"); }));
        opciones.add(boton("Productos", () -> { actualizarProductos(""); tarjetas.show(contenido, "productos"); }));
        opciones.add(boton("Consultar inventario", () -> { actualizarProductos(""); tarjetas.show(contenido, "inventario"); }));
        opciones.add(boton("Facturación — próximo avance", () -> JOptionPane.showMessageDialog(this,
            "HU05 a HU08 quedan pendientes para el siguiente avance.")));
        opciones.add(boton("Cerrar sesión", () -> { sesion.setText(""); usuario.setText(""); tarjetas.show(contenido, "login"); }));
        p.add(opciones, BorderLayout.CENTER); return p;
    }
    private JPanel pantallaClientes() {
        JPanel p = base("Clientes"); JTextField[] c = campos(4); JTextField buscar = new JTextField(20);
        JPanel arriba = new JPanel(new BorderLayout(10, 10));
        arriba.add(formulario(new String[]{"Identificación", "Nombre", "Teléfono", "Correo"}, c), BorderLayout.NORTH);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acciones.add(boton("Guardar cliente", () -> {
            try {
                sistema.registrarCliente(c[0].getText(), c[1].getText(), c[2].getText(), c[3].getText());
                actualizarClientes(""); buscar.setText(""); for (JTextField x : c) x.setText("");
                JOptionPane.showMessageDialog(this, "Cliente registrado.");
            } catch (ValidacionException ex) { error(ex.getMessage()); }
        }));
        acciones.add(new JLabel("Buscar identificación o nombre:")); acciones.add(buscar);
        acciones.add(boton("Consultar", () -> actualizarClientes(buscar.getText())));
        acciones.add(boton("Ver todos", () -> { buscar.setText(""); actualizarClientes(""); }));
        arriba.add(acciones, BorderLayout.SOUTH);
        JPanel cuerpo = new JPanel(new BorderLayout(10, 15)); cuerpo.add(arriba, BorderLayout.NORTH);
        cuerpo.add(tabla(clientes), BorderLayout.CENTER); p.add(cuerpo, BorderLayout.CENTER);
        p.add(boton("Volver al menú", () -> tarjetas.show(contenido, "menu")), BorderLayout.SOUTH); return p;
    }
    private JPanel pantallaProductos() {
        JPanel p = base("Productos"); JTextField[] c = campos(5);
        JPanel arriba = new JPanel(new BorderLayout(10, 10));
        arriba.add(formulario(new String[]{"Código", "Nombre", "Categoría", "Precio (ejemplo: 1500,50)", "Cantidad (entero, mínimo 0)"}, c), BorderLayout.CENTER);
        arriba.add(boton("Guardar producto", () -> {
            try {
                sistema.getInventario().agregarProducto(c[0].getText(), c[1].getText(), c[2].getText(), c[3].getText(), c[4].getText());
                actualizarProductos(""); for (JTextField x : c) x.setText("");
                JOptionPane.showMessageDialog(this, "Producto registrado.");
            } catch (ValidacionException ex) { error(ex.getMessage()); }
        }), BorderLayout.SOUTH);
        JPanel cuerpo = new JPanel(new BorderLayout(10, 15)); cuerpo.add(arriba, BorderLayout.NORTH);
        cuerpo.add(tabla(productos), BorderLayout.CENTER); p.add(cuerpo, BorderLayout.CENTER);
        p.add(boton("Volver al menú", () -> tarjetas.show(contenido, "menu")), BorderLayout.SOUTH); return p;
    }
    private JPanel pantallaInventario() {
        JPanel p = base("Inventario"); JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField buscar = new JTextField(25); filtro.add(new JLabel("Código, nombre o categoría:")); filtro.add(buscar);
        filtro.add(boton("Consultar", () -> actualizarProductos(buscar.getText())));
        filtro.add(boton("Ver todos", () -> { buscar.setText(""); actualizarProductos(""); }));
        JPanel cuerpo = new JPanel(new BorderLayout(10, 15)); cuerpo.add(filtro, BorderLayout.NORTH);
        cuerpo.add(tabla(existencias), BorderLayout.CENTER); p.add(cuerpo, BorderLayout.CENTER);
        p.add(boton("Volver al menú", () -> tarjetas.show(contenido, "menu")), BorderLayout.SOUTH); return p;
    }
    private void actualizarClientes(String filtro) {
        clientes.setRowCount(0); String q = filtro.trim().toLowerCase(Locale.ROOT);
        for (Cliente c : sistema.getClientes())
            if ((c.getIdentificacion() + " " + c.getNombre()).toLowerCase(Locale.ROOT).contains(q))
                clientes.addRow(new Object[]{c.getIdentificacion(), c.getNombre(), c.getTelefono(), c.getCorreo()});
    }
    private void actualizarProductos(String filtro) {
        productos.setRowCount(0); existencias.setRowCount(0); String q = filtro.trim().toLowerCase(Locale.ROOT);
        for (Producto p : sistema.getInventario().consultarExistencias()) {
            Object[] fila = {p.getCodigo(), p.getNombre(), p.getCategoria(), moneda.format(p.getPrecio()), p.getCantidadDisponible()};
            productos.addRow(fila);
            if ((p.getCodigo() + " " + p.getNombre() + " " + p.getCategoria()).toLowerCase(Locale.ROOT).contains(q)) existencias.addRow(fila);
        }
    }
    private void error(String mensaje) { JOptionPane.showMessageDialog(this, mensaje, "Revise los datos", JOptionPane.WARNING_MESSAGE); }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
