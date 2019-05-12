import GUI.BEGEOT_BUNOUF_GUI_Login;
import GUI.BEGEOT_BUNOUF_GUI_noConnexion;
import UsefulFunctions.BEGEOT_BUNOUF_Database_Connection;

import javax.swing.*;


/**
 * Class main du projet
 *
 * @author Célia
 */
public class BEGEOT_BUNOUF_Main {
    public static void main(String[] argv) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();

        if (database.getConnexion() == null) {
            database.Database_Deconnection();
            new BEGEOT_BUNOUF_GUI_noConnexion();
        } else {
            database.Database_Deconnection();
            new BEGEOT_BUNOUF_GUI_Login();
        }
    }
}