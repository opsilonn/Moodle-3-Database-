import GUI.GUI_Login;
import GUI.GUI_noConnexion;
import GUI_Components.CustomJFrame;
import Gestion_admin.Database_Connection;
import javax.swing.*;


/**
 * Class main du projet
 *
 * @author Célia
 */
public class Main
{
    public static void main(String[] argv) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Database_Connection database = new Database_Connection();
        CustomJFrame frame;

        if( database.getConnexion() == null )
            frame = new GUI_noConnexion();
        else
            frame = new GUI_Login();

        database.Database_Deconnection();
    }
}