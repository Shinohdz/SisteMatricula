package vista;

import conexion.ConexionMongo;
import dao.UsuarioDAO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import modelo.Usuario;

public class frmInicio extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private UsuarioDAO usuarioDAO;

    public frmInicio() {
        setTitle("Inicio de Sesión - Sistema de Gestión Académica UES");
        setSize(420, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        if (ConexionMongo.probarConexion()) {
            this.usuarioDAO = new UsuarioDAO();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo conectar con MongoDB en localhost:27017.\n"
                    + "Verifique que el servicio esté activo y haya ejecutado init_mongodb.js.",
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);
        }

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblTitulo = new JLabel("SISTEMA DE GESTIÓN ACADÉMICA", SwingConstants.CENTER);
        lblTitulo.setBounds(20, 20, 365, 25);
        panel.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Autenticación de Usuario (MongoDB)", SwingConstants.CENTER);
        lblSubtitulo.setBounds(20, 45, 365, 20);
        panel.add(lblSubtitulo);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(50, 85, 100, 25);
        panel.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(150, 85, 200, 28);
        panel.add(txtUsuario);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(50, 125, 100, 25);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 125, 200, 28);
        panel.add(txtPassword);

        btnIngresar = new JButton("Iniciar Sesión");
        btnIngresar.setBounds(130, 180, 150, 32);
        panel.add(btnIngresar);

        btnIngresar.addActionListener(e -> validarCredenciales());
        txtPassword.addActionListener(e -> validarCredenciales());

        setContentPane(panel);
    }

    private void validarCredenciales() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Complete el usuario y la contraseña.",
                    "Campos Obligatorios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (usuarioDAO == null) {
            if (!ConexionMongo.probarConexion()) {
                JOptionPane.showMessageDialog(this,
                        "Sin conexión a MongoDB.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            usuarioDAO = new UsuarioDAO();
        }

        Usuario autenticado = usuarioDAO.autenticarUsuario(user, pass);
        if (autenticado != null) {
            JOptionPane.showMessageDialog(this,
                    "Bienvenido/a, " + autenticado.getNombreCompleto(),
                    "Acceso Concedido",
                    JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.invokeLater(() -> {
                frmMenu menu = new frmMenu(autenticado);
                menu.setVisible(true);
                this.dispose();
            });
        } else {
            JOptionPane.showMessageDialog(this,
                    "Credenciales incorrectas. Verifique su usuario y contraseña.",
                    "Error de Autenticación",
                    JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }
}