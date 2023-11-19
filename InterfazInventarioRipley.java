import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Producto {
    private int id;
    private String nombre;
    private String codigo;
    private String descripcion;
    private double precio;
    private int stock;
    private String datosRelacionados;

    public Producto(int id, String nombre, String codigo, String descripcion, double precio, int stock, String datosRelacionados) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.datosRelacionados = datosRelacionados;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDatosRelacionados() {
        return datosRelacionados;
    }
}

class Inventario {
    private List<Producto> productos;

    public Inventario() {
        productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
        producto.setStock(producto.getStock() - 1);
    }

    public void eliminarProducto(int id) {
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                productos.remove(producto);
                producto.setStock(producto.getStock() + 1);
                break;
            }
        }
    }

    public void modificarProducto(int id, int stock) {
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                producto.setStock(stock);
                break;
            }
        }
    }

    public Producto consultarProducto(int id) {
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null;
    }

    public List<Producto> obtenerProductos() {
        return productos;
    }
}

public class InterfazInventarioRipley extends JFrame {
    private Inventario inventario;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    public InterfazInventarioRipley() {
        inventario = new Inventario();

        setTitle("Gestión de Inventarios - Ripley");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Id");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Código");
        modeloTabla.addColumn("Descripción");
        modeloTabla.addColumn("Precio");
        modeloTabla.addColumn("Stock");
        modeloTabla.addColumn("Datos Relacionados");

        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);

        JButton btnAgregar = new JButton("Agregar Producto");
        JButton btnEliminar = new JButton("Eliminar Producto");
        JButton btnModificar = new JButton("Modificar Producto");
        JButton btnConsultar = new JButton("Consultar Producto");

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnConsultar);
        add(panelBotones, BorderLayout.NORTH);

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = generarIdUnico();
                String nombre = JOptionPane.showInputDialog("Ingrese el nombre del producto:");
                String codigo = JOptionPane.showInputDialog("Ingrese el código del producto:");
                String descripcion = JOptionPane.showInputDialog("Ingrese la descripción del producto:");
                double precio = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el precio del producto:"));
                int stock = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el stock del producto:"));
                String datosRelacionados = JOptionPane.showInputDialog("Ingrese los datos relacionados del producto:");

                Producto producto = new Producto(id, nombre, codigo, descripcion, precio, stock, datosRelacionados);
                inventario.agregarProducto(producto);
                actualizarTablaProductos();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del producto a eliminar:"));
                inventario.eliminarProducto(id);
                actualizarTablaProductos();
            }
        });

        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del producto a modificar:"));
                Producto producto = inventario.consultarProducto(id);
                if (producto != null) {
                    int stock = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el nuevo stock del producto:"));

                    inventario.modificarProducto(id, stock);
                    actualizarTablaProductos();
                } else {
                    JOptionPane.showMessageDialog(null, "El producto con ID " + id + " no existe.");
                }
            }
        });

        btnConsultar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del producto a consultar:"));
                Producto producto = inventario.consultarProducto(id);
                if (producto != null) {
                    JOptionPane.showMessageDialog(null, "ID: " + producto.getId() +
                            "\nNombre: " + producto.getNombre() +
                            "\nCódigo: " + producto.getCodigo() +
                            "\nDescripción: " + producto.getDescripcion() +
                            "\nPrecio: " + producto.getPrecio() +
                            "\nStock: " + producto.getStock() +
                            "\nDatos Relacionados: " + producto.getDatosRelacionados());
                } else {
                    JOptionPane.showMessageDialog(null, "El producto con ID " + id + " no existe.");
                }
            }
        });
    }

    public void mostrarInterfaz() {
        setVisible(true);
    }

    private void actualizarTablaProductos() {

        modeloTabla.setRowCount(0);


        List<Producto> productos = inventario.obtenerProductos();

        for (Producto producto : productos) {
            Object[] fila = {producto.getId(), producto.getNombre(), producto.getCodigo(), producto.getDescripcion(),
                    producto.getPrecio(), producto.getStock(), producto.getDatosRelacionados()};
            modeloTabla.addRow(fila);
        }
    }

    private int generarIdUnico() {
        return inventario.obtenerProductos().size() + 1;
    }

    public static void main(String[] args) {
        InterfazInventarioRipley interfaz = new InterfazInventarioRipley();
        interfaz.mostrarInterfaz();
    }
}
