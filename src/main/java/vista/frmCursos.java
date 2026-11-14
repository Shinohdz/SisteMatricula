package vista;

import dao.CursoDAO;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import modelo.Curso;
import modelo.Usuario;

public class frmCursos extends JFrame {

    private final Usuario usuarioActual;
    private final CursoDAO cursoDAO;

    private JTextField txtCodigo, txtNombre, txtDescripcion, txtTutor, txtCupo, txtBuscar;
    private JTable tabla;
    private DefaultTableModel modelo;

    public frmCursos(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.cursoDAO = new CursoDAO();

        setTitle("Gestión de Cursos - UES");
        setSize(920, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        inicializarComponentes();
        cargarTabla(cursoDAO.obtenerCursosOrdenados(false));
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton btnVolver = new JButton("<< Volver al Menú Principal");
        btnVolver.setBounds(20, 12, 210, 28);
        panel.add(btnVolver);

        JPanel form = new JPanel();
        form.setLayout(null);
        form.setBorder(BorderFactory.createTitledBorder("Datos del Curso"));
        form.setBounds(20, 48, 865, 130);

        JLabel lblCod = new JLabel("Código:");
        lblCod.setBounds(15, 25, 70, 25);
        form.add(lblCod);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(85, 25, 130, 25);
        form.add(txtCodigo);

        JLabel lblNom = new JLabel("Nombre:");
        lblNom.setBounds(230, 25, 70, 25);
        form.add(lblNom);

        txtNombre = new JTextField();
        txtNombre.setBounds(295, 25, 250, 25);
        form.add(txtNombre);

        JLabel lblCupo = new JLabel("Cupo Máx:");
        lblCupo.setBounds(560, 25, 80, 25);
        form.add(lblCupo);

        txtCupo = new JTextField();
        txtCupo.setBounds(640, 25, 100, 25);
        form.add(txtCupo);

        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setBounds(15, 60, 80, 25);
        form.add(lblDesc);

        txtDescripcion = new JTextField();
        txtDescripcion.setBounds(95, 60, 310, 25);
        form.add(txtDescripcion);

        JLabel lblTutor = new JLabel("Docente:");
        lblTutor.setBounds(420, 60, 70, 25);
        form.add(lblTutor);

        txtTutor = new JTextField();
        txtTutor.setBounds(490, 60, 350, 25);
        form.add(txtTutor);

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

        JButton btnOrdenCod = new JButton("Ordenar por Código");
        btnOrdenCod.setBounds(485, 92, 170, 28);
        form.add(btnOrdenCod);

        JButton btnOrdenNom = new JButton("Ordenar por Nombre");
        btnOrdenNom.setBounds(665, 92, 175, 28);
        form.add(btnOrdenNom);

        panel.add(form);

        JLabel lblBuscar = new JLabel("Buscar (Código / Nombre):");
        lblBuscar.setBounds(20, 190, 180, 25);
        panel.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(200, 190, 220, 25);
        panel.add(txtBuscar);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(430, 190, 100, 25);
        panel.add(btnBuscar);

        JButton btnTodos = new JButton("Mostrar Todos");
        btnTodos.setBounds(540, 190, 130, 25);
        panel.add(btnTodos);

        modelo = new DefaultTableModel(
                new Object[]{"Código", "Nombre", "Descripción", "Docente Responsable", "Cupo Máx.", "Matriculados", "Cupos Disponibles"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 225, 865, 280);
        panel.add(scroll);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                txtCodigo.setText(modelo.getValueAt(fila, 0).toString());
                txtNombre.setText(modelo.getValueAt(fila, 1).toString());
                txtDescripcion.setText(modelo.getValueAt(fila, 2).toString());
                txtTutor.setText(modelo.getValueAt(fila, 3).toString());
                txtCupo.setText(modelo.getValueAt(fila, 4).toString());
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
        btnOrdenCod.addActionListener(e -> cargarTabla(cursoDAO.obtenerCursosOrdenados(false)));
        btnOrdenNom.addActionListener(e -> cargarTabla(cursoDAO.obtenerCursosOrdenados(true)));

        btnBuscar.addActionListener(e -> {
            Curso c = cursoDAO.buscarCurso(txtBuscar.getText());
            if (c != null) {
                cargarTabla(List.of(c));
            } else {
                JOptionPane.showMessageDialog(this, "Curso no encontrado.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnTodos.addActionListener(e -> {
            txtBuscar.setText("");
            cargarTabla(cursoDAO.obtenerCursosOrdenados(false));
        });

        setContentPane(panel);
    }

    private void registrar() {
        String cod = txtCodigo.getText().trim().toUpperCase();
        String nom = txtNombre.getText().trim();
        String desc = txtDescripcion.getText().trim();
        String tutor = txtTutor.getText().trim();
        String cupoStr = txtCupo.getText().trim();

        if (cod.isEmpty() || nom.isEmpty() || desc.isEmpty() || tutor.isEmpty() || cupoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cupo;
        try {
            cupo = Integer.parseInt(cupoStr);
            if (cupo <= 0) {
                JOptionPane.showMessageDialog(this, "El cupo máximo debe ser un entero positivo.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El cupo máximo debe ser un número entero.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String res = cursoDAO.registrarCurso(new Curso(cod, nom, desc, tutor, cupo, 0));
        if ("OK".equals(res)) {
            JOptionPane.showMessageDialog(this, "Curso registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
            cargarTabla(cursoDAO.obtenerCursosOrdenados(false));
        } else {
            JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar() {
        String cod = txtCodigo.getText().trim().toUpperCase();
        String nom = txtNombre.getText().trim();
        String desc = txtDescripcion.getText().trim();
        String tutor = txtTutor.getText().trim();
        String cupoStr = txtCupo.getText().trim();

        if (cod.isEmpty() || nom.isEmpty() || desc.isEmpty() || tutor.isEmpty() || cupoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso y complete todos los campos.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cupo;
        try {
            cupo = Integer.parseInt(cupoStr);
            if (cupo <= 0) {
                JOptionPane.showMessageDialog(this, "El cupo máximo debe ser positivo.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número entero válido.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String res = cursoDAO.modificarCurso(cod, nom, desc, tutor, cupo);
        if ("OK".equals(res)) {
            JOptionPane.showMessageDialog(this, "Curso modificado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla(cursoDAO.obtenerCursosOrdenados(false));
        } else {
            JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        String cod = txtCodigo.getText().trim();
        if (cod.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar el curso " + cod + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            String res = cursoDAO.eliminarCurso(cod);
            if ("OK".equals(res)) {
                JOptionPane.showMessageDialog(this, "Curso eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiar();
                cargarTabla(cursoDAO.obtenerCursosOrdenados(false));
            } else {
                JOptionPane.showMessageDialog(this, res, "Operación Bloqueada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtTutor.setText("");
        txtCupo.setText("");
        tabla.clearSelection();
    }

    private void cargarTabla(List<Curso> lista) {
        modelo.setRowCount(0);
        for (Curso c : lista) {
            modelo.addRow(new Object[]{
                c.getCodCurso(),
                c.getNombre(),
                c.getDescripcion(),
                c.getTutor(),
                c.getCupoMaximo(),
                c.getMatriculadosActivos(),
                c.getCuposDisponibles()
            });
        }
    }
}