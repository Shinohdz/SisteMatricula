package vista;

import dao.EstudianteDAO;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import modelo.Estudiante;
import modelo.Usuario;

public class frmEstudiantes extends JFrame {

    private final Usuario usuarioActual;
    private final EstudianteDAO estudianteDAO;

    private JTextField txtCarnet, txtNombre, txtDocumento, txtCorreo, txtBuscar;
    private JCheckBox chkActivo;
    private JTable tabla;
    private DefaultTableModel modelo;

    private static final Pattern PATRON_CORREO = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public frmEstudiantes(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.estudianteDAO = new EstudianteDAO();

        setTitle("Gestión de Estudiantes - UES");
        setSize(920, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        inicializarComponentes();
        cargarTabla(estudianteDAO.obtenerOrdenadosPorCarnet());
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton btnVolver = new JButton("<< Volver al Menú Principal");
        btnVolver.setBounds(20, 12, 210, 28);
        panel.add(btnVolver);

        JPanel form = new JPanel();
        form.setLayout(null);
        form.setBorder(BorderFactory.createTitledBorder("Datos del Estudiante"));
        form.setBounds(20, 48, 865, 130);

        JLabel lblCarnet = new JLabel("Carnet:");
        lblCarnet.setBounds(15, 25, 80, 25);
        form.add(lblCarnet);

        txtCarnet = new JTextField();
        txtCarnet.setBounds(100, 25, 160, 25);
        form.add(txtCarnet);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(280, 25, 80, 25);
        form.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(350, 25, 260, 25);
        form.add(txtNombre);

        chkActivo = new JCheckBox("Estudiante Activo", true);
        chkActivo.setBounds(630, 25, 160, 25);
        form.add(chkActivo);

        JLabel lblDoc = new JLabel("DUI:");
        lblDoc.setBounds(15, 60, 80, 25);
        form.add(lblDoc);

        txtDocumento = new JTextField();
        txtDocumento.setBounds(100, 60, 160, 25);
        form.add(txtDocumento);

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setBounds(280, 60, 80, 25);
        form.add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setBounds(350, 60, 260, 25);
        form.add(txtCorreo);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(15, 92, 110, 28);
        form.add(btnRegistrar);

        JButton btnModificar = new JButton("Modificar");
        btnModificar.setBounds(135, 92, 110, 28);
        form.add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(255, 92, 110, 28);
        form.add(btnEliminar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(375, 92, 100, 28);
        form.add(btnLimpiar);

        JButton btnOrdenCarnet = new JButton("Orden Carnet (Comparable)");
        btnOrdenCarnet.setBounds(485, 92, 185, 28);
        form.add(btnOrdenCarnet);

        JButton btnOrdenNombre = new JButton("Orden Nombre (Comparator)");
        btnOrdenNombre.setBounds(675, 92, 180, 28);
        form.add(btnOrdenNombre);

        panel.add(form);

        JLabel lblBuscar = new JLabel("Buscar (Carnet / DUI / Nombre):");
        lblBuscar.setBounds(20, 190, 200, 25);
        panel.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(220, 190, 220, 25);
        panel.add(txtBuscar);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(450, 190, 100, 25);
        panel.add(btnBuscar);

        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        btnMostrarTodos.setBounds(560, 190, 130, 25);
        panel.add(btnMostrarTodos);

        modelo = new DefaultTableModel(
                new Object[]{"Carnet", "Nombre Completo", "Documento", "Correo", "Estado", "Cursos Activos"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 225, 865, 280);
        panel.add(scroll);

        // Eventos
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                txtCarnet.setText(modelo.getValueAt(fila, 0).toString());
                txtNombre.setText(modelo.getValueAt(fila, 1).toString());
                txtDocumento.setText(modelo.getValueAt(fila, 2).toString());
                txtCorreo.setText(modelo.getValueAt(fila, 3).toString());
                chkActivo.setSelected("ACTIVO".equals(modelo.getValueAt(fila, 4).toString()));
            }
        });

        btnVolver.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            new frmMenu(usuarioActual).setVisible(true);
            this.dispose();
        }));

        btnRegistrar.addActionListener(e -> registrar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());
        btnOrdenCarnet.addActionListener(e -> cargarTabla(estudianteDAO.obtenerOrdenadosPorCarnet()));
        btnOrdenNombre.addActionListener(e -> cargarTabla(estudianteDAO.obtenerOrdenadosPorNombre()));

        btnBuscar.addActionListener(e -> {
            Estudiante encontrado = estudianteDAO.buscarEstudiante(txtBuscar.getText());
            if (encontrado != null) {
                cargarTabla(List.of(encontrado));
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el estudiante.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnMostrarTodos.addActionListener(e -> {
            txtBuscar.setText("");
            cargarTabla(estudianteDAO.obtenerOrdenadosPorCarnet());
        });

        setContentPane(panel);
    }

    private void registrar() {
        String carnet = txtCarnet.getText().trim().toUpperCase();
        String nombre = txtNombre.getText().trim();
        String doc = txtDocumento.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (carnet.isEmpty() || nombre.isEmpty() || doc.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!PATRON_CORREO.matcher(correo).matches()) {
            JOptionPane.showMessageDialog(this, "Formato de correo electrónico inválido.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String res = estudianteDAO.registrarEstudiante(new Estudiante(carnet, nombre, doc, correo, chkActivo.isSelected()));
        if ("OK".equals(res)) {
            JOptionPane.showMessageDialog(this, "Estudiante registrado en MongoDB.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
            cargarTabla(estudianteDAO.obtenerOrdenadosPorCarnet());
        } else {
            JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar() {
        String carnet = txtCarnet.getText().trim().toUpperCase();
        String nombre = txtNombre.getText().trim();
        String doc = txtDocumento.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (carnet.isEmpty() || nombre.isEmpty() || doc.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante y complete los campos.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!PATRON_CORREO.matcher(correo).matches()) {
            JOptionPane.showMessageDialog(this, "Formato de correo electrónico inválido.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String res = estudianteDAO.modificarEstudiante(carnet, nombre, doc, correo, chkActivo.isSelected());
        if ("OK".equals(res)) {
            JOptionPane.showMessageDialog(this, "Estudiante actualizado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla(estudianteDAO.obtenerOrdenadosPorCarnet());
        } else {
            JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        String carnet = txtCarnet.getText().trim();
        if (carnet.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar al estudiante " + carnet + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            String res = estudianteDAO.eliminarEstudiante(carnet);
            if ("OK".equals(res)) {
                JOptionPane.showMessageDialog(this, "Estudiante eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiar();
                cargarTabla(estudianteDAO.obtenerOrdenadosPorCarnet());
            } else {
                JOptionPane.showMessageDialog(this, res, "Operación Bloqueada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiar() {
        txtCarnet.setText("");
        txtNombre.setText("");
        txtDocumento.setText("");
        txtCorreo.setText("");
        chkActivo.setSelected(true);
        tabla.clearSelection();
    }

    private void cargarTabla(List<Estudiante> lista) {
        modelo.setRowCount(0);
        for (Estudiante e : lista) {
            modelo.addRow(new Object[]{
                e.getCarnet(),
                e.getNombre(),
                e.getDocumento(),
                e.getCorreo(),
                e.isActivo() ? "ACTIVO" : "INACTIVO",
                String.join(", ", e.getCursosEnCurso())
            });
        }
    }
}