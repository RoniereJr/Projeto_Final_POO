/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package views;

/**
 *
 * @author Roni
 */
import javax.swing.SwingUtilities;

public class Projeto_Final_POO {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}