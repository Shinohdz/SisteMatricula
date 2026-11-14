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
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import modelo.Curso;
import modelo.Estudiante;
import modelo.Usuario;

public class frmMenu extends JFrame {

    private final Usuario usuarioActual;
    private final EstudianteDAO estudianteDAO;
    private final CursoDAO cursoDAO;
    private final MatriculaDAO matriculaDAO;

    private JComboBox<Curso> cmbCursoConsulta;
    private JComboBox<Estudiante> cmbEstudianteConsulta;
    private DefaultTableModel modeloConsultas;
    private DefaultTableModel modeloResumen;

    public frmMenu(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.estudianteDAO = new EstudianteDAO();
        this.cursoDAO = new CursoDAO();
        this.matriculaDAO = new MatriculaDAO();

        setTitle("Menú Principal - Gestión Académica UES | Usuario: " + usuarioActual.getNombreCompleto());
        setSize(940, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        inicializarComponentes();
        cargarCombosYResumen();
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblBienvenida = new JLabel("Usuario activo: " + usuarioActual.getNombreCompleto() + " (" + usuarioActual.getRol() + ")");
        lblBienvenida.setBounds(20, 15, 450, 25);
        panel.add(lblBienvenida);

        JButton btnEstudiantes = new JButton("1. Gestión de Estudiantes");
        btnEstudiantes.setBounds(20, 50, 210, 35);
        panel.add(btnEstudiantes);

        JButton btnCursos = new JButton("2. Gestión de Cursos");
        btnCursos.setBounds(245, 50, 210, 35);
        panel.add(btnCursos);

        JButton btnMatriculas = new JButton("3. Gestión de Matrículas");
        btnMatriculas.setBounds(470, 50, 210, 35);
        panel.add(btnMatriculas);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBounds(750, 15, 150, 30);
        panel.add(btnCerrarSesion);

        // Panel de Consultas Académicas (Proceso 4)
        JPanel panelConsultas = new JPanel();
        panelConsultas.setLayout(null);
        panelConsultas.setBorder(BorderFactory.createTitledBorder("Consultas Académicas por Curso o por Estudiante"));
        panelConsultas.setBounds(20, 100, 885, 230);

        JLabel lblCurso = new JLabel("Curso:");
        lblCurso.setBounds(15, 25, 50, 25);
        panelConsultas.add(lblCurso);

        cmbCursoConsulta = new JComboBox<>();
        cmbCursoConsulta.setBounds(65, 25, 220, 25);
        panelConsultas.add(cmbCursoConsulta);

        JButton btnEstPorCurso = new JButton("Ver Estudiantes del Curso");
        btnEstPorCurso.setBounds(295, 25, 190, 25);
        panelConsultas.add(btnEstPorCurso);

        JLabel lblEst = new JLabel("Estudiante:");
        lblEst.setBounds(500, 25, 70, 25);
        panelConsultas.add(lblEst);

        cmbEstudianteConsulta = new JComboBox<>();
        cmbEstudianteConsulta.setBounds(570, 25, 160, 25);
        panelConsultas.add(cmbEstudianteConsulta);

        JButton btnCurPorEst = new JButton("Ver Cursos");
        btnCurPorEst.setBounds(740, 25, 130, 25);
        panelConsultas.add(btnCurPorEst);

        modeloConsultas = new DefaultTableModel(new Object[]{"Código / Carnet", "Nombre", "Detalle 1", "Detalle 2"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaConsultas = new JTable(modeloConsultas);
        JScrollPane scrollConsultas = new JScrollPane(tablaConsultas);
        scrollConsultas.setBounds(15, 60, 855, 155);
        panelConsultas.add(scrollConsultas);

        panel.add(panelConsultas);

        // Panel de Resumen de Matrícula y Cupos Disponibles
        JPanel panelResumen = new JPanel();
        panelResumen.setLayout(null);
        panelResumen.setBorder(BorderFactory.createTitledBorder("Resumen General de Matrícula y Cupos Disponibles por Curso"));
        panelResumen.setBounds(20, 340, 885, 225);

        modeloResumen = new DefaultTableModel(
                new Object[]{"Código Curso", "Nombre del Curso", "Docente Responsable", "Cupo Máximo", "Matriculados Activos", "Cupos Disponibles"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaResumen = new JTable(modeloResumen);
        JScrollPane scrollResumen = new JScrollPane(tablaResumen);
        scrollResumen.setBounds(15, 25, 855, 185);
        panelResumen.add(scrollResumen);

        panel.add(panelResumen);

        // Eventos con EDT
        btnEstudiantes.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            new frmEstudiantes(usuarioActual).setVisible(true);
            this.dispose();
        }));

        btnCursos.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            new frmCursos(usuarioActual).setVisible(true);
            this.dispose();
        }));

        btnMatriculas.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            new frmMatriculas(usuarioActual).setVisible(true);
            this.dispose();
        }));

        btnCerrarSesion.addActionListener(e -> {
            int conf = JOptionPane.showConfirmDialog(this,
                    "¿Desea cerrar la sesión actual?",
                    "Cerrar Sesión",
                    JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(() -> {
                    new frmInicio().setVisible(true);
                    this.dispose();
                });
            }
        });

        btnEstPorCurso.addActionListener(e -> consultarEstudiantesDeCurso());
        btnCurPorEst.addActionListener(e -> consultarCursosDeEstudiante());

        setContentPane(panel);
    }

    private void cargarCombosYResumen() {
        cmbCursoConsulta.removeAllItems();
        modeloResumen.setRowCount(0);
        List<Curso> cursos = cursoDAO.obtenerCursosOrdenados(false);
        for (Curso c : cursos) {
            cmbCursoConsulta.addItem(c);
            modeloResumen.addRow(new Object[]{
                c.getCodCurso(),
                c.getNombre(),
                c.getTutor(),
                c.getCupoMaximo(),
                c.getMatriculadosActivos(),
                c.getCuposDisponibles()
            });
        }

        cmbEstudianteConsulta.removeAllItems();
        for (Estudiante est : estudianteDAO.obtenerOrdenadosPorCarnet()) {
            cmbEstudianteConsulta.addItem(est);
        }
    }

    private void consultarEstudiantesDeCurso() {
        Curso c = (Curso) cmbCursoConsulta.getSelectedItem();
        if (c == null) return;
        modeloConsultas.setColumnIdentifiers(new Object[]{"Carnet", "Nombre Completo", "Documento", "Correo"});
        modeloConsultas.setRowCount(0);
        List<Estudiante> inscritos = matriculaDAO.buscarEstudiantesPorCurso(c.getCodCurso());
        for (Estudiante e : inscritos) {
            modeloConsultas.addRow(new Object[]{e.getCarnet(), e.getNombre(), e.getDocumento(), e.getCorreo()});
        }
        if (inscritos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay estudiantes activos en este curso.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void consultarCursosDeEstudiante() {
        Estudiante est = (Estudiante) cmbEstudianteConsulta.getSelectedItem();
        if (est == null) return;
        modeloConsultas.setColumnIdentifiers(new Object[]{"Código Curso", "Nombre del Curso", "Docente", "Cupos Disponibles"});
        modeloConsultas.setRowCount(0);
        List<Curso> asociados = matriculaDAO.buscarCursosPorEstudiante(est.getCarnet());
        for (Curso c : asociados) {
            modeloConsultas.addRow(new Object[]{c.getCodCurso(), c.getNombre(), c.getTutor(), c.getCuposDisponibles()});
        }
        if (asociados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El estudiante no tiene cursos activos inscritos.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}