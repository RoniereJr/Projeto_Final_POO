import com.formdev.flatlaf.FlatLightLaf;
import dao.BibliotecaFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.UIManager;
import views.LoginView;

public class app {
    public static void main(String args[]) {
      
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(() -> new LoginView().setVisible(true));
    }
}