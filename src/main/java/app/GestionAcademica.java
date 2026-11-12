package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import vista.frmInicio;

public class GestionAcademica {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            frmInicio inicio = new frmInicio();
            inicio.setVisible(true);
        });
    }
}