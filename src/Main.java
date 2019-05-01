import GUI.GUI_Login;
import Gestion_admin.Database_Connection;
import javax.swing.*;


/**
 * Class main du projet : HUB d'où l'on lance le logiciel
 *
 * @author Célia
 */
public class Main
{
    public static void main(String[] argv) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Database_Connection database = new Database_Connection();
        GUI_Login Login = new GUI_Login(database);

        Login log_etud = new Login(20160024, "bla");
        log_etud.LogIn();
        Login log_admin = new Login(00000000, "admin");
        log_admin.LogIn();
    }
}