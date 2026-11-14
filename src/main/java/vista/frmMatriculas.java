package vista;

import dao.CursoDAO;
import dao.EstudianteDAO;
import dao.MatriculaDAO;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import modelo.Estudiante;
import modelo.Matricula;
import modelo.Usuario;

public class frmMatriculas extends JFrame {

    private final Usuario usuarioActual;
    private final MatriculaDAO matriculaDAO;
    private final EstudianteDAO estudianteDAO;
    private final CursoDAO cursoDAO;

    private JComboBox<Estudiante> cmbEstudiante;
    private JComboBox<Curso> cmbCurso;
    private JTextField txtFiltro;
    private JComboBox<String> cmbEstadoFiltro;
    private JTable tabla;
    private DefaultTableModel modelo;

    public frmMatriculas(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.matriculaDAO = new MatriculaDAO();
        this.estudianteDAO = new EstudianteDAO();
        this.cursoDAO = new CursoDAO();

        setTitle("Gestión de Matrículas - UES");
        setSize(920, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        inicializarComponentes();
        actualizarVista();
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton btnVolver = new JButton("<< Volver al Menú Principal");
        btnVolver.setBounds(20, 12, 210, 28);
        panel.add(btnVolver);

        JPanel form = new JPanel();
        form.setLayout(null);
        form.setBorder(BorderFactory.createTitledBorder("Inscripción y Retiro de Matrículas"));
        form.setBounds(20, 48, 865, 110);

        JLabel lblEst = new JLabel("Estudiante:");
        lblEst.setBounds(15, 28, 80, 25);
        form.add(lblEst);

        cmbEstudiante = new JComboBox<>();
        cmbEstudiante.setBounds(95, 28, 320, 25);
        form.add(cmbEstudiante);

        JLabel lblCur = new JLabel("Curso:");
        lblCur.setBounds(435, 28, 60, 25);
        form.add(lblCur);

        cmbCurso = new JComboBox<>();
        cmbCurso.setBounds(495, 28, 350, 25);
        form.add(cmbCurso);

        JButton btnMatricular = new JButton("Registrar Matrícula");
        btnMatricular.setBounds(15, 68, 180, 28);
        form.add(btnMatricular);

        JButton btnRetirar = new JButton("Retirar Matrícula Seleccionada");
        btnRetirar.setBounds(210, 68, 240, 28);
        form.add(btnRetirar);

        panel.add(form);

        JLabel lblFiltro = new JLabel("Filtrar (Código / Carnet / Curso):");
        lblFiltro.setBounds(20, 172, 200, 25);
        panel.add(lblFiltro);

        txtFiltro = new JTextField();
        txtFiltro.setBounds(220, 172, 180, 25);
        panel.add(txtFiltro);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(415, 172, 55, 25);
        panel.add(lblEstado);

        cmbEstadoFiltro = new JComboBox<>(new String[]{"TODOS", "ACTIVA", "RETIRADA"});
        cmbEstadoFiltro.setBounds(470, 172, 120, 25);
        panel.add(cmbEstadoFiltro);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBounds(605, 172, 100, 25);
        panel.add(btnFiltrar);

        JButton btnMostrarTodas = new JButton("Mostrar Todas");
        btnMostrarTodas.setBounds(715, 172, 130, 25);
        panel.add(btnMostrarTodas);

        modelo = new DefaultTableModel(
                new Object[]{"Código Matrícula", "Carnet", "Estudiante", "Código Curso", "Curso", "Fecha", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 210, 865, 295);
        panel.add(scroll);

        btnVolver.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            new frmMenu(usuarioActual).setVisible(true);
            this.dispose();
        }));

        btnMatricular.addActionListener(e -> {
            Estudiante est = (Estudiante) cmbEstudiante.getSelectedItem();
            Curso cur = (Curso) cmbCurso.getSelectedItem();
            if (est == null || cur == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un estudiante y un curso.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String res = matriculaDAO.matricularEstudiante(est.getCarnet(), cur.getCodCurso());
            if (res.startsWith("OK:")) {
                JOptionPane.showMessageDialog(this, "Matrícula registrada con código: " + res.substring(3), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarVista();
            } else {
                JOptionPane.showMessageDialog(this, res, "No se pudo matricular", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRetirar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione una matrícula en la tabla.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String idMat = modelo.getValueAt(fila, 0).toString();
            int conf = JOptionPane.showConfirmDialog(this, "¿Retirar la matrícula " + idMat + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                String res = matriculaDAO.retirarMatricula(idMat);
                if ("OK".equals(res)) {
                    JOptionPane.showMessageDialog(this, "Matrícula retirada y cupo liberado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarVista();
                } else {
                    JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnFiltrar.addActionListener(e -> filtrar());
        btnMostrarTodas.addActionListener(e -> {
            txtFiltro.setText("");
            cmbEstadoFiltro.setSelectedIndex(0);
            cargarTabla(matriculaDAO.obtenerMatriculas());
        });

        setContentPane(panel);
    }

    private void actualizarVista() {
        cmbEstudiante.removeAllItems();
        for (Estudiante e : estudianteDAO.obtenerOrdenadosPorCarnet()) {
            cmbEstudiante.addItem(e);
        }
        cmbCurso.removeAllItems();
        for (Curso c : cursoDAO.obtenerCursosOrdenados(false)) {
            cmbCurso.addItem(c);
        }
        cargarTabla(matriculaDAO.obtenerMatriculas());
    }

    private void filtrar() {
        String texto = txtFiltro.getText().trim().toUpperCase();
        String estSel = cmbEstadoFiltro.getSelectedItem().toString();
        List<Matricula> filtradas = matriculaDAO.obtenerMatriculas().stream().filter(m -> {
            boolean okTexto = texto.isEmpty()
                    || m.getIdMatricula().toUpperCase().contains(texto)
                    || m.getEstudiante().getCarnet().toUpperCase().contains(texto)
                    || m.getCurso().getCodCurso().toUpperCase().contains(texto);
            boolean okEstado = "TODOS".equals(estSel) || m.getEstado().equalsIgnoreCase(estSel);
            return okTexto && okEstado;
        }).toList();
        cargarTabla(filtradas);
    }

    private void cargarTabla(List<Matricula> lista) {
        modelo.setRowCount(0);
        for (Matricula m : lista) {
            modelo.addRow(new Object[]{
                m.getIdMatricula(),
                m.getEstudiante().getCarnet(),
                m.getEstudiante().getNombre(),
                m.getCurso().getCodCurso(),
                m.getCurso().getNombre(),
                m.getFechaMatricula(),
                m.getEstado()
            });
        }
    }
}