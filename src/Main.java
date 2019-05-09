import GUI.GUI_Login;
import GUI.GUI_noConnexion;
import Gestion_admin.Database_Connection;

import javax.swing.*;


/**
 * Class main du projet
 *
 * @author Célia
 */
public class Main {
    public static void main(String[] argv) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Database_Connection database = new Database_Connection();

        if (database.getConnexion() == null) {
            database.Database_Deconnection();
            new GUI_noConnexion();
        } else {
            database.Database_Deconnection();
            new GUI_Login();
        }
    }
}